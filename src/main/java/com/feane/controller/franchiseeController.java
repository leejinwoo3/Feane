package com.feane.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class franchiseeController {
	@GetMapping(value ="/franchisee")
	public String franchiseeList() {
		return "franchisee/franchiseeList";
	}
}
