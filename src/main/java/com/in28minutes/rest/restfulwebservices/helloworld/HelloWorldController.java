package com.in28minutes.rest.restfulwebservices.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	@GetMapping( path="/helloworld")
	public String HelloWorld() {
		return "hello world";
	}

	@GetMapping( path="/helloworldbean")
	public HelloWorldBean HelloBean() {
		return new HelloWorldBean("hello world");
	}
	
	@GetMapping( path="/helloworldbean/path/{name}")
	public HelloWorldBean HelloBeanPath(@PathVariable String name) {
		return new HelloWorldBean(String.format("hello world %s", name));
	}

}

