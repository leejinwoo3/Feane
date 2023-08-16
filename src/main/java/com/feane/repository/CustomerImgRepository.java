package com.feane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feane.entity.CustomerImg;

public interface CustomerImgRepository extends JpaRepository<CustomerImg, Long>{
  List<CustomerImg> findByCustomerIdOrderByIdAsc(Long customerId);
  
  CustomerImg findByCustomerIdAndRepimgYn(Long customerId,String repimgYn);
}
