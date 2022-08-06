package com.example.postscategory.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;


import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//mark class as an Entity   
@Entity

//defining class name as Table name 
@Table (name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	
	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	//@Temporal(TemporalType.DATE)
	private Date createdAt;
	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Date updatedAt;
	
//	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "Categories_Posts",
//			joinColumns  = { @JoinColumn(name = "categoryID") },//joincolums đại diện cho cái key có trong class dang trỏ
//            inverseJoinColumns = { @JoinColumn(name = "postsID") })//inverseJoinColums đại diện cho cái key không nằm trong class đó
//	
//	private  Set<posts> Posts;
	
}
