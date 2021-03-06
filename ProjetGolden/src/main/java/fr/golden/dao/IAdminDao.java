package fr.golden.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.golden.models.Admin;

public interface IAdminDao extends JpaRepository<Admin, Integer>{

	public Admin findByLogin(String Login);
	
}
