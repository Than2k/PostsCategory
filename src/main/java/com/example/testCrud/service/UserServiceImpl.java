package com.example.testCrud.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testCrud.model.User;
import com.example.testCrud.repository.UserRepository;

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
