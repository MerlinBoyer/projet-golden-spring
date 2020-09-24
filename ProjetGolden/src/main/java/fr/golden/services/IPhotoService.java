package fr.golden.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;

public interface IPhotoService {
	
	public Photo saveOnDisk(MultipartFile file, String imgPath);
	
	public Photo compressAndSaveOnDisk(String pathImgFullSize);
	
	public Photo compressAndSaveOnDiskWithCustomParams(String pathImgFullSize, Float compressionFactor, Float maxImgSize, int maxResizedW, int maxResizedH);
	
	// public byte[] getFromDisk(int id) throws IOException;
	public InputStream getFromDisk(int id, boolean isCompressedImage);
	
	public Photo getById(int id);
	
	public Photo getByName(String name);
	
	public Photo update(Photo photo);
	
	public void delete(int id);

	Photo add(Photo p);

}
