package com.example.postscategory.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.postscategory.model.User;
import com.example.postscategory.repository.UserRepository;
import com.example.postscategory.service.IUserService;

@Service
public class UserServiceImpl  implements IUserService{

	@Autowired
	private UserRepository URepository;

	@Override
	public User findByUserName(String userName) {
		return URepository.findByUserName(userName);
	}
	@Override
	public void save(User user) {
		URepository.save(user);
		
	}

}
