package fr.golden.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.golden.dao.IPublicUserDao;
import fr.golden.models.PublicUser;

@Service
public class PublicUserService implements IPublicUserService {

	@Autowired
	private IPublicUserDao uDao;
	
	
	@Override
	public List<PublicUser> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicUser getById(int id) {
		Optional<PublicUser> u = uDao.findById(id);
		return u.get();
	}

	@Override
	public PublicUser add(PublicUser u) {
		return uDao.save(u);
	}

	@Override
	public PublicUser update(PublicUser u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public PublicUser findByLogin(String login) {
		return uDao.findByLogin(login);
	}

	
}
