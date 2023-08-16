package com.feane.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.feane.dto.OrderDto;
import com.feane.dto.OrderHistDto;
import com.feane.dto.OrderMenuDto;
import com.feane.entity.Member;
import com.feane.entity.Menu;
import com.feane.entity.MenuImg;
import com.feane.entity.Order;
import com.feane.entity.OrderMenu;
import com.feane.repository.MemberRepository;
import com.feane.repository.MenuImgRepository;
import com.feane.repository.MenuRepository;
import com.feane.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final MenuImgRepository menuImgRepository;

//주문하기
	public Long orders(List<OrderDto> orderDtoList, String email) {
		  Member member = memberRepository.findByEmail(email);
	        List<OrderMenu> orderMenuList = new ArrayList<>();

	        for (OrderDto orderDto : orderDtoList) {
	            Menu menu = menuRepository.findById(orderDto.getMenuId())
	                    .orElseThrow(EntityNotFoundException::new);

	            OrderMenu orderMenu = OrderMenu.createOrderMenu(menu, orderDto.getCount());
	            orderMenuList.add(orderMenu);
	        }

	        Order order = Order.createOrder(member, orderMenuList);
	        orderRepository.save(order);

	        return order.getId();
	    }


	// 주문 목록
	@Transactional(readOnly = true)
	public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
		
		List<Order> orders = orderRepository.findOrders(email, pageable);
		
		Long totalCount = orderRepository.conutOrder(email);

		List<OrderHistDto> orderHistDtos = new ArrayList<>();

		for (Order order : orders) {
			OrderHistDto orderHisDto = new OrderHistDto(order);
			List<OrderMenu> orderMenus = order.getOrderMenus();

			for (OrderMenu orderMenu : orderMenus) {
				MenuImg menuImg = menuImgRepository.findByMenuIdAndRepimgYn(orderMenu.getMenu().getId(), "Y");
				OrderMenuDto orderMenuDto = new OrderMenuDto(orderMenu, menuImg.getImgUrl());
				orderHisDto.addOrderMenuDto(orderMenuDto);
			}
			orderHistDtos.add(orderHisDto);
		}
		return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);
	}
	public boolean validateOrder(Long orderId, String email) {
		Member curMember = memberRepository.findByEmail(email);// 로그인한 사용자 찾기
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);// 주문 내역

		Member saveMember = order.getMember();// 주문한 사용자 찾기
		// 로그인한 사용자의 이메일과 주문한 사용자의 이메일이 같은지 최종 비교
		if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
			// 같지 않으면
			return false;

		}
		return true;
	}
	// 주문취소
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		//OrderStatus를 update -> entity 의 필드 값을 바꿔주면 된다.
		order.cancelOrder();
	}
	public void deleteOrder(Long orderId) {
		//delete 하기전에 select를 한번 해준다. -> 영속성 컨텍스트 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		//delete
		orderRepository.delete(order);
	}


	  public Long order(OrderDto orderDto, String email){

	        Menu menu = menuRepository.findById(orderDto.getMenuId())
	                .orElseThrow(EntityNotFoundException::new);

	        Member member = memberRepository.findByEmail(email);

	        List<OrderMenu> orderMenuList = new ArrayList<>();
	        OrderMenu orderMenu = OrderMenu.createOrderMenu(menu, orderDto.getCount());
	        orderMenuList.add(orderMenu);
	        Order order = Order.createOrder(member, orderMenuList);
	        orderRepository.save(order);

	        return order.getId();

	  }
}
