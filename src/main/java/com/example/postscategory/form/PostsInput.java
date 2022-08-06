package com.example.postscategory.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostsInput {
	private int id;
	private String title;
	private String content;
	private MultipartFile image;
	private List<Integer> categoryIDs;

}
