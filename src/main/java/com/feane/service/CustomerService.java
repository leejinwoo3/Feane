package com.feane.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.feane.dto.CustomerDto;
import com.feane.dto.CustomerImgDto;
import com.feane.dto.CustomerSearchDto;
import com.feane.dto.MainCustomerDto;
import com.feane.entity.Customer;
import com.feane.entity.CustomerImg;
import com.feane.repository.CustomerImgRepository;
import com.feane.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
	private final CustomerImgRepository customerImgRepository;
	private final CustomerImgService customerImgService;
	private final CustomerRepository customerRepository;

	public Long saveCustomer(CustomerDto customerDto, List<MultipartFile> customerImgFileList) throws Exception {
		Customer customer = customerDto.creatCustomer();
		customerRepository.save(customer);
		for (int i = 0; i < customerImgFileList.size(); i++) {
			// 부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			CustomerImg customerImg = new CustomerImg();
			customerImg.setCustomer(customer);
			// 첫번째 이미지 일때 대표상품 이미지 지정
			if (i == 0) {
				customerImg.setRepimgYn("Y");
			} else {
				customerImg.setRepimgYn("N");
			}
			System.out.println("a");

			customerImgService.savaCustomerImg(customerImg, customerImgFileList.get(i));
			System.out.println("b");
		}
		return customer.getId();
	}
	//메뉴 가져오기
		@Transactional(readOnly = true)
		public CustomerDto getCustomerDtl(Long customerId) {
			List<CustomerImg> customerImgList = customerImgRepository.findByCustomerIdOrderByIdAsc(customerId);
			List<CustomerImgDto> customerImgDtoList = new ArrayList<>();
			for (CustomerImg customerImg : customerImgList) {
				CustomerImgDto customerImgDto = CustomerImgDto.of(customerImg);
				customerImgDtoList.add(customerImgDto);
			}
			Customer customer = customerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);
			
			CustomerDto customerDto = CustomerDto.of(customer);
			
			customerDto.setCustomerImgDtoList(customerImgDtoList);
			
			return customerDto;
			
		}
		public Long updateCustomer(CustomerDto customerDto, List<MultipartFile>menuImgFileList) throws Exception{
			Customer customer = customerRepository.findById(customerDto.getId()).orElseThrow(EntityNotFoundException::new);
			customer.updateCustomer(customerDto);
			List<Long>menuImgIds =customerDto.getCusmtomerImgIds();
			for(int i=0;i<menuImgFileList.size();i++) {
				customerImgService.updateCustomerImg(menuImgIds.get(i),menuImgFileList.get(i));
			}
			return customer.getId();
		}
	@Transactional(readOnly = true)
	public Page<MainCustomerDto> getMainCustomerPage(CustomerSearchDto customerSearchDto, Pageable pageable) {
		Page<MainCustomerDto> mainCustomerPage= customerRepository.getMainCustomerPage(customerSearchDto, pageable);
		return mainCustomerPage;
	}
}
