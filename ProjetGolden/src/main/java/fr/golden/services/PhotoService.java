package fr.golden.services;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.*;

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
	private static final String rootPath_compressed = "albums_compressed";
	private static final Float compressionQuality = 0.9f;
	private static final Float compressed_WFactor = 0.3f;  // can be used someday
	private static final Float compressed_HFactor = 0.3f;  // can be used someday
	private static final Float maxImgSize = 1000f;
	
	@Autowired
	private IPhotoDao photoDao;
	
	
	
	
	public PhotoService() {
		super();
		(new File(rootPath)).mkdir();
		(new File(rootPath_compressed)).mkdir();
	}

	@Override
	public Photo saveOnDisk(MultipartFile image, String imgPath) {
		
		
		imgPath = rootPath + File.separator + imgPath;
		
		// write pic on disk
		try {
			Path filepath = Paths.get(imgPath);
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
	public Photo compressAndSaveOnDisk(String path) {
		String pathImgFullSize = rootPath + File.separator + path;
		File imgFullSizeFile = new File( pathImgFullSize );
		String pathImgCompressed= rootPath_compressed + File.separator + path;
		File imgCompressedFile = new File( pathImgCompressed );
		
		//first check if full size image exist
		if(!imgFullSizeFile.exists()) {
			System.out.println("impossible de compresser pathImgFullSize : image full size not found");
			return null;
		}
		
		try {
			BufferedImage image = ImageIO.read(imgFullSizeFile);
		    OutputStream os = new FileOutputStream(imgCompressedFile);

		    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		    ImageWriter writer = (ImageWriter) writers.next();

		    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		    writer.setOutput(ios);

		    ImageWriteParam param = writer.getDefaultWriteParam();
		    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		    // resize image
		    int biggestSide = image.getWidth() > image.getHeight() ? image.getWidth() : image.getHeight();
		    if(biggestSide > 1080) {
		    	Float factor =  maxImgSize / biggestSide;
		    	image = resize(image, (int) (factor * image.getWidth()), (int)(factor * image.getHeight()));
		    }
		    // compress image
		    param.setCompressionQuality(compressionQuality);
		    writer.write(null, new IIOImage(image, null, null), param);

		    os.close();
		    ios.close();
		    writer.dispose();
		} catch (IOException e) {
			System.out.println("error when compressing and saving : " + pathImgCompressed);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public InputStream getFromDisk(int id, boolean isCompressedImage) {
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
		
		// get full Image by default
		String ppp = rootPath + File.separator + p.getAlbum().getName() + File.separator + p.getName();			
		if(isCompressedImage == true) {
			ppp = rootPath_compressed + File.separator + p.getAlbum().getName() + File.separator + p.getName();			
		}
		
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
		String ppp_compressed = rootPath_compressed + File.separator + p.getAlbum().getName() + File.separator + p.getName();
		try {
			boolean result = Files.deleteIfExists(Paths.get(ppp));
			boolean result_compressed = Files.deleteIfExists(Paths.get(ppp_compressed));
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
	
	public BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	} 

}
