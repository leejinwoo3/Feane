package com.feane.dto;

import com.feane.constant.MenuSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuSearchDto {
	private String searchDateType;
	private MenuSellStatus searchSellStatus;
	private String searchBy;
	private String searchQuery = "";
	
}
