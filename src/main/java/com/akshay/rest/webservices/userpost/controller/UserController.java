package com.akshay.rest.webservices.userpost.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/users")
	public List<User> findAll() {
		return service.findAll();
	}
	
	@GetMapping("users/{id}")
	public EntityModel<User> findById(@PathVariable int id) {
		User user = service.findById(id);
		
		// HATEOS (hypermedia as the engine of applications)
		// To add additional useful links to the User resource
		EntityModel<User> resource = EntityModel.of(user);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAll());
		resource.add(linkTo.withRel("all-users"));
		return resource;
	}
	
	// return - 201 created status and the created URI
	@PostMapping("/users")
	public ResponseEntity<Object> create(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		URI location = ServletUriComponentsBuilder.
				fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/users/{id}")
	public User update(@PathVariable int id, @Valid @RequestBody User user) {
		return service.update(id, user);
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteById(@PathVariable int id) {
		service.deleteById(id);
	}
	
	// Internationalization
	@GetMapping("hello-i18n")
	public String sayHello() {
		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}
	
}
