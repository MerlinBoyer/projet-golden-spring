package fr.golden.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
	public InputStream getFromDisk(int id) {
		Optional<Photo> o = photoDao.findById(id);
		Photo p = null;
		try {
			p = o.get();
		} catch (final NoSuchElementException ex) {
			ex.printStackTrace();
		}
		if(p == null || p.getAlbum() == null) {
			return null;
		}
		p.getAlbum().setPictures(null);
		
		String ppp = rootPath + File.separator + p.getAlbum().getName() + File.separator + p.getName();
		try {
			File file = new File( ppp );
			BufferedImage image = ImageIO.read( file );
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( image, "jpg", baos );
			InputStream fis = new ByteArrayInputStream(baos.toByteArray());
			return fis;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	@Override
	public byte[] getFromDisk(int id) throws IOException {
		Optional<Photo> o = photoDao.findById(id);
		Photo p = o.get();
		if(p == null || p.getAlbum() == null) {
			return null;
		}
		p.getAlbum().setPictures(null);
		String ppp = rootPath + File.separator + p.getAlbum().getName() + File.separator + p.getName();
		File file = new File( ppp );
		BufferedImage image = ImageIO.read( file );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( image, "jpg", baos );
		InputStream in = new ByteArrayInputStream(baos.toByteArray());
		return IOUtils.toByteArray(in);
	}
	*/
	
	@Override
	public Photo getById(int id) {
		Optional<Photo> o = photoDao.findById(id);
		Photo p = null;
		try {
			p = o.get();
		} catch (final NoSuchElementException ex) {
			ex.printStackTrace();
		}
		if(p == null || p.getAlbum() == null) {
			return p;
		}
		p.getAlbum().setPictures(null);
		return p;
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
		Optional<Photo> o = photoDao.findById(id);
		Photo p = null;
		try {
			p = o.get();
		} catch (final NoSuchElementException ex) {
			ex.printStackTrace();
			return;
		}
		String ppp = rootPath + File.separator + p.getAlbum().getName() + File.separator + p.getName();
		try {
			boolean result = Files.deleteIfExists(Paths.get(ppp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {			
			photoDao.deleteById(id);
		}
	}
	
	@Override
	public Photo add(Photo p) {
		return photoDao.save( p );
	}

}
