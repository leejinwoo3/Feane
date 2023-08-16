package com.feane.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
	private Long cartMenuId;
	private List<CartDto> cartDtoList;
}
