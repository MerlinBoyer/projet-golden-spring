package fr.golden.services;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Photo;

public class PhotoService implements IPhotoService {

	@Override
	public Photo add(@RequestBody Photo photo, @RequestParam("imageFile") MultipartFile image) {
		System.out.println("i save an image right now");
		return null;
	}

}
