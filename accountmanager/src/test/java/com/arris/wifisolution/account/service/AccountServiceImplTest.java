/*package com.arris.wifisolution.account.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arris.wifisolution.account.AccountService;
import com.arris.wifisolution.account.Role;
import com.arris.wifisolution.account.UserInfo;
import com.arris.wifisolution.account.model.User;
import com.arris.wifisolution.dao.UserRepository;
import com.arris.wifisoluton.AccountManagerTestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountManagerTestApplication.class)
public class AccountServiceImplTest {
	
	@Autowired
	AccountService service;
	
	
	@Autowired
	UserRepository userRepo;
	
	@Test
	public void testInstance(){
		assertNotNull(service);
	}
	
	@Test
	public void testAddUser(){
		UserInfo user = new UserInfo("bindu_p22@yahoo.com","Venkata", "harikishan", "password", "Arris", "software Engineer", "Home", "9901973157",Role.MGR);
		service.addUser(user);		
		List<User> userList = userRepo.findAll();
		assertNotNull(userList);
		User user1 = userRepo.findByIdAndPassword("bindu_p22@yahoo.com", "password");
		assertNotNull(user1);
		assertEquals(user1.getCompany(),user.getCompany());
		assertEquals(user1.getId(),"bindu_p22@yahoo.com");
	}
	
	@Test
	public void testDuplicateUser(){
		
		UserInfo user = new UserInfo("bindu_p23@yahoo.com","Venkata", "harikishan", "password", "Arris", "software Engineer", "Home", "9901973157",Role.MGR);
		service.addUser(user);	
		UserInfo user2 = new UserInfo("bindu_p23@yahoo.com","Venkata2", "harikishan2", "password", "Arris", "software Engineer", "Home", "9901973157",Role.MGR);
		service.addUser(user2);		
		User user3 = userRepo.findById("bindu_p23@yahoo.com");		
		assertNotNull(user3);
		assertEquals(user3.getFirstName(),"Venkata2");		
	}
	
	@Test
	public void testGetUser(){	
		UserInfo userInfo = new UserInfo("pvharikishan@gmail.com", "Venkata", "harikishan", "password2", "motorola", "software Engineer", "Home", "9901973157",Role.MGR);
		User user = new User(userInfo);
		userRepo.save(user);		
		UserInfo user1 = service.getUser("pvharikishan@gmail.com", "password2");
		assertNotNull(user1);
		assertEquals(user1.getCompany(),"motorola");		
	}
	
	
	
	
	
}
*/