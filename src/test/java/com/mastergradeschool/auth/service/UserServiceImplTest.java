package com.mastergradeschool.auth.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mastergradeschool.auth.dao.UserDao;
import com.mastergradeschool.auth.model.User;

public class UserServiceImplTest {

	@Mock
	UserDao dao;
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Spy
	List<User> users = new ArrayList<User>();
	
	@BeforeClass
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		users = getUserList();
	}

	@Test
	public void findById(){
		User emp = users.get(0);
		when(dao.findById(anyInt())).thenReturn(emp);
		Assert.assertEquals(userService.findById(emp.getId()),emp);
	}

	@Test
	public void saveUser(){
		doNothing().when(dao).saveUser(any(User.class));
		userService.saveUser(any(User.class));
		verify(dao, atLeastOnce()).saveUser(any(User.class));
	}
	
	@Test
	public void updateUser(){
		User emp = users.get(0);
		when(dao.findById(anyInt())).thenReturn(emp);
		userService.updateUser(emp);
		verify(dao, atLeastOnce()).findById(anyInt());
	}

	@Test
	public void deleteUserBySsn(){
		doNothing().when(dao).deleteUserBySsn(anyString());
		userService.deleteUserBySsn(anyString());
		verify(dao, atLeastOnce()).deleteUserBySsn(anyString());
	}
	
	@Test
	public void findAllUsers(){
		when(dao.findAllUsers()).thenReturn(users);
		Assert.assertEquals(userService.findAllUsers(), users);
	}
	
	@Test
	public void findUserBySsn(){
		User emp = users.get(0);
		when(dao.findUserBySsn(anyString())).thenReturn(emp);
		Assert.assertEquals(userService.findUserBySsn(anyString()), emp);
	}

	@Test
	public void isUserSsnUnique(){
		User emp = users.get(0);
		when(dao.findUserBySsn(anyString())).thenReturn(emp);
		Assert.assertEquals(userService.isUserSsnUnique(emp.getId(), emp.getSsn()), true);
	}
	
	
	public List<User> getUserList(){
		User e1 = new User();
		e1.setId(1);
		e1.setName("Axel");
		e1.setJoiningDate(new LocalDate());
		e1.setSalary(new BigDecimal(10000));
		e1.setSsn("XXX111");
		
		User e2 = new User();
		e2.setId(2);
		e2.setName("Jeremy");
		e2.setJoiningDate(new LocalDate());
		e2.setSalary(new BigDecimal(20000));
		e2.setSsn("XXX222");
		
		users.add(e1);
		users.add(e2);
		return users;
	}
	
}
