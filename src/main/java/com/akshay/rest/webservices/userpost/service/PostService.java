package com.akshay.rest.webservices.userpost.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akshay.rest.webservices.userpost.entity.Post;
import com.akshay.rest.webservices.userpost.exception.NotFoundException;
import com.akshay.rest.webservices.userpost.repository.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	public Post findById(int id) {
		Optional<Post> post = postRepository.findById(id);
		
		if(!post.isPresent()) {
			throw new NotFoundException("Post with post id: " + id + " doesn't exist");
		}
		
		return post.get();
	}
	
	public void save(Post post) {
		postRepository.save(post);
	}
	
	public void update(int id, Post actualPost, Post updatePost) {		
		actualPost.setDescription(updatePost.getDescription());
		postRepository.save(actualPost);
	}
	
	public void deleteById(int id) {	
		postRepository.deleteById(id);
	}

}
