package com.h2rd.refactoring.usermanagement;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class UserDao {

	private static ArrayList<User> users;

	public static volatile UserDao userDao;

	// Singleton class
	private UserDao() {

		// Prevent form the reflection api.
		if (userDao != null) {
			throw new RuntimeException("Use getUserDao() method to get the single instance of this class.");
		}
	}

	public static UserDao getUserDao() {

		// Double check locking pattern
		if (userDao == null) { // Check for the first time
			synchronized (UserDao.class) {
				// Check for the second time. If there is no instance available... create new
				// one
				if (userDao == null)
					userDao = new UserDao();
			}
		}
		return userDao;
	}

	public void saveUser(User user) {
		if (users == null) {
			users = new ArrayList<User>();
		}
		users.add(user);
		System.out.println(users);
	}

	public ArrayList<User> getUsers() {
		try {
			return users;
		} catch (Throwable e) {
			System.out.println("error");
			return null;
		}
	}

	public boolean deleteUser(User userToDelete) {
		boolean flag = false;
		try {
			for (User user : users) {
				if (user.getEmail().equals(userToDelete.getEmail())) {
					users.remove(user);
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean updateUser(User userToUpdate) {
		boolean flag = false;
		try {
			for (User user : users) {
				if (user.getEmail().equals(userToUpdate.getEmail())) {
					user.setName(userToUpdate.getName());
					user.setRoles(userToUpdate.getRoles());
					flag = true;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		return flag;

	}

	public User findUser(String name) {
		System.out.println(name);
		try {
			for (User user : users) {
				if (user.getEmail().equals(name)) {
					return user;
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean checkEmail(String email) {
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
}
