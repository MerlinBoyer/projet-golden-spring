package fr.golden.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;

public interface IPhotoService {
	
	public Photo saveOnDisk(Album al, MultipartFile file, String img_name);
	
	// public byte[] getFromDisk(int id) throws IOException;
	public InputStream getFromDisk(int id);
	
	public Photo getById(int id);
	
	public Photo getByName(String name);
	
	public Photo update(Photo photo);
	
	public void delete(int id);

}
