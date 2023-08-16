package com.feane.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainCustomerDto {
	private Long id;
	private String customerNm;
	private String customerwriteig;
	private String imgUrl;
	
	@QueryProjection // 쿼리dsl로 결과 조회할때 MainItemDto 객채로 바로 받아올수 있다.
	public MainCustomerDto(Long id, String customerNm, String customerwriteig, String imgUrl) {
		this.id = id;
		this.customerNm = customerNm;
		this.customerwriteig = customerwriteig;
		this.imgUrl = imgUrl;
	}
}
