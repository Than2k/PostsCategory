package com.example.testCrud.service;

import java.util.Optional;

import com.example.testCrud.model.Category;

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
     * 
     * @param id
     * @return
     */
    Optional<Category> findOne(int id);
}
