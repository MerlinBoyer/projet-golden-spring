package fr.golden.services;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.golden.dao.IAlbumDao;
import fr.golden.models.Album;
import fr.golden.models.Photo;

@Service
public class AlbumService implements IAlbumService {
	
	@Autowired
	private IAlbumDao albumDao;
	
	private static final String rootPath = "albums";

	public AlbumService() {
		(new File(rootPath)).mkdir();
	}
	
	@Override
	public Album add(Album album) {
		
		// first check if rep already exists and create it
		File newDir = new File(new File(rootPath), album.getName());
		if(newDir.exists()) {
			System.out.println("erreur : l'album existe deja");
			return null;
		}
		boolean success = newDir.mkdir();
	    if (success) {
	    	// 2. create dir and save it in db
	      System.out.println("Directory: " + album.getName() + " created");
	      for(Photo p : album.getPictures()) {
	    	  p.setAlbum(album);
	      }
	      return albumDao.save(album);
	    }
	    System.out.println("erreur de creation");
		return null;
	}

	@Override
	public Album getById(int id) {
		Optional<Album> o = albumDao.findById(id);
		return o.get();
	}

	@Override
	public Album getByName(String name) {
		return albumDao.findByName(name);
	}
	
	@Override
	public List<Album> getAll() {
		return albumDao.findAll();
	}

	@Override
	public Album update(Album album) {
		return albumDao.save( album );
	}

	@Override
	public void delete(int id) {
		albumDao.deleteById(id);
	}

	
}
