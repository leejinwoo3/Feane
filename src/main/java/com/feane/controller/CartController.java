package com.feane.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feane.dto.CartDetailDto;
import com.feane.dto.CartDto;
import com.feane.dto.CartMenuDto;
import com.feane.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	@PostMapping(value = "/cart")
	public @ResponseBody ResponseEntity order(@RequestBody @Valid CartMenuDto cartMenuDto, BindingResult bindingResult,
			Principal principal) {

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}

			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}

		String email = principal.getName();
		Long carMenuId;

		try {
			carMenuId = cartService.addCart(cartMenuDto, email);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Long>(carMenuId, HttpStatus.OK);
	}

	@GetMapping(value = "/cart")
	public String orderHist(Principal principal, Model model) {
		List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
		model.addAttribute("cartMenus", cartDetailList);
		return "cart/cartList";
	}
	
	
	//수정
	@PatchMapping(value = "/cartMenu/{cartMenuId}")
	public @ResponseBody ResponseEntity updateCartMenu(@PathVariable("cartMenuId") Long cartMenuId, int count,
			Principal principal) {

		if (count <= 0) {
			return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
		} else if (!cartService.validateCartMenu(cartMenuId, principal.getName())) {
			return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		cartService.updateCartMenuCount(cartMenuId, count);
		return new ResponseEntity<Long>(cartMenuId, HttpStatus.OK);
	}

	//삭제
	@DeleteMapping(value = "/cartMenu/{cartMenuId}/delete")
	public @ResponseBody ResponseEntity deleteCartMenu(@PathVariable("cartMenuId") Long cartMenuId,
			Principal principal) {
		if (!cartService.validateCartMenu(cartMenuId, principal.getName())) {
			return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		cartService.deleteCartMenu(cartMenuId);
		return new ResponseEntity<Long>(cartMenuId, HttpStatus.OK);
	}

	
	@PostMapping(value = "/cart/orders")
	public @ResponseBody ResponseEntity orderCartMenu(@RequestBody CartDto cartOrderDto, Principal principal) {

		List<CartDto> cartDtoList = cartOrderDto.getCartDtoList();

		if (cartDtoList == null || cartDtoList.size() == 0) {
			return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
		}

		for (CartDto cartOrder : cartDtoList) {
			if (!cartService.validateCartMenu(cartOrder.getCartMenuId(), principal.getName())) {
				return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
			}
		}

		Long orderId = cartService.orderCartMenu(cartDtoList, principal.getName());
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
	
	/*
	 * @PostMapping("/carted") public @ResponseBody ResponseEntity
	 * carted(@RequestParam Map<String, Object> param, Principal principal) { Long
	 * menuId=(Long)param.get("menuId"); Integer count=(Integer)param.get("count");
	 * 
	 * CartMenuDto cartMenuDto = new CartMenuDto(); cartMenuDto.setCount(count);
	 * cartMenuDto.setMenuId(menuId);
	 * 
	 * cartService.addCart(cartMenuDto, principal.getName()); return new
	 * ResponseEntity<String>(HttpStatus.OK);
	 * 
	 * }
	 */
}
