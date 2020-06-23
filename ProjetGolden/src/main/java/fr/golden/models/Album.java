package fr.golden.models;


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
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@XmlRootElement
@Table(name = "albums")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Album {

	//Attributs
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id_alb")
		protected int id;
		
		@OneToMany(mappedBy = "album", cascade= { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH},fetch=FetchType.LAZY)
		private List<Photo> pictures;
		
		private String name;
		private String path;
		private int visibility;
		private String code;
		private String description;
		private int nb_visits;
		private Date creation_date;
		
		
		
		
		
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public List<Photo> getPictures() {
			return pictures;
		}
		public void setPictures(List<Photo> pictures) {
			this.pictures = pictures;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getVisibility() {
			return visibility;
		}
		public void setVisibility(int visibility) {
			this.visibility = visibility;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public int getNb_visits() {
			return nb_visits;
		}
		public void setNb_visits(int nb_visits) {
			this.nb_visits = nb_visits;
		}
		public Date getCreation_date() {
			return creation_date;
		}
		public void setCreation_date(Date creation_date) {
			this.creation_date = creation_date;
		}
		@Override
		public String toString() {
			return "Album [id=" + id + ", name=" + name + ", path=" + path + ", visibility=" + visibility + ", code="
					+ code + ", description=" + description + ", nb_visits=" + nb_visits + ", creation_date="
					+ creation_date + "]";
		}
		
		
		
}
