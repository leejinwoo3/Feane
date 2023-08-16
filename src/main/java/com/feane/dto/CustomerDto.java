package com.feane.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.feane.entity.Customer;
import com.feane.entity.Menu;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
	private Long id;
	@NotNull(message = "고객 이름는 필수 입력입니다.")
	private String customerNm;
	
	@NotNull(message = "글쓰기는 필수 입력입니다.")
	private String customerwriteig;
	
	
	private List<CustomerImgDto> customerImgDtoList =new ArrayList<>();
	
	private List<Long>cusmtomerImgIds = new ArrayList<>();
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Customer creatCustomer() {
		return modelMapper.map(this, Customer.class);
	}
	public static CustomerDto of(Customer customer) {
		return modelMapper.map(customer, CustomerDto.class);
	}
}
