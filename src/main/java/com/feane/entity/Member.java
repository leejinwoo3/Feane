package com.feane.entity;



import org.springframework.security.crypto.password.PasswordEncoder;

import com.feane.constant.Role;
import com.feane.dto.MemberFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name = "member") // 테이블 이름 지정
 @Getter
@Setter
@ToString
public class Member {
	@Id
	@Column(name = "member_id") // 테이블로 생성될때 컬럼 이름을 지정해준다.
	@GeneratedValue(strategy = GenerationType.AUTO) // 기본키를 자동으로 생성해주는 전략 사용(시퀀스랑 비슷한개념)
	private Long id;//

	@Column(unique = true) // 중복된 값이 들어올수 없다.
	private String email;// 상품명->item_nm

	@Column(nullable = false, length = 225) // not null 여부, 컬럼 크기지정
	private String name;// 상품명->item_nm

	@Column(nullable = false, length = 225) // not null 여부, 컬럼 크기지정
	private String password;// 상품명->item_nm

	@Column(nullable = false, length = 225)
	private String address;

	@Enumerated(EnumType.STRING) // enum의 이름을 db에 저장
	private Role role;// 판매상태(SELL,SOLD_OUT) ->itme_sell_status



	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

		// 패스워드 암호화
		String password = passwordEncoder.encode(memberFormDto.getPassword());

		//MemberFormDto를 -> Member 엔티티 객체로 변환
		
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		member.setPassword(password);
		/* member.setRole(Role.ADMIN);  //관리자로 가입할때*/
		member.setRole(Role.ADMIN);//일반 사용자로 가입할때
		
		return member;
	}

	public static Member createMaster(PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode("12345678");
		
		Member member = new Member();
		
		member.setName("master");
		member.setEmail("master@naver.com");
		member.setPassword(password);
		member.setAddress("서울시");
		member.setRole(Role.ADMIN);
		
		return member;
	}

	
}
