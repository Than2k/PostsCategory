package com.example.postscategory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.postscategory.model.Role;
import com.example.postscategory.repository.RoleRepository;
@Service
public class RoleServiceImpl implements IRoleService{
	
	@Autowired
	private RoleRepository roleRepostory;
	@Override
	public List<Role> listRoleByUser(String userName) {
		return roleRepostory.listRoleByUser(userName);
	}
}
