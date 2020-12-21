package com.akshay.rest.webservices.userpost.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.akshay.rest.webservices.userpost.entity.User;
import com.akshay.rest.webservices.userpost.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public List<User> findAll() {
		return service.findAll();
	}
	
	@GetMapping("users/{id}")
	public User findById(@PathVariable int id) {
		return service.findById(id);
	}
	
	// return - 201 created status and the created URI
	@PostMapping("/users")
	public ResponseEntity<Object> create(@RequestBody User user) {
		User savedUser = service.save(user);
		URI location = ServletUriComponentsBuilder.
				fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/users/{id}")
	public User update(@PathVariable int id, @RequestBody User user) {
		return service.update(id, user);
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteById(@PathVariable int id) {
		service.deleteById(id);
	}
	
}
