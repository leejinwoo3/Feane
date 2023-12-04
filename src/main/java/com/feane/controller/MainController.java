package com.feane.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.feane.dto.CustomerSearchDto;
import com.feane.dto.MainCustomerDto;
import com.feane.dto.MainMenuDto;
import com.feane.dto.MenuSearchDto;
import com.feane.service.CustomerService;
import com.feane.service.MemberService;
import com.feane.service.MenuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MenuService menuService;
	private final CustomerService customerService;
	private final PasswordEncoder passwordEncoder;
	private final MemberService memberService;

	@GetMapping(value = "/")
	public String main(MenuSearchDto menuSearchDto, Optional<Integer>page,Model model, CustomerSearchDto customerSearchDto ) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainMenuDto> menus = menuService.getMainMenuPage(menuSearchDto, pageable);
		Page<MainCustomerDto> customers = customerService.getMainCustomerPage(customerSearchDto, pageable);
		
		model.addAttribute("menus",menus);
		model.addAttribute("MenuSearchDto", menuSearchDto);
		model.addAttribute("customers", customers);
		model.addAttribute("customerSearchDto", customerSearchDto);
		model.addAttribute("maxPage", 6);
		return "main";
	}

	/*
	 * @GetMapping(value = "/") public String index(Model model, Authentication
	 * authentication) {
	 * 
	 * // 로그인되지 않은 상태 if (authentication == null ||
	 * !authentication.isAuthenticated()) { // 서버 최초 실행 시 관리자 없으면 등록 if
	 * (memberService.getAdminList().isEmpty()) { Member member =
	 * Member.createMaster(passwordEncoder); memberService.saveMember(member); }
	 * 
	 * // 서버 최초 실행 시 등록된 카테고리 없으면 등록 if (menuService.getCategoryList().isEmpty()) {
	 * Category category1 = Category.createCategory((long) 1, "탕"); Category
	 * category2 = Category.createCategory((long) 2, "구이"); Category category3 =
	 * Category.createCategory((long) 3, "찜"); Category category4 =
	 * Category.createCategory((long) 4, "정식");
	 * 
	 * menuService.saveCategory(category1); menuService.saveCategory(category2);
	 * menuService.saveCategory(category3); menuService.saveCategory(category4); }
	 * 
	 * model.addAttribute("loginMessage", "로그인 후 이용해주세요.");
	 * 
	 * List<MainMenuDto> menus = menuService.mainMenuList();
	 * 
	 * 
	 * model.addAttribute("menus", menus);
	 * 
	 * return "main"; }
	 * 
	 * 
	 * List<MainMenuDto> menus = menuService.mainMenuList();
	 * 
	 * 
	 * model.addAttribute("menus", menus);
	 * 
	 * return "main"; }
	 */

}
