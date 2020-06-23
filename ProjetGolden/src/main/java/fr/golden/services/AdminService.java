package fr.golden.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.golden.dao.IAdminDao;
import fr.golden.models.Admin;

@Service
public class AdminService implements IAdminService {

	@Autowired
	private IAdminDao uDao;
	
	
	@Override
	public List<Admin> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Admin getById(int id) {
		Optional<Admin> u = uDao.findById(id);
		return u.get();
	}

	@Override
	public Admin add(Admin u) {
		return uDao.save(u);
	}

	@Override
	public Admin update(Admin u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Admin findByLogin(String login) {
		return uDao.findByLogin(login);
	}

	
}
