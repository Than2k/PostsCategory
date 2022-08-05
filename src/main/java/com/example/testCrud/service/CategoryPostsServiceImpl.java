package com.example.testCrud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testCrud.model.Category;
import com.example.testCrud.model.CategoryPosts;
import com.example.testCrud.model.Posts;
import com.example.testCrud.repository.CategoryPostsRepository;


@Service
public class CategoryPostsServiceImpl implements ICategoryPostsService {

	@Autowired
	private CategoryPostsRepository CPRepository;
	
	@Override
	public Iterable<CategoryPosts> findAll() {
		return CPRepository.findAll();
	}

	@Override
	public void save(CategoryPosts data) {
		CPRepository.save(data);
	}

	@Override
	public void delete(int id) {
		CPRepository.deleteById(id);
	}

	@Override
	public Optional<CategoryPosts> findOne(int id) {
		return CPRepository.findById(id);
	}

	@Override
	public List<Category> getcategoyInCP() {
		return CPRepository.getCategoryInCategory_posts();
	}

	@Override
	public List<Posts> getposts(int id) {
		return CPRepository.getposts(id);
	}
	@Override
	public List<Integer> getListCategoryID(int id) {
		return CPRepository.getListCategoryID(id);
	}

	@Override
	public void Add(int category_id, int posts_id) {
		CPRepository.Add(category_id, posts_id);
	}
	
	@Override
	public void deleteCP(int posts_id, int category_id) {
		CPRepository.deleteCP(posts_id, category_id);
	}
	@Override
	public void deleteByPostsID(int posts_id) {
		CPRepository.deleteByPostsID(posts_id);
	}
}
