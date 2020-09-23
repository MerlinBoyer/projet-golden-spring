package fr.golden.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
	private static final String rootPath_compressed = "albums_compressed";

	public AlbumService() {
		(new File(rootPath)).mkdir();
		(new File(rootPath_compressed)).mkdir();
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
	    	// create dir for compressed images
	    	File compressedDir = new File(new File(rootPath_compressed), album.getName());
	    	compressedDir.mkdir();
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
	public String getAlbumPathById(int id) {
		Optional<Album> o = albumDao.findById(id);
		Album alb = null;
		try {
			alb = o.get();
		} catch (final NoSuchElementException ex) {
			ex.printStackTrace();
		}
		return rootPath + File.separator + alb.getName();
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
	
	
	@Override
	public void synchronize() {
		this.parseAlbums(rootPath);
	}
	
	public void parseAlbums( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                syncAlbum( f.getAbsoluteFile() );
            }
        }
    }
	
	
	// TODO should also synchronize with compressed images
	public void syncAlbum(File f) {
		String name = f.getName();
		System.out.println("candidat album : " + name);
		Album alb = this.getByName(name);
		if( alb == null ) {
			Album new_alb = new Album();
			new_alb.setName(name);
			new_alb.setCreation_date(new Date());
			
			File[] list = f.listFiles();
			if (list == null) return;

	        for ( File pic : list ) {
	        	if ( pic.isDirectory() ) {
	                syncAlbum( pic.getAbsoluteFile() );
	            }
	            String picName = pic.getName();
	            Photo p = new Photo();
	            p.setName(picName);
	            p.setDateCreation(new Date());
	            new_alb.addPic( p );
	            p.setAlbum(new_alb);
	        }
	        
	        albumDao.save(new_alb);
	        
	        System.out.println(" ---> created");
		} else {
			System.out.println(" ---> already exist");
		}
		
	}

	
}
