package fr.golden.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.golden.models.GoldenUser;
import fr.golden.models.PublicUser;

public interface IPublicUserDao extends JpaRepository<PublicUser, Integer>{

	public PublicUser findByLogin(String Login);
	
}
