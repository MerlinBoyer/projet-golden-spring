package fr.golden.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
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
		Album alb = null;
		try {
			alb = o.get();
		} catch (final NoSuchElementException ex) {
			ex.printStackTrace();
		}
		return alb;
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
	public void delete(int id) {
		Album alb = albumDao.findById(id).get();
		String name = alb.getName();
		File dirToDelete = new File(new File(rootPath), name);
		try {
			FileUtils.deleteDirectory(dirToDelete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			albumDao.deleteById(id);			
		}
	}
	
	@Override
	public Album update(Album album) {
		// rename pictures folder
		Album old_alb = albumDao.findById(album.getId()).get();
		String oldName = old_alb.getName();
		String newName = album.getName();
		File newDir = new File(new File(rootPath), newName);
		File oldDir = new File(new File(rootPath), oldName);
		oldDir.renameTo( newDir );
		
		// update album in db
		return albumDao.save( album );
	}

	
}
