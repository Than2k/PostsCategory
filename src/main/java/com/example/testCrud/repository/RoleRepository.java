package com.example.testCrud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.testCrud.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query(value = "select r.* from user as u join user_roles as ur on u.id = ur.user_id  "
			     + " join roles as r on r.id = ur.role_id"
			     + " where u.user_name = :userName",nativeQuery = true)
	public List<Role> listRoleByUser(@Param("userName") String userName);
} 
