package com.feane.entity;

import com.feane.dto.CustomerDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name = "customer") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Customer extends BaseEntity {
	@Id
	@Column(name = "customer_id") // 테이블로 생성될때 컬럼 이름을 지정해준다.
	@GeneratedValue(strategy = GenerationType.AUTO) // 기본키를 자동으로 생성해주는 전략 사용(시퀀스랑 비슷한개념)
	private Long id;// 상품코드
	
	@Column(nullable = false, length = 50)
	private String customerNm;// 상품명->item_nm
	
	@Lob // clob과 같은 큰타입의 문자타입으로 컬럼을 만든다.
	@Column(nullable = false, columnDefinition = "longtext")
	private String customerwriteig;// 상품상세 설명 ->item_detail
	public void updateCustomer(CustomerDto customerDto) {
		this.customerNm=customerDto.getCustomerNm();
		this.customerwriteig = customerDto.getCustomerwriteig();
	}
}
