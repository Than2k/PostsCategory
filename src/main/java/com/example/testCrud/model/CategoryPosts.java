package com.example.testCrud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category_posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryPosts {
	@Id	
	@Column(name = "posts_id")
	private int posts_id;
	@Column(name = "category_id")
	private int category_id;

}
