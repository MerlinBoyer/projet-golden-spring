package fr.golden.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.golden.models.Photo;

public interface IPhotoDao extends JpaRepository<Photo, Integer>{

}
