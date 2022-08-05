package com.example.testCrud.service;

import com.example.testCrud.model.User;

/**
 * Interface UserService
 * @author ADMIN
 *
 */
public interface IUserService {
	
	/**
	 * Trả về user theo userName
	 * @param userName
	 * @return User
	 */
	User findByUserName(String userName);
	/**
	 * Thêm or sửa user
	 * @param user
	 */
	void save(User user);
}
