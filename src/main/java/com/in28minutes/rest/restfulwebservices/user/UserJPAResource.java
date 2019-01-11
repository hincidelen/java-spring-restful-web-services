package com.in28minutes.rest.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.restfulwebservices.exception.UserNotFoundException;
	
@RestController
public class UserJPAResource {

	@Autowired
	private UserDaoService service;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping("/jpa/users2/{id}")
	private User retrieveUser(@PathVariable int id){
		User newUser = service.findOne(id);
		if(newUser==null)
			throw new UserNotFoundException("User id="+id+" is not found." );
	
		return newUser;
	}
	
	@GetMapping("/jpa/users/{id}")
	public Resource<User> retrieveUser2(@PathVariable int id){
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User id="+id+" is not found." );
		
		//adds Hateoas links to body
		Resource<User> resource = new Resource<User>(user.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		resource.add(linkTo.withRel("all-user"));
		
		return resource;
	}
	
	@DeleteMapping("/jpa/users/{id}")
	private void deleteUser(@PathVariable int id){
		userRepository.deleteById(id);
	}
	 
	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
		//returne ResponseEntity diyerek HTTP status 201 created yapıyoruz.
		User savedUser = userRepository.save(user);
		
		//pathin sonuna /{id} ekleyerek yaratılan objenin get URI
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		// location headerına ekler. 
		return ResponseEntity.created(location).build();
	}
	

	 
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post){
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User id="+id+" is not found." );
		

		post.setUser(user.get());
		postRepository.save(post);
		
		//pathin sonuna /{id} ekleyerek yaratılan objenin get URI
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(post.getId()).toUri();
		// location headerına ekler. 
		return ResponseEntity.created(location).build();
		
		
	}
	
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveUserPosts(@PathVariable int id){
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User id="+id+" is not found." );
		
		return user.get().getPosts();
	}
}
