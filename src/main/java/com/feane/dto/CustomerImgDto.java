package com.feane.dto;

import org.modelmapper.ModelMapper;

import com.feane.entity.CustomerImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerImgDto {
	private Long id;
	private String imgName;

	private String oriImgName;

	private String imgUrl;

	private String repimgYn;

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static CustomerImgDto of(CustomerImg customerImg) {
		return modelMapper.map(customerImg, CustomerImgDto.class);
	}
}
