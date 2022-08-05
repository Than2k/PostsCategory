package com.example.testCrud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testCrud.model.Posts;
import com.example.testCrud.repository.PostsRepository;

@Service
public class PostsServiceImpl  implements IPostsService{

	@Autowired
	private PostsRepository postsRepository;
	
	@Override
	public Iterable<Posts> findAll() {
		return postsRepository.findAll();
	}

	@Override
	public void save(Posts data) {
		postsRepository.save(data);
	}

	@Override
	public void delete(int id) {
		postsRepository.deleteById(id);;
	}

	@Override
	public Optional<Posts> findOne(int id) {
		return postsRepository.findById(id);
	}

	@Override
	public Posts getPostdByID(int id) {
		return postsRepository.getPostdByID(id);
	}
	@Override
	public List<Posts> listPosts(int category_id,int page, int pageSize,String searchValues) {
		if(searchValues != "")
			searchValues = "%" +searchValues +"%";
		return postsRepository.listPosts(category_id,((page-1)*pageSize), pageSize, searchValues);
	}
	@Override
	public int count(int category_id, String searchValues) {
		if(searchValues != "")
			searchValues = "%" +searchValues +"%";
		return postsRepository.count(category_id, searchValues);
	}
}
