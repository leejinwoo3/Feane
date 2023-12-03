package com.feane.dto;

import com.feane.entity.Category;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainMenuDto {
	private Long id;
	
	private String menuNm;
	private String menuDetail;
	private String imgUrl;
	private Category category;

	private Integer price;
	

	
	@QueryProjection // 쿼리dsl로 결과 조회할때 MainItemDto 객채로 바로 받아올수 있다.
	public MainMenuDto(Long id, String menuNm, String menuDetail, String imgUrl, Integer price, Category category) {
		this.id = id;
		this.menuNm = menuNm;
		this.menuDetail = menuDetail;
		this.imgUrl = imgUrl;
		this.price = price;
		this.category =category;
	}

}
