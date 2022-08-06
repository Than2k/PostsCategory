package com.example.postscategory.service;

import java.util.Optional;

import com.example.postscategory.model.Category;

/**
 * Interface CategoryService
 * @author ADMIN
 *
 */
public interface ICategoryService {
	
	/**
	 * Trả về all thể loại
	 * @return Iterable
	 */
	Iterable<Category> findAll();
	/**
	 * Thêm hoặc sửa thể loại
	 * @param data
	 */
    void save(Category data);
    /**
     * Xóa thể loại theo Id
     * @param id
     */
    void delete(int id);
    /**
     * trả về thể tên thể loại theo id
     * @param id
     * @return String
     */
    
    String getNameByID(int id);
    /**
     * Trả về category theo id
     * @param id
     * @return Category
     */
    Category getCategoryById(int id);
    /**
     * 
     * @param id
     * @return
     */
    Optional<Category> findOne(int id);
}
