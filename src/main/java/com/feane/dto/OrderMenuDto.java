package com.feane.dto;

import com.feane.entity.OrderMenu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMenuDto {
	public OrderMenuDto(OrderMenu orderMenu, String imgUrl) {
		this.menuNm = orderMenu.getMenu().getMenuNm();
		this.count = orderMenu.getCount();
		this.orderPrice = orderMenu.getOrderPrice();
		this.imgUrl = imgUrl;
	}

	private String menuNm; // 상품명

	private int count; // 주문수량

	private int orderPrice; // 주문 금액

	private String imgUrl; // 상품 
}
