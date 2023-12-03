package com.feane.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.feane.constant.MenuSellStatus;
import com.feane.entity.Category;
import com.feane.entity.Menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuFormDto {
	private Long id;
	
	@NotBlank(message = "상품명은 필수 입력 값입니다.")
	private String menuNm;
	
	@NotNull(message = "가격은 필수 입력입니다.")
	private int price;
	
	@NotNull(message = "재고는 필수 입력입니다.")
	private int stockNumber;
	
	@NotNull(message = "상품상세설명은 필수 입력입니다.")
	private String menuDetail;
	@NotNull(message = "카테고리는 필수 선택입니다.")
	private Long categoryId;

	private Category category;
	
	private MenuSellStatus menuSellStatus;

	private List<MenuImgDto> menuImgDtoList = new ArrayList<>();

	private List<Long> menuImgIds = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	public Menu createMenu(Category category) {
		this.category = category;
		return modelMapper.map(this, Menu.class);
	}
	
	public static MenuFormDto of(Menu menu) {
		return modelMapper.map(menu, MenuFormDto.class);
	}

	
	
	
	
}
