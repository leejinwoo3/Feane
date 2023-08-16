package com.feane.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.feane.constant.OrderStatus;
import com.feane.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHistDto {

	public OrderHistDto(Order order) {
		this.orderId = order.getId();
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
		this.orderStatus = order.getOrderStatus();
	}

	private Long orderId; // 주문아이디

	private String orderDate; // 주문날짜
	
	private OrderStatus orderStatus; // 주문상태
	
	private List<OrderMenuDto> orderMenuDtoList = new ArrayList<>();

	public void addOrderMenuDto(OrderMenuDto orderMenuDto) {
		orderMenuDtoList.add(orderMenuDto);
	}
}
