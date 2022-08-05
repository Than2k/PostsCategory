package com.example.testCrud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.testCrud.model.Category;
import com.example.testCrud.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired //dependency enjection
	private CategoryRepository categoryRepository ;
	
	@Override
	public Iterable<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public void save(Category data) {
		categoryRepository.save(data);
	}

	@Override
	public void delete(int id) {
		categoryRepository.deleteById(id);;
	}

	@Override
	public Optional<Category> findOne(int id) {
		return categoryRepository.findById(id);
	}
	@Override
	public String getNameByID(int id) {
		return categoryRepository.getNameByID(id);
	}
}
