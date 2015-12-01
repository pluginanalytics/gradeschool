package com.mastergradeschool.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mastergradeschool.auth.dao.UserDao;
import com.mastergradeschool.auth.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;
	
	public User findById(int id) {
		return dao.findById(id);
	}

	public void saveUser(User user) {
		dao.saveUser(user);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate update explicitly.
	 * Just fetch the entity from db and update it with proper values within transaction.
	 * It will be updated in db once transaction ends. 
	 */
	public void updateUser(User user) {
		User entity = dao.findById(user.getId());
		if(entity!=null){
			entity.setName(user.getName());
			entity.setJoiningDate(user.getJoiningDate());
			entity.setSalary(user.getSalary());
			entity.setSsn(user.getSsn());
		}
	}

	public void deleteUserBySsn(String ssn) {
		dao.deleteUserBySsn(ssn);
	}
	
	public List<User> findAllUsers() {
		return dao.findAllUsers();
	}

	public User findUserBySsn(String ssn) {
		return dao.findUserBySsn(ssn);
	}

	public boolean isUserSsnUnique(Integer id, String ssn) {
		User user = findUserBySsn(ssn);
		return ( user == null || ((id != null) && (user.getId() == id)));
	}
	
}
