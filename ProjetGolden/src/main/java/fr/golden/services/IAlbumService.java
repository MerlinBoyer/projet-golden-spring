package fr.golden.services;

import fr.golden.models.Album;

public interface IAlbumService {
	
	public Album add(Album album);
	
	public Album getById(int id);
	
	public Album getByName(String name);
	
	public Album update(Album album);
	
	public void delete(int id);

}
