package com.feane.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.feane.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select o from Order o where o.member.email = :email order by o.orderDate desc")
	List<Order> findOrders(@Param("email") String email, Pageable pageable);

	// 현재 로그인한 회원의 주문 개수가 몇개인지 조회
	@Query("select count(o) from Order o where o.member.email = :email")
	Long conutOrder(@Param("email") String email);
}
