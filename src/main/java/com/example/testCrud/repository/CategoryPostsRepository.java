package com.example.testCrud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.testCrud.model.Category;
import com.example.testCrud.model.CategoryPosts;
import com.example.testCrud.model.Posts;

@Repository
public interface CategoryPostsRepository extends JpaRepository<CategoryPosts, Integer> {

	@Query(value = "SELECT c.* \r\n" + 
			"from posts p JOIN category_posts cp on p.id = cp.posts_id \r\n" + 
			"JOIN categories c on c.id = cp.category_id \r\n" + 
			"WHERE p.id = 22",nativeQuery = true)//naviteQuery
	public List<Category> getCategoryInCategory_posts();
	@Query(value = "select p from posts p where id=:id",nativeQuery = true)//query
	public List<Posts> getposts(int id);
	
	@Transactional//đây là là một cái giao dịch
	@Modifying//định nghĩa giao dịch này có thê update/delete/insert
	@Query(value = "insert into category_posts(posts_id,category_id) values(:pID,:cID)",nativeQuery = true)//query
	public void Add(@Param("cID") int cID, @Param("pID") int pID);
	
	@Query(value = "select c.category_id from category_posts c where posts_id = :id",nativeQuery = true)
	public List<Integer> getListCategoryID(@Param("id") int id);
	
	@Transactional
	@Modifying
	@Query(value = "delete from category_posts where posts_id = :pID and category_id = :cID",nativeQuery = true)
	public void deleteCP(@Param("pID") int posts_id, @Param("cID") int cattegory_id);
	
	@Transactional
	@Modifying
	@Query(value = "delete from category_posts where posts_id = :id", nativeQuery = true)
	public void deleteByPostsID(@Param("id") int posts_id);
}
