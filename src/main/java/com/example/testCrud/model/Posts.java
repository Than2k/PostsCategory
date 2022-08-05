package com.example.testCrud.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Posts {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String content;
	private String image;
	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Date createdAt;
	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Date updatedAt;
//	
//	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "Categories_Posts",
//	joinColumns  = { @JoinColumn(name = "postsID") },//joincolums đại diện cho cái key có trong class dang trỏ
//    inverseJoinColumns = { @JoinColumn(name = "categoryID") })//inverseJoinColums đại diện cho cái key không nằm trong class đó
//	
//	private  Set<category> Category;


		
}
