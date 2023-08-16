package com.feane.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feane.entity.Cart;
import com.feane.entity.CartMenu;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByMemberId(Long memberId);

	

}


