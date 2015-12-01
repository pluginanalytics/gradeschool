package com.mastergradeschool.auth.dao;

import java.math.BigDecimal;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mastergradeschool.auth.model.User;


public class UserDaoImplTest extends EntityDaoImplTest{

	@Autowired
	UserDao userDao;

	@Override
	protected IDataSet getDataSet() throws Exception{
		IDataSet dataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("User.xml"));
		return dataSet;
	}
	
	/* In case you need multiple datasets (mapping different tables) and you do prefer to keep them in separate XML's
	@Override
	protected IDataSet getDataSet() throws Exception {
	  IDataSet[] datasets = new IDataSet[] {
			  new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("User.xml")),
			  new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("Benefits.xml")),
			  new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("Departements.xml"))
	  };
	  return new CompositeDataSet(datasets);
	}
	*/

	@Test
	public void findById(){
		Assert.assertNotNull(userDao.findById(1));
		Assert.assertNull(userDao.findById(3));
	}

	
	@Test
	public void saveUser(){
		userDao.saveUser(getSampleUser());
		Assert.assertEquals(userDao.findAllUsers().size(), 3);
	}
	
	@Test
	public void deleteUserBySsn(){
		userDao.deleteUserBySsn("11111");
		Assert.assertEquals(userDao.findAllUsers().size(), 1);
	}
	
	@Test
	public void deleteUserByInvalidSsn(){
		userDao.deleteUserBySsn("23423");
		Assert.assertEquals(userDao.findAllUsers().size(), 2);
	}

	@Test
	public void findAllUsers(){
		Assert.assertEquals(userDao.findAllUsers().size(), 2);
	}
	
	@Test
	public void findUserBySsn(){
		Assert.assertNotNull(userDao.findUserBySsn("11111"));
		Assert.assertNull(userDao.findUserBySsn("14545"));
	}

	public User getSampleUser(){
		User user = new User();
		user.setName("Karen");
		user.setSsn("12345");
		user.setSalary(new BigDecimal(10980));
		user.setJoiningDate(new LocalDate());
		return user;
	}

}
