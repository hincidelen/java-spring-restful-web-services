package com.in28minutes.rest.restfulwebservices.user;

import java.net.URI;
import java.util.List;

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
public class UserResource {

	@Autowired
	private UserDaoService service;
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return service.findAll();
	}
	
	@GetMapping("/users/{id}")
	private User retrieveUser(@PathVariable int id){
		User newUser = service.findOne(id);
		if(newUser==null)
			throw new UserNotFoundException("User id="+id+" is not found." );
	
		return newUser;
	}
	
	@GetMapping("/users2/{id}")
	public Resource<User> retrieveUser2(@PathVariable int id){
		User user = service.findOne(id);
		if(user==null)
			throw new UserNotFoundException("User id="+id+" is not found." );
		
		//adds Hateoas links to body
		Resource<User> resource = new Resource<User>(user);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		resource.add(linkTo.withRel("all-user"));
		
		return resource;
	}
	
	@DeleteMapping("/users/{id}")
	private User deleteUser(@PathVariable int id){
		User newUser = service.deleteById(id);
		if(newUser==null)
			throw new UserNotFoundException("User id="+id+" is not found." );
		return newUser;
	}
	 
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
		//returne ResponseEntity diyerek HTTP status 201 created yapıyoruz.
		User savedUser = service.save(user);
		
		//pathin sonuna /{id} ekleyerek yaratılan objenin get URI
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		// location headerına ekler. 
		return ResponseEntity.created(location).build();
	}
}
