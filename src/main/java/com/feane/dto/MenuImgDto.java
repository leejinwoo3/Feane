package com.feane.dto;

import org.modelmapper.ModelMapper;

import com.feane.entity.MenuImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuImgDto {
	private Long id;

	private String imgName;

	private String oriImgName;

	private String imgUrl;

	private String repimgYn;

	private static ModelMapper modelMapper = new ModelMapper();

	public static MenuImgDto of(MenuImg menuImg) {
		return modelMapper.map(menuImg, MenuImgDto.class);
	}
}
