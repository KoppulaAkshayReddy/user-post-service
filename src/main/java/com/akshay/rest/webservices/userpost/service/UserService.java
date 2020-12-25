package com.akshay.rest.webservices.userpost.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akshay.rest.webservices.userpost.entity.User;
import com.akshay.rest.webservices.userpost.exception.BadRequestException;
import com.akshay.rest.webservices.userpost.exception.NotFoundException;
import com.akshay.rest.webservices.userpost.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findById(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent()) {
			throw new NotFoundException("User with id: " + id + " doesn't exist");
		}
		
		return user.get();
	}
	
	public User save(User newUser) {
		Optional<User> user = userRepository.findByEmail(newUser.getEmail());
		
		if(user.isPresent()) {
			throw new BadRequestException("User with email: " + newUser.getEmail() + " already exists");
		}
		
		userRepository.save(newUser);
		return newUser;
	}
	
	public void update(int id, User updateUser) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent()) {
			throw new NotFoundException("User with id: " + id + " doesn't exist");
		}
		
		user.get().setEmail(updateUser.getEmail());
		user.get().setFirstName(updateUser.getFirstName());
		user.get().setLastName(updateUser.getLastName());
		user.get().setPassword(updateUser.getPassword());
		user.get().setBirthDate(updateUser.getBirthDate());
		userRepository.save(user.get());
	}
	
	public void deleteById(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent()) {
			throw new NotFoundException("User with id: " + id + " doesn't exist");
		}
		
		userRepository.deleteById(id);
	}
 	
}
