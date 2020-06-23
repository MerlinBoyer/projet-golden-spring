package fr.golden.services;

import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;

public interface IPhotoService {
	
	public Photo add(Album album, Photo photo);

}
