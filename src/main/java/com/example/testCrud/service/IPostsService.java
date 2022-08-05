package com.example.testCrud.service;

import java.util.List;
import java.util.Optional;
import com.example.testCrud.model.Posts;

/**
 * Interface Posts
 * @author ADMIN EDITOR
 *
 */
public interface IPostsService {
   
	/**
	 * Trả về tất cả bài viết
	 * @return
	 */
	Iterable<Posts> findAll();
	/**
	 * Thêm hoắc sửa bài viết
	 * @param data
	 */
    void save(Posts data);
    /**
     * Xóa bài viết theo postsId
     * @param id
     */
    void delete(int id);
    /**
     * lấy bài biết bởi id
     * @param id
     * @return
     */
    Posts getPostdByID(int id);
    /**
     * Trả về danh sách bài viết tìm kiếm theo (category_id, page, pageSize, searchValues)
     * 
     * @param category_id
     * @param page
     * @param pageSize
     * @param searchValues
     * @return
     */
    List<Posts> listPosts(int category_id,int page, int pageSize, String searchValues);
    /**
     * 
     * @param id
     * @return
     */
    Optional<Posts> findOne(int id);
    /**
     * Đếm số lượng bài viết tìm kiếm theo (categoryid, searchValues)
     * @param categoryid
     * @param searchValues
     * @return
     */
    int count(int categoryId, String searchValues);
}
