package com.example.postscategory.service;

import java.util.List;

import com.example.postscategory.model.Role;

/**
 * Interface RoleService
 * @author ADMIN
 *
 */
public interface IRoleService {
	/**
	 * Trả về danh sách Role theo userName
	 * @param userName
	 * @return
	 */
	List<Role> listRoleByUser(String userName);
}
