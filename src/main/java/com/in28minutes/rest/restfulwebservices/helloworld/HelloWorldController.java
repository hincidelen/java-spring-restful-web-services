package com.in28minutes.rest.restfulwebservices.helloworld;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	@Autowired
	private MessageSource messageSource;
	
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

	@GetMapping( path="/helloworld_int")
	public String HelloWorldInternationalized(@RequestHeader(name="Accept-Language", required=false) Locale locale) {
		return messageSource.getMessage("good.morning.message", null, locale);
	}
}

