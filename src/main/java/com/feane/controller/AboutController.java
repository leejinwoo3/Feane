package com.feane.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AboutController {
	@GetMapping(value = "/about")
	public String aboutList() {
		return "about/aboutList";
	}
}
