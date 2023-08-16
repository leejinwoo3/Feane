package com.feane.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.feane.dto.CustomerSearchDto;
import com.feane.dto.MainCustomerDto;
import com.feane.dto.MainMenuDto;
import com.feane.dto.MenuSearchDto;
import com.feane.service.CustomerService;
import com.feane.service.MenuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MenuService menuService;
	private final CustomerService customerService;
	@GetMapping(value = "/")
	public String main(MenuSearchDto menuSearchDto, Optional<Integer>page,Model model, CustomerSearchDto customerSearchDto ) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,6);
		Page<MainMenuDto>menus=menuService.getMainMenuPage(menuSearchDto,pageable);
		Page<MainCustomerDto>customers=customerService.getMainCustomerPage(customerSearchDto, pageable);
		
		model.addAttribute("menus",menus);
		model.addAttribute("MenuSearchDto", menuSearchDto);
		model.addAttribute("customers", customers);
		model.addAttribute("customerSearchDto", customerSearchDto);
		model.addAttribute("maxPage", 6);
		return "main";
	}
}
