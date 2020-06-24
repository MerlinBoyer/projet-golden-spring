package fr.golden.models;

import java.awt.Image;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@XmlRootElement
@Table(name = "pictures")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Photo {

	//Attributs
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pic")
	protected int id;
	
	@ManyToOne(cascade= { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH},fetch=FetchType.EAGER)
	private Album album;
	
	@OneToMany(mappedBy = "photo", cascade= { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH},fetch=FetchType.LAZY)
	private List<Comment> comments;
	
	private double size;
	private Date dateCreation;	
	private String name;
	private int nbDownloads;
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private MultipartFile image;
	
	
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNbDownloads() {
		return nbDownloads;
	}
	public void setNbDownloads(int nbDownloads) {
		this.nbDownloads = nbDownloads;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	@Override
	public String toString() {
		return "Photo [id=" + id + ", size=" + size + ", dateCreation=" + dateCreation + ", name=" + name
				+ ", nbDownloads=" + nbDownloads + ", image=" + image + "]";
	}
	
	
	
	
}
