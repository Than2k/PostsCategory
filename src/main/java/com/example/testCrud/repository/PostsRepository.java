package com.example.testCrud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.testCrud.model.Posts;

@Repository
public interface PostsRepository extends CrudRepository<Posts, Integer> {
	
	@Query(value ="select * from posts where id = :id",nativeQuery = true)
	public Posts getPostdByID( @Param("id") int id );
	
	@Query(value = "SELECT  DISTINCT posts.id, posts.title,posts.content,posts.image,posts.created_at,posts.updated_at\r\n" + 
			"from posts JOIN categories\r\n" + 
			"WHERE ( :searchValues = '' OR  title LIKE :searchValues)\r\n" + 
			"      AND ( :cID = 0 or \r\n" + 
			"      posts.id  in (  SELECT posts.id \r\n" + 
			"                      FROM\r\n" + 
			"         	           posts JOIN category_posts on category_posts.posts_id = posts.id\r\n" + 
			"         	           WHERE category_posts.category_id = :cID))"+
			"       limit :page, :pageSize"+
			"       ", nativeQuery = true)
	public List<Posts> listPosts( @Param("cID") int cID, @Param("page") int index, 
								  @Param("pageSize") int pageSize, 
								  @Param("searchValues") String searchValues);
	
	@Query(value = "SELECT COUNT(DISTINCT(posts.id))\r\n" + 
			"from posts JOIN categories\r\n" + 
			"WHERE ( :searchValues = '' OR  title LIKE :searchValues)\r\n" + 
			"      AND ( :cID = 0 or \r\n" + 
			"      posts.id  in (  SELECT posts.id \r\n" + 
			"                      FROM\r\n" + 
			"         	           posts JOIN category_posts on category_posts.posts_id = posts.id\r\n" + 
			"         	           WHERE category_posts.category_id = :cID))"+
			"       ", nativeQuery = true)
	public int count( @Param("cID") int cID, @Param("searchValues") String searchValues);
	
}
