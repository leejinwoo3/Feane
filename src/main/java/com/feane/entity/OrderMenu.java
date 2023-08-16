package com.feane.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_menu")
@Setter
@Getter
@ToString
public class OrderMenu {
	@Id
	@Column(name = "order_menu_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice; // 주문 가격

	private int count; // 수량

	public static OrderMenu createOrderMenu(Menu menu, int count) {
		OrderMenu orderMenu = new OrderMenu();
		orderMenu.setMenu(menu);
		orderMenu.setCount(count);
		orderMenu.setOrderPrice(menu.getPrice());

		menu.removeStock(count);// 재고감소

		return orderMenu;
	}
	public void cancel() {
		this.getMenu().addStock(count);
	}
	public int getTotalPrice() {
		return orderPrice * count;
	}
}
