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
@Table(name = "cart_menu")
@Setter
@Getter
@ToString
public class CartMenu extends BaseEntity {
	@Id
	@Column(name = "cart_menu_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int count; // 수량

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	public static CartMenu createCartMenu(Cart cart, Menu menu, int cout) {
		CartMenu cartMenu = new CartMenu();
		cartMenu.setCart(cart);
		cartMenu .setMenu(menu);
		cartMenu.setCount(cout);
		
	
		return cartMenu;
	}
	public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
	
}
