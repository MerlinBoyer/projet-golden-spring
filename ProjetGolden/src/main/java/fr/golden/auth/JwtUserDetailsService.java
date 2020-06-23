package fr.golden.auth;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.golden.services.IAdminService;
import fr.golden.services.IPublicUserService;
import fr.golden.models.PublicUser;
import fr.golden.models.Admin;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private IPublicUserService userService;
	
	@Autowired
	private IAdminService adminService;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// can be a conseiller or a client
		PublicUser p_user = userService.findByLogin(username);
		Admin admin = adminService.findByLogin(username);
		
		if ( p_user != null ) {
			
			// add Client Authority
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_CLIENT");
			List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
			updatedAuthorities.add(authority);
			return new User(p_user.getLogin(), p_user.getMdp(), updatedAuthorities);
			
		} else if ( admin != null ) {
			
			// add admin Authority
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
			List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
			updatedAuthorities.add(authority);
			return new User(admin.getLogin(), admin.getMdp(), updatedAuthorities);
			
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
	}

	public PublicUser save(PublicUser p_user) {
		// encrypt password
		p_user.setMdp(bcryptEncoder.encode(p_user.getMdp()));
		return userService.add(p_user);
	}
	
	public Admin saveAdmin(Admin admin) {
		admin.setMdp(bcryptEncoder.encode(admin.getMdp()));
		return adminService.add(admin);
	}

}