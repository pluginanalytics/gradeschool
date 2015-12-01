package com.mastergradeschool.auth.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.mastergradeschool.auth.model.User;
import com.mastergradeschool.auth.service.UserService;

@Controller
@RequestMapping("/")
public class AppController {

	@Autowired
	UserService service;
	
	@Autowired
	MessageSource messageSource;

	/*
	 * This method will list all existing users.
	 */
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {
		System.out.println("Got here");
		List<User> users = service.findAllUsers();
		model.addAttribute("users", users);
		return "allusers";
	}


	/*
	 * Testing CORS with Client Site Angular Template.
	 */
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public String loginUser (@RequestParam("username") String username, @RequestParam("password") String password,ModelMap model) {
 		System.out.println("IN-LOGIN *******"+ username + password);
		List<User> users = service.findAllUsers();
		model.addAttribute("users", users);
		return "allusers";
	}





	/*
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/new" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		return "registration";
	}

	/*
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/new" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * Preferred way to achieve uniqueness of field [ssn] should be implementing custom @Unique annotation 
		 * and applying it on field [ssn] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		if(!service.isUserSsnUnique(user.getId(), user.getSsn())){
			FieldError ssnError =new FieldError("user","ssn",messageSource.getMessage("non.unique.ssn", new String[]{user.getSsn()}, Locale.getDefault()));
		    result.addError(ssnError);
			return "registration";
		}
		
		service.saveUser(user);

		model.addAttribute("success", "User " + user.getName() + " registered successfully");
		return "success";
	}


	/*
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-{ssn}-user" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssn, ModelMap model) {
		User user = service.findUserBySsn(ssn);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		return "registration";
	}
	
	/*
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-{ssn}-user" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String ssn) {

		if (result.hasErrors()) {
			return "registration";
		}

		if(!service.isUserSsnUnique(user.getId(), user.getSsn())){
			FieldError ssnError =new FieldError("user","ssn",messageSource.getMessage("non.unique.ssn", new String[]{user.getSsn()}, Locale.getDefault()));
		    result.addError(ssnError);
			return "registration";
		}

		service.updateUser(user);

		model.addAttribute("success", "User " + user.getName()	+ " updated successfully");
		return "success";
	}

	
	/*
	 * This method will delete an user by it's SSN value.
	 */
	@RequestMapping(value = { "/delete-{ssn}-user" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssn) {
		service.deleteUserBySsn(ssn);
		return "redirect:/list";
	}

}
