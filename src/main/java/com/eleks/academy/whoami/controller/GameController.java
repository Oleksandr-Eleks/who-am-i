package com.eleks.academy.whoami.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class GameController {


	@GetMapping
	public List<?> test() {
		return List.of(1, 2, 3);
	}

}
