package com.feane.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feane.dto.OrderDto;
import com.feane.dto.OrderHistDto;
import com.feane.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping(value = "/order")
	public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult,
			Principal principal) {
		// Principal : 로그인한 사용자의 정보를 가져올 수 있다.

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		String email = principal.getName();
		Long orderId;
		try {
			orderId = orderService.order(orderDto, email);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);// 성공시
	}

	// 주문 내역을 보여준다.
	@GetMapping(value = { "/orders", "/orders/{page}" })
	public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
		// 1.한페이지당 4개의 데이터를 가지고 오도록 설정
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

		// 2.서비스 호출
		Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
		model.addAttribute("orders", orderHistDtoList);
		model.addAttribute("maxPage", 5);
		model.addAttribute("page", pageable.getPageNumber());
		return "order/orderHist";
	}

	// 주문취소
	@PostMapping("/order/{orderId}/cancel")
	public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
		// 1.주문취소 권한이 있는지 확인(본인확인)
		if (!orderService.validateOrder(orderId, principal.getName())) {
			return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		// 2.주문취소
		orderService.cancelOrder(orderId);
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);// 성공했을때
	}

	// 주문 삭제
	@DeleteMapping("/order/{orderId}/delete")
	public @ResponseBody ResponseEntity deleteOrder(@PathVariable("orderId") Long orderId, Principal principal) {
		// 본인 인증
		if (!orderService.validateOrder(orderId, principal.getName())) {
			return new ResponseEntity<String>("주문 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		// 주문삭제
		orderService.deleteOrder(orderId);
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}

	@PostMapping("/orderd")
	public @ResponseBody ResponseEntity orderd(@RequestParam Map<String, Object> param, Principal principal) {

		Long menuId = (Long) param.get("menuId");
		Integer count = (Integer) param.get("count");

		OrderDto orderDto = new OrderDto();

		orderDto.setCount(count);
		orderDto.setMenuId(menuId);

		orderService.order(orderDto, principal.getName());

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
