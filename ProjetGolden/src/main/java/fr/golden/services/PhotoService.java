package fr.golden.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.golden.dao.IPhotoDao;
import fr.golden.models.Album;
import fr.golden.models.Photo;
@Service
public class PhotoService implements IPhotoService {

	private static final String rootPath = "albums";
	
	@Autowired
	private IPhotoDao photoDao;
	
	
	
	
	public PhotoService() {
		super();
		(new File(rootPath)).mkdir();
	}

	@Override
	public Photo saveOnDisk(Album al, MultipartFile image, String img_name) {
		
		// save pic here
//		System.out.println("i save an image right now lolilol : " + img_name);
//		System.out.println(" de l'album : " + al.getName());
//		System.out.println("save here : " + rootPath + File.separator + al.getName() + File.separator + img_name);
		
		// picture path
		String ppp = rootPath + File.separator + al.getName() + File.separator + img_name;
		
		// write pic on disk
		try {
			Path filepath = Paths.get(ppp);
			image.transferTo(filepath);
			System.out.println("saved");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}

	@Override
	public Photo getById(int id) {
		Optional<Photo> o = photoDao.findById(id);
		return o.get();
	}

	@Override
	public Photo getByName(String name) {
		return photoDao.findByName(name);
	}

	@Override
	public Photo update(Photo photo) {
		return photoDao.save( photo );
	}

	@Override
	public void delete(int id) {
		photoDao.deleteById(id);
	}

}
