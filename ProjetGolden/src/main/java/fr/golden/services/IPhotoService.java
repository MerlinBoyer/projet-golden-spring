package fr.golden.services;

import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;

public interface IPhotoService {
	
	public Photo saveOnDisk(Album al, MultipartFile file, String img_name);
	
	public Photo getById(int id);
	
	public Photo getByName(String name);
	
	public Photo update(Photo photo);
	
	public void delete(int id);

}
