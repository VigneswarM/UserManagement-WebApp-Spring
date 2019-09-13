package test.com.h2rd.refactoring.integration;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Test;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.web.UserResource;

public class UserIntegrationTest {
	
	@Test
	public void createUserTest() {
		UserResource userResource = new UserResource();
		
		User integration = new User();
        integration.setName("integration");
        integration.setEmail("initia@integration.com");
        integration.setRoles(Arrays.asList("admin", "master"));
        //integration.setRoles(new ArrayList<String>());
        
        Response response = userResource.addUser(integration.getName(), integration.getEmail(), integration.getRoles());
        Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void updateUserTest() {
		UserResource userResource = new UserResource();
		
		createUserTest();
		
        User updated = new User();
        updated.setName("newNAme");
        updated.setEmail("initial@integration.com");
        updated.setRoles(Arrays.asList("admin", "master"));
        //updated.setRoles(new ArrayList<String>());
        
        
        Response response = userResource.updateUser(updated.getName(), updated.getEmail(), updated.getRoles());
        Assert.assertEquals(200, response.getStatus());
	}
}
