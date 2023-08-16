package com.feane.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
	private Long cartMenuId;
	private String menuNm;
	private int price;
	private int count;
	private String imgUrl;
	private Long menuId;

	public CartDetailDto(Long cartMenuId, String menuNm, int price, int count, String imgUrl,Long meunId) {
		this.cartMenuId = cartMenuId;
		this.menuNm = menuNm;
		this.price = price;
		this.count = count;
		this.imgUrl = imgUrl;
		this.menuId= meunId;

	}
}
