package com.example.testCrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.testCrud.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{
	@Query(value = "select * from user where user_name = :userName",nativeQuery = true)
	public User findByUserName(@Param("userName") String userName);
}
