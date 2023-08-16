package com.feane.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.feane.dto.CustomerSearchDto;
import com.feane.dto.MainCustomerDto;

public interface CustomerRepositoryCustom {
	Page<MainCustomerDto> getMainCustomerPage(CustomerSearchDto customerSearchDto, Pageable pageable);


}
