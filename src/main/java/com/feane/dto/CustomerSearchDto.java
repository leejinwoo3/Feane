package com.feane.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchDto {
	private String searchDateType;
	private String searchBy;
	private String searchQuery = "";
}
