package com.example.blogfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BlogFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogFinderApplication.class, args);
	}

}
