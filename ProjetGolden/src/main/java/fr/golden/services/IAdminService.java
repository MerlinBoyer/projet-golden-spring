package fr.golden.services;

import java.util.List;

import fr.golden.models.Admin;

public interface IAdminService {

	public List<Admin> getAll();

	public Admin getById(int id);

	public Admin add(Admin u);

	public Admin update(Admin u);

	public void delete(int id);
	
	public Admin findByLogin(String login);
}
