package com.feane.entity;

import java.util.ArrayList;
import java.util.List;

import com.feane.constant.MenuSellStatus;
import com.feane.dto.MenuFormDto;
import com.feane.exception.OutOfStockException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name = "menu") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Menu extends BaseEntity {
	@Id
	@Column(name = "menu_id") // 테이블로 생성될때 컬럼 이름을 지정해준다.
	@GeneratedValue(strategy = GenerationType.AUTO) // 기본키를 자동으로 생성해주는 전략 사용(시퀀스랑 비슷한개념)
	private Long id;// 상품코드

	@Column(nullable = false, length = 50)
	private String menuNm;// 상품명->item_nm

	@Column(nullable = false)
	private int price;// 가격 ->price

	@Column(nullable = false)
	private int stockNumber;// 재고수량->stock_number

	@Lob // clob과 같은 큰타입의 문자타입으로 컬럼을 만든다.
	@Column(nullable = false, columnDefinition = "longtext")
	private String menuDetail;// 상품상세 설명 ->item_detail

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // 연관관계의 주인
																											// // 지정)
	private List<OrderMenu> orderMenus = new ArrayList<>();

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CartMenu> cartMenus = new ArrayList<>();

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<MenuImg> menuimgs = new ArrayList<>();

	

	public void addOrderMenu(OrderMenu orderMenu) {
		this.orderMenus.add(orderMenu);
		orderMenu.setMenu(this);// 양방향 참조관계일때는 orderItem 객체에도 order 객체를 세팅한다.

	}

	@Enumerated(EnumType.STRING) // enum의 이름을 db에 저장
	private MenuSellStatus menuSellStatus;// 판매상태(SELL,SOLD_OUT) ->menu_sell_status

	public void updateMenu(MenuFormDto menuFormDto) {
		this.menuNm = menuFormDto.getMenuNm();
		this.price = menuFormDto.getPrice();
		this.stockNumber = menuFormDto.getStockNumber();
		this.menuDetail = menuFormDto.getMenuDetail();
		this.menuSellStatus = menuFormDto.getMenuSellStatus();
	}

	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber;

		if (restStock < 0) {
			throw new OutOfStockException("상품의 재고가 부족합니다. 현재 재고 수량 : " + this.stockNumber);

		}
		this.stockNumber = restStock;// 남은 재고수량
	}

	public void addStock(int count) {
		this.stockNumber += stockNumber;

	}

}
