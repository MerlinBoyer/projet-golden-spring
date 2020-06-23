package fr.golden.services;

import java.util.List;

import fr.golden.models.PublicUser;


public interface IPublicUserService {

	public List<PublicUser> getAll();

	public PublicUser getById(int id);

	public PublicUser add(PublicUser u);

	public PublicUser update(PublicUser u);

	public void delete(int id);
	
	public PublicUser findByLogin(String login);
	
}
