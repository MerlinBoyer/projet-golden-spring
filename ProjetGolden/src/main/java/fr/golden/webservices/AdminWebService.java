package fr.golden.webservices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping(value="/addPhoto")
	public Photo register(@RequestParam("album") Album album, @RequestParam("photo") Photo photo) {
		
		return photoService.add(album, photo);
	}

}
