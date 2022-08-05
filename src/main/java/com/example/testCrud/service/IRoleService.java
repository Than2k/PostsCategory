package com.example.testCrud.service;

import java.util.List;

import com.example.testCrud.model.Role;

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
