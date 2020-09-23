package fr.golden.webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;
import fr.golden.services.IAlbumService;
import fr.golden.services.IPhotoService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/public/album")
public class AlbumWebService {

	
	@Autowired
	private IAlbumService albumService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// l'objet WebDataBinder sert a faire le lien entre les params de la requete et
		// les objets java
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		// la methode register custom editor: a configurer la conversion du param recu
		// au type de l'attribut
		// L'objet customdateeditor : sert a lier la date recu comme param de la requete
		// a l'attribut de l'objet
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
	}
	
	@GetMapping(value="/getAll", produces="application/json")
	public List<Album> getAll( ) {
		
		List<Album> allAlbums = albumService.getAll();
		for(Album al : allAlbums) {
			al.setCode("");
			if(al.getVisibility() == 0) {
				al.setPictures( null );
			}
		}
		return allAlbums;
	}
	
	@GetMapping(value="/{pId}/{pCode}", produces="application/json")
	public Album getById(@PathVariable("pId") int id, @PathVariable(value = "pCode", required = false) String code) {
		
		Album album = albumService.getById(id);
		
		System.out.println("get Album : " + album.getCode() + " vs " + code);
		
		if(album.getVisibility() == 0 && album.getCode().compareTo(code) != 0 ) {
			return null;
		} else {
			return album;
		}
		
		
	}
	
	@GetMapping(value="/download/{pId}/{pCode}", produces="application/zip")
	public void downloadById(@PathVariable("pId") int id, 
			@PathVariable(value = "pCode", required = false) String pCode,
			HttpServletResponse response	) throws IOException {
		
		Album album = albumService.getById(id);
		if(album.getVisibility() == 0 && album.getCode().compareTo(pCode) != 0 ) {
			System.out.println("Code album num " + id + "non valide, download aborted");
			return;
		}
		
		String zipPath = albumService.getAlbumPathById(id);
		
		System.out.println("DOWNLOAD ALBUM : " +zipPath );
		
		//setting headers  
	    response.setStatus(HttpServletResponse.SC_OK);
	    response.addHeader("Content-Disposition", "attachment; filename=\"album.zip\"");

	    ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

	    // create a list to add files to be zipped
	    File root = new File( zipPath );
	    File[] files = root.listFiles();

	    // package files
	    for (File file : files) {
	        //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
	        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
	        FileInputStream fileInputStream = new FileInputStream(file);

	        IOUtils.copy(fileInputStream, zipOutputStream);

	        fileInputStream.close();
	        zipOutputStream.closeEntry();
	    }    

	    zipOutputStream.close();
	}
}
