package com.h2rd.refactoring.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserDao;

@Path("/users")
@Repository
//@Controller
public class UserResource {

	@Autowired
	private UserDao userDao;

	private static int flag = 0;

	@POST
	@Path("add/")
	public Response addUser(@QueryParam("name") String name, @QueryParam("email") String email,
			@QueryParam("role") List<String> roles) {

		if (roles.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Bad Request - A user should have at least 1 role").build();
		} else {

			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setRoles(roles);

			if (userDao == null) {
				userDao = UserDao.getUserDao();
			}

			if (flag == 0) {
				userDao.saveUser(user);
				flag++;
				return Response.ok().entity("New User Added Successfully").build();
			} else if (userDao.checkEmail(email)) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Bad Request - User Email: " + email + " already Exists").build();
			} else {
				userDao.saveUser(user);
				flag++;
				return Response.status(Response.Status.OK).entity("New User " + email + " Added Successfully").build();
			}
		}

	}

	@PUT
	@Path("update/")
	public Response updateUser(@QueryParam("name") String name, @QueryParam("email") String email,
			@QueryParam("role") List<String> roles) {

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setRoles(roles);

		if (userDao == null) {
			userDao = UserDao.getUserDao();
		}

		if (roles.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Bad Request - A user should have at least 1 role").build();
		}

		else {
			if (userDao.updateUser(user))
				return Response.status(Response.Status.OK).entity("User Updated Successfully").build();
			else
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Bad Request - User email : " + email + " does not Exists").build();
		}
	}

	@DELETE
	@Path("delete/")
	public Response deleteUser(@QueryParam("email") String email) {
		User user = new User();
		user.setEmail(email);

		if (userDao == null) {
			userDao = UserDao.getUserDao();
		}

		if (userDao.deleteUser(user))
			return Response.ok().entity("User +" + email + " deleted successfully").build();
		else
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Bad Request - User email : " + email + " does not Exists").build();

	}

	@GET
	@Path("find/")
	public Response getUsers() {

		// Get Singleton instance
		userDao = UserDao.getUserDao();

		List<User> users = userDao.getUsers();
		if (users == null) {
			users = new ArrayList<User>();
		}

		GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {
		};
		return Response.status(200).entity(usersEntity).build();
	}

	@GET
	@Path("search/")
	public Response findUser(@QueryParam("email") String email) {

		if (userDao == null) {
			userDao = UserDao.getUserDao();
		}

		User user = userDao.findUser(email);
		if (user == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Bad Request - User : " + email + " does not Exists").build();
		else
			return Response.ok().entity(user).build();
	}
}
