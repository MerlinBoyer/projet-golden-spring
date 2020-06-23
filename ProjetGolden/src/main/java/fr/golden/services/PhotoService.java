package fr.golden.services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;

@Service
public class PhotoService implements IPhotoService {

	@Override
	public Photo add(Album album, Photo photo) {
		System.out.println("i save an image right now");
		return photo;
	}

}
