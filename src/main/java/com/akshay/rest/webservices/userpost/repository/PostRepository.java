package com.akshay.rest.webservices.userpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.akshay.rest.webservices.userpost.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

}
