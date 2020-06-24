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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@XmlRootElement
@Table(name = "comments")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Comment {
	
	//Attributs
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id_com")
		protected int id;
		
		@ManyToOne(cascade= { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH},fetch=FetchType.EAGER)
		private Photo photo;
		
		private String author;
		private String content;

		
		
		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Photo getPhoto() {
			return photo;
		}

		public void setPhoto(Photo photo) {
			this.photo = photo;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
		
		

}
