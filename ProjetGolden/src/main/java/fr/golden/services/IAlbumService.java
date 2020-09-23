package fr.golden.services;

import java.util.List;

import fr.golden.models.Album;

public interface IAlbumService {
	
	public Album add(Album album);
	
	public Album getById(int id);
	
	public String getAlbumPathById(int id);
		
	public Album getByName(String name);
	
	public List<Album>getAll();
	
	public Album update(Album album);
	
	public void delete(int id);

	void synchronize();
	
}
