package com.feane.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.feane.constant.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Table(name ="orders")
@Getter
@ToString
public class Order  {
	
	@Id
	@Column(name="order_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	private LocalDateTime orderDate; // 주문일
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; //주문상태
	
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name ="member_id")
	private Member member;

	//order에서도 orderItem을 참조할 수 있도록 양방향 관계를 만든다.
	//다만 orderItem은 자식 테이블이 되므로 List로 만든다.
	@OneToMany(mappedBy = "order" , cascade = CascadeType.ALL, orphanRemoval = true , fetch =FetchType.LAZY) //연관관계의 주인 설정(외래키로 지정)
	private List<OrderMenu> orderMenus =new ArrayList<>();
	
	public void addOrderMenu(OrderMenu orderMenu) {
		this.orderMenus.add(orderMenu);
		orderMenu.setOrder(this);
	}
	public static Order createOrder(Member member, List<OrderMenu> orderMenuList) {
		Order order = new Order();
		order.setMember(member);

		for (OrderMenu orderMenu : orderMenuList) {
			order.addOrderMenu(orderMenu);
		}
		order.setOrderStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());

		return order;
	}
	public void cancelOrder() {
		this.orderStatus=OrderStatus.CANCEL;
		
		for(OrderMenu orderMenu : orderMenus){
			orderMenu.cancel();
		}
		
	}
	public int getTotalPrice() {
		int totalPrice=0;
		for(OrderMenu orderMenu : orderMenus) {
			totalPrice +=orderMenu.getTotalPrice();
		}
		return totalPrice;
	}
	
}
