package com.example.testCrud.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostsOutput {
	private String title;
	private String content;
	private String image;
	List<CategoryNameId> categories;

}
