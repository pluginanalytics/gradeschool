package com.mastergradeschool.auth.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import static org.mockito.Mockito.atLeastOnce;

import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.mastergradeschool.auth.model.User;
import com.mastergradeschool.auth.service.UserService;

public class AppControllerTest {

	@Mock
	UserService service;
	
	@Mock
	MessageSource message;
	
	@InjectMocks
	AppController appController;
	
	@Spy
	List<User> users = new ArrayList<User>();

	@Spy
	ModelMap model;
	
	@Mock
	BindingResult result;
	
	@BeforeClass
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		users = getUserList();
	}
	
	@Test
	public void listUsers(){
		when(service.findAllUsers()).thenReturn(users);
		Assert.assertEquals(appController.listUsers(model), "allusers");
		Assert.assertEquals(model.get("users"), users);
		verify(service, atLeastOnce()).findAllUsers();
	}
	
	@Test
	public void newUser(){
		Assert.assertEquals(appController.newUser(model), "registration");
		Assert.assertNotNull(model.get("user"));
		Assert.assertFalse((Boolean)model.get("edit"));
		Assert.assertEquals(((User)model.get("user")).getId(), 0);
	}


	@Test
	public void saveUserWithValidationError(){
		when(result.hasErrors()).thenReturn(true);
		doNothing().when(service).saveUser(any(User.class));
		Assert.assertEquals(appController.saveUser(users.get(0), result, model), "registration");
	}

	@Test
	public void saveUserWithValidationErrorNonUniqueSSN(){
		when(result.hasErrors()).thenReturn(false);
		when(service.isUserSsnUnique(anyInt(), anyString())).thenReturn(false);
		Assert.assertEquals(appController.saveUser(users.get(0), result, model), "registration");
	}

	
	@Test
	public void saveUserWithSuccess(){
		when(result.hasErrors()).thenReturn(false);
		when(service.isUserSsnUnique(anyInt(), anyString())).thenReturn(true);
		doNothing().when(service).saveUser(any(User.class));
		Assert.assertEquals(appController.saveUser(users.get(0), result, model), "success");
		Assert.assertEquals(model.get("success"), "User Axel registered successfully");
	}

	@Test
	public void editUser(){
		User emp = users.get(0);
		when(service.findUserBySsn(anyString())).thenReturn(emp);
		Assert.assertEquals(appController.editUser(anyString(), model), "registration");
		Assert.assertNotNull(model.get("user"));
		Assert.assertTrue((Boolean)model.get("edit"));
		Assert.assertEquals(((User)model.get("user")).getId(), 1);
	}

	@Test
	public void updateUserWithValidationError(){
		when(result.hasErrors()).thenReturn(true);
		doNothing().when(service).updateUser(any(User.class));
		Assert.assertEquals(appController.updateUser(users.get(0), result, model,""), "registration");
	}

	@Test
	public void updateUserWithValidationErrorNonUniqueSSN(){
		when(result.hasErrors()).thenReturn(false);
		when(service.isUserSsnUnique(anyInt(), anyString())).thenReturn(false);
		Assert.assertEquals(appController.updateUser(users.get(0), result, model,""), "registration");
	}

	@Test
	public void updateUserWithSuccess(){
		when(result.hasErrors()).thenReturn(false);
		when(service.isUserSsnUnique(anyInt(), anyString())).thenReturn(true);
		doNothing().when(service).updateUser(any(User.class));
		Assert.assertEquals(appController.updateUser(users.get(0), result, model, ""), "success");
		Assert.assertEquals(model.get("success"), "User Axel updated successfully");
	}
	
	
	@Test
	public void deleteUser(){
		doNothing().when(service).deleteUserBySsn(anyString());
		Assert.assertEquals(appController.deleteUser("123"), "redirect:/list");
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
