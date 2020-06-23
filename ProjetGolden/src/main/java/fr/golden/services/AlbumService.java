package fr.golden.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.golden.dao.IAlbumDao;
import fr.golden.models.Album;

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
		File newDir = new File(new File(rootPath), album.getName());
		if(newDir.exists()) {
			System.out.println("erreur : l'album existe deja");
			return null;
		}
		boolean success = newDir.mkdir();
	    if (success) {
	      System.out.println("Directory: " + album.getName() + " created");
	      return albumDao.save(album);
	    }
	    System.out.println("erreur de creation");
		return null;
	}

	
}
