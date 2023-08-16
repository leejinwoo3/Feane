package com.feane.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.feane.dto.CustomerDto;
import com.feane.dto.CustomerSearchDto;
import com.feane.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
	List<Customer> findByCustomerNm(String customerNm);
	/*
	 * List<Customer> findByCustomerNmAndcustomerwriteig(String customerNm,String
	 * customerwriteig);
	 */


}
