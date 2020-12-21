package com.akshay.rest.webservices.userpost.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.akshay.rest.webservices.userpost.entity.User;
import com.akshay.rest.webservices.userpost.exception.BadRequestException;
import com.akshay.rest.webservices.userpost.exception.NotFoundException;

@Service
public class UserService {

	private static List<User> users = new ArrayList<>();
	
	static {
		users.add(new User(1, "AKshay", "Reddy", "rakshay@gmail.com", new Date()));
		users.add(new User(2, "AKshay", "Reddy", "rakshay@gmail.com", new Date()));
		users.add(new User(3, "AKshay", "Reddy", "rakshay@gmail.com", new Date()));
		users.add(new User(4, "AKshay", "Reddy", "rakshay@gmail.com", new Date()));
	}
	
	private static int autoId = 5;
	
	
	public List<User> findAll() {
		return users;
	}
	
	public User findById(int id) {
		for(User user : users) {
			if(user.getId() == id) {
				return user;
			}
		}
		
		throw new NotFoundException("User with id: " + id + " doesn't exist");
	}
	
	public User save(User newUser) {
		for(User user : users) {
			if(user.getEmail().equals(newUser.getEmail())) {
				throw new BadRequestException("User with email: " + newUser.getEmail() + " already exists");
			}
		}
		newUser.setId(autoId++);
		users.add(newUser);
		return newUser;
	}
	
	public User update(int id, User updatedUser) {
		for(User user : users) {
			if(user.getId() == id) {
				user.setFirstName(updatedUser.getFirstName());
				user.setLastName(updatedUser.getLastName());
				user.setEmail(updatedUser.getEmail());
				user.setBirthDate(updatedUser.getBirthDate());
				return user;
			}
		}
		
		throw new NotFoundException("User with id: " + id + " doesn't exist");
	}
	
	public void deleteById(int id) {
		User deleteUser = null;
		for(User user : users) {
			if(user.getId() == id) {
				deleteUser = user;
			}
		}
		
		if(deleteUser == null) {
			throw new NotFoundException("User with id: " + id + " doesn't exist");
		}
	    users.remove(deleteUser);
	}
 	
}
