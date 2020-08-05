package fr.golden.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.golden.models.Photo;
import fr.golden.services.IPhotoService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/public/photos")
public class PhotoWebService {

	
	@Autowired
	private IPhotoService photoService;
	
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
	
	@GetMapping(value="/{pId}/{pCode}", produces="application/json")
	public Photo getById(@PathVariable("pId") int id, @PathVariable(value = "pCode", required = false) String code ) {
		
		return photoService.getById(id);
	}
	
	@CrossOrigin
	@GetMapping(value="/getImg/{pId}/{pCode}", produces="image/jpg")
	public void getImageById(@PathVariable("pId") int id, @PathVariable(value = "pCode", required = false) String code, HttpServletResponse response) {
		Photo p = photoService.getById(id);
		if(p == null)	return;
		
		InputStream is = photoService.getFromDisk(id);
		 try {
		      // get your file as InputStream
		      // copy it to response's OutputStream
		      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
		      response.flushBuffer();
		    } catch (IOException ex) {
		      System.out.println("ERROR CATCH : cant write pic in buffer [PhotoWebService.getImageById()]");
		    }
	}
	
	/*
	@GetMapping(value="/getImg/{pId}")
	public @ResponseBody byte[] getImageById(@PathVariable("pId") int id, HttpServletResponse response) throws IOException {
		Photo p = photoService.getById(id);
		if(p == null)	return null;
		
		return photoService.getFromDisk(id);
		
	}*/
}
