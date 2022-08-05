package com.example.testCrud.service;

import java.util.List;
import java.util.Optional;


import com.example.testCrud.model.Category;
import com.example.testCrud.model.CategoryPosts;
import com.example.testCrud.model.Posts;

/**
 * Interface CategoryService
 * @author ADMIN
 *
 */
public interface ICategoryPostsService  {
	/**
	 * Trả về all bảng category_posts
	 * @return
	 */
	Iterable<CategoryPosts> findAll();
	/**
	 * Thêm or sửa bảng category_posts
	 * @param data
	 */
    void save(CategoryPosts data);
    /**
     * Xóa category_posts by id
     * @param id
     */
    void delete(int id);
    /**
     * Trả về list category trong category_posts
     * @return List
     */
    List<Category> getcategoyInCP();
    /**
     * Trả về List bài viết theo postsID trong category_posts
     * @param id
     * @return List
     */
    List<Posts>  getposts(int id);
    /**
     * Trả về list categoryId trong category_posts
     * @param id
     * @return
     */
    List<Integer> getListCategoryID(int id);
    /**
     * Thêm mới vào bảng category_posts
     * @param category_id
     * @param posts_id
     */
    void Add(int category_id,int posts_id);
    /**
     * Xóa category_posts theo (category_id, posts_id)
     * @param category_id
     * @param posts_id
     */
    void deleteCP(int  category_id,int posts_id );
    /**
     * Xóa tất cả bảng postsId có trong bảng category_posts
     * @param posts_id
     */
    void deleteByPostsID(int posts_id);
    /**
     * 
     * @param id
     * @return
     */
    Optional<CategoryPosts> findOne(int id);
}
