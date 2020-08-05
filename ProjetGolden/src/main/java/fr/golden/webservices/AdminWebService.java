package fr.golden.webservices;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Album;
import fr.golden.models.Photo;
import fr.golden.services.IAlbumService;
import fr.golden.services.IPhotoService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/admin")
public class AdminWebService {
	
	@Autowired
	private IPhotoService photoService;
	
	
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
	
	@PostMapping(value="/registerAlbum", produces="application/json")
	public Album register(@RequestBody Album album) {
		return albumService.add(album);
	}
	
	@PostMapping(value="/savePhoto")
	public Photo register(@RequestParam("imageFile") MultipartFile file,
			@RequestParam("album_name") String album_name,
			@RequestParam("album_id") String al_id) throws IOException {
		
		System.out.println(" img : " + file.getOriginalFilename());
		System.out.println(" from alb : " + Integer.parseInt(al_id) + " : " + album_name);
		String img_name = file.getOriginalFilename();
		
		Album al = albumService.getById( Integer.parseInt(al_id) );
		if(al == null) return null;
		
		return photoService.saveOnDisk(al, file, file.getOriginalFilename());
	}
	
	@PostMapping(value="/addPhoto")
	public Photo add(@RequestParam("imageFile") MultipartFile file,
			@RequestParam("album_name") String album_name,
			@RequestParam("album_id") String al_id) throws IOException {
		
		System.out.println(" img : " + file.getOriginalFilename());
		System.out.println(" from alb : " + Integer.parseInt(al_id) + " : " + album_name);
		String img_name = file.getOriginalFilename();
		
		Album al = albumService.getById( Integer.parseInt(al_id) );
		if(al == null) return null;
		
		// add pic into album metadata
		Photo p = new Photo();
		p.setName(img_name);
		p.setAlbum( al );
		p = photoService.add( p );
		List<Photo> l = al.getPictures();
		l.add(p);
		al.setPictures( l );
		al.getPictures().forEach(System.out::println);
		albumService.update(al);
		
		return photoService.saveOnDisk(al, file, file.getOriginalFilename());
	}
	
	
	
	
	@GetMapping(value="/album/{pId}", produces="application/json")
	public Album getById(@PathVariable("pId") int id) {
		return albumService.getById(id);
	}
	
	@PutMapping(value="/album/update", produces="application/json")
	public Album update(@RequestBody Album album) {
		System.out.println("update album : " + album);
		return albumService.update(album);
	}
	
	@DeleteMapping(value="/album/delete/{pId}", produces="application/json")
	public void delete(@PathVariable("pId") int id) {
		albumService.delete( id );
	}
	
	@DeleteMapping(value="/photo/delete/{pId}", produces="application/json")
	public void deleteP(@PathVariable("pId") int id) {
		System.out.println("delete pic : " + id);
		photoService.delete( id );
	}
	
	@GetMapping(value="/synchronize")
	public void sync() {
		albumService.synchronize();
	}
}
