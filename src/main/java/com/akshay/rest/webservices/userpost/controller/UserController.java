package com.akshay.rest.webservices.userpost.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.akshay.rest.webservices.userpost.entity.Post;
import com.akshay.rest.webservices.userpost.entity.User;
import com.akshay.rest.webservices.userpost.entity.UserVersion2;
import com.akshay.rest.webservices.userpost.exception.BadRequestException;
import com.akshay.rest.webservices.userpost.exception.NotFoundException;
import com.akshay.rest.webservices.userpost.service.PostService;
import com.akshay.rest.webservices.userpost.service.UserService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/users")
	public MappingJacksonValue findAll() {
		List<User> users = userService.findAll();
		MappingJacksonValue mapping = new MappingJacksonValue(users);
		mapping.setFilters(getUserBeanFilters());
		return mapping;
	}
	
	@GetMapping("users/{id}")
	public MappingJacksonValue findById(@PathVariable int id) {
		User user = userService.findById(id);
		
		// HATEOS (hypermedia as the engine of applications)
		// To add additional useful links to the User resource
		EntityModel<User> resource = EntityModel.of(user);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAll());
		resource.add(linkTo.withRel("all-users"));
		
		MappingJacksonValue mapping = new MappingJacksonValue(resource);
		mapping.setFilters(getUserBeanFilters());
		return mapping;
	}
	
	// return - 201 created status and the created URI
	@PostMapping("/users")
	public ResponseEntity<Object> create(@Valid @RequestBody User user) {
		userService.save(user);
		URI location = ServletUriComponentsBuilder.
				fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/users/{id}")
	public void update(@PathVariable int id, @Valid @RequestBody User user) {
		userService.update(id, user);
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteById(@PathVariable int id) {
		userService.deleteById(id);
	}
	
	@GetMapping("/users/{id}/posts")
	public List<Post> findUserPosts(@PathVariable int id) {
		User user = userService.findById(id);
		return user.getPosts();
	}
	
	@GetMapping("/users/{id}/posts/{post-id}")
	public Post findUserPostByPostId(@PathVariable int id, @PathVariable(name = "post-id") int postId) {
		User user = userService.findById(id);
		Post post = postService.findById(postId);
		for(Post p : user.getPosts()) {
			if(p.getId() == postId) {
				return post;
			}
		}
		throw new NotFoundException("The post (post id: " + postId + ") for user (user id: " + id + ") doesn't exist");
	}
	
	@PostMapping("/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
		User user = userService.findById(id);
		post.setUser(user);
		postService.save(post);
		URI location = ServletUriComponentsBuilder.
				fromCurrentRequest()
				.path("/{post-id}")
				.buildAndExpand(post.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/users/{id}/posts/{post-id}")
	public void update(@PathVariable int id, @PathVariable(name = "post-id") int postId, @RequestBody Post newPost) {
		User user = userService.findById(id);
		Post post = postService.findById(postId);
		for(Post p : user.getPosts()) {
			if(p.getId() == postId) {
				postService.update(id, post, newPost);
				return;
			}
		}
		throw new BadRequestException("User (user id: " + id + ") cannot update post (post id: " + postId + ")");
	}
	
	@DeleteMapping("/users/{id}/posts/{post-id}")
	public void deleteById(@PathVariable int id, @PathVariable(name = "post-id") int postId) {
		User user = userService.findById(id);
		postService.findById(postId);
		for(Post p : user.getPosts()) {
			if(p.getId() == postId) {
				postService.deleteById(postId);
				return;
			}
		}
		throw new BadRequestException("User (user id: " + id + ") cannot delete post (post id: " + postId + ")");
	}
	
	// Internationalization
	@GetMapping("hello-i18n")
	public String sayHello() {
		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}
	
	//Implement dynamic filtering for RESTful service
	private FilterProvider getUserBeanFilters() {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName", "email", "birthDate");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserBeanFilter", filter);
		return filters;
	}
	
	/* #### Test versioning of RESTful service #### */
	
	
	//URI versioning (Adv: easy caching, API documentation and execute on browser)
	@GetMapping("v2/users")
	public UserVersion2 findAllV2() {
		return new UserVersion2(1, "Akshay Reddy", "akshayreddy@gmail.com", "pwd", new Date());
	}
	
	// Parameter versioning (Adv: easy caching, API documentation and execute on browser)
	@GetMapping(value="/users", params = "version=2")
	public UserVersion2 findAllV2Params() {
		return new UserVersion2(1, "Akshay Reddy", "akshayreddy@gmail.com", "pwd", new Date());
	}
	
	// Custom headers versioning (Adv: no URI pollution)
	@GetMapping(value="/users", headers = "API-VERSION=2")
	public UserVersion2 findAllV2Headers() {
		return new UserVersion2(1, "Akshay Reddy", "akshayreddy@gmail.com", "pwd", new Date());
	}
	
	// Accept header or content negotiation versioning (Adv: no URI pollution)
	@GetMapping(value="/users", produces = "application/user-v2+json")
	public UserVersion2 findAllVersion2() {
		return new UserVersion2(1, "Akshay Reddy", "akshayreddy@gmail.com", "pwd", new Date());
	}
}
