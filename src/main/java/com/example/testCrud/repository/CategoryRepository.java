package com.example.testCrud.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.testCrud.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>{
	
	@Query(value = "select c.name from categories c where id=:id",nativeQuery = true)
	public String getNameByID(@Param("id") int id);
}
