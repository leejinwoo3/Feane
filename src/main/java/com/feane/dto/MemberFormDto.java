package com.feane.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;

	@Email(message = "이메일 형식으로 입력해주세요")
	@NotEmpty(message = "이메일은 필수입력 입니다.")
	private String email;

	@Length(min = 8, max = 16, message = "비밀번호는 8자 ~16자 사이")
	@NotEmpty(message = "비밀번호는 필수입력 입니다.")
	private String password;

	@NotEmpty(message = "주소는 필수입력 입니다.")
	private String address;

}
