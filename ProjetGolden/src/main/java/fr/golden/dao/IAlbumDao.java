package fr.golden.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.golden.models.Album;

public interface IAlbumDao extends JpaRepository<Album, Integer>{

}
