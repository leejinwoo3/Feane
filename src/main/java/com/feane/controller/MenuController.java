package com.feane.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.feane.dto.CustomerSearchDto;
import com.feane.dto.MainCustomerDto;
import com.feane.dto.MainMenuDto;
import com.feane.dto.MenuFormDto;
import com.feane.dto.MenuSearchDto;
import com.feane.entity.Menu;
import com.feane.service.CustomerService;
import com.feane.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	private final CustomerService customerService;

	@GetMapping(value = "/menu/feane")
	public String menushopList(Model model, MenuSearchDto menuSearchDto, Optional<Integer> page, Authentication authentication) {

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainMenuDto> menus = menuService.getMainMenuPage(menuSearchDto, pageable);

		model.addAttribute("menus", menus);

		model.addAttribute("menuSearchDto", menuSearchDto);

		model.addAttribute("maxPage", 6);
		return "menu/menuList";
	}

	@GetMapping(value = "/menu/cutomer")
	public String customerhopList(Model model, CustomerSearchDto customerSearchDto, Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
		Page<MainCustomerDto> customers = customerService.getMainCustomerPage(customerSearchDto, pageable);

		model.addAttribute("customers", customers);

		model.addAttribute("customerSearchDto", customerSearchDto);

		model.addAttribute("maxPage", 3);
		return "menu/menuList";
	}

	// 메뉴 상세 페이지
	@GetMapping(value = "/menu/{menuId}")
	public String menuDtl(Model model, @PathVariable("menuId") Long menuId) {
		MenuFormDto menuFormDto = menuService.getMenuDtl(menuId);
		model.addAttribute("menu", menuFormDto);
		return "menu/menuDtl";
	}

	// 메뉴 등록 페이지
	@GetMapping(value = "/admin/menu/new")
	public String menuForm(Model model) {
		model.addAttribute("menuFormDto", new MenuFormDto());
		return "menu/menuForm";
	}

	// 메뉴 등록
	@PostMapping(value = "/admin/menu/new")
	public String menuNew(@Valid MenuFormDto menuFormDto, BindingResult bindingResult, Model model,
			@RequestParam("menuImgFile") List<MultipartFile> menuImgFileList) {
		if (bindingResult.hasErrors()) {
			return "menu/menuForm";
		}
		if (menuImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "menu/menuForm";
		}
		try {
			menuService.saveMenu(menuFormDto, menuImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품등록중 에러가 발생했습니다.");
			return "menu/menuForm";
		}
		return "redirect:/";
	}

	// 메뉴 관리
	@GetMapping(value = { "/admin/menus", "/admin/menus/{page}" })
	public String menuMange(MenuSearchDto memnuSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

		Page<Menu> menus = menuService.getAdminMenuPage(memnuSearchDto, pageable);
		model.addAttribute("menus", menus);
		model.addAttribute("memnuSearchDto", memnuSearchDto);
		model.addAttribute("maxPage", 5);
		return "menu/menuMng";
	}

	// 메뉴 수정 페이지
	@GetMapping(value = "/admin/menu/{menuId}")
	public String menuDtl(@PathVariable("menuId") Long menuId, Model model) {
		try {
			MenuFormDto menuFormDto = menuService.getMenuDtl(menuId);
			model.addAttribute("menuFormDto", menuFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "메뉴정보를 가져올때 에러가 발생했습니다.");
			model.addAttribute("menuFormDto", new MenuFormDto());
			return "menu/menuForm";

		}
		return "menu/menuModifyForm";
	}

//메뉴 수정
	@PostMapping(value = "/admin/menu/{menuId}")
	public String menuUpdate(@Valid MenuFormDto menuFormDto, Model model, BindingResult bindingResult,
			@RequestParam("menuImgFile") List<MultipartFile> menuImgFileList) {
		if (bindingResult.hasErrors()) {
			return "menu/menuForm";
		}

		if (menuImgFileList.get(0).isEmpty() && menuFormDto.getId() == null) {
			return "menu/menuForm";
		}
		try {
			menuService.updateMenu(menuFormDto, menuImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "메뉴정보를 가져올때 에러가 발생했습니다.");
			return "menu/menuForm";
		}
		return "redirect:/";

	}

	@DeleteMapping(value = "/admin/menu/{menuId}/delete")
	public @ResponseBody ResponseEntity deleteMenu(@PathVariable("menuId") Long menuId) {

		menuService.deleteMenu(menuId);
		return new ResponseEntity<Long>(menuId, HttpStatus.OK);
	}

}
