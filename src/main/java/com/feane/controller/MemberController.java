package com.feane.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.feane.dto.CustomerDto;
import com.feane.dto.MemberFormDto;
import com.feane.entity.Member;
import com.feane.service.CustomerService;
import com.feane.service.MemberService;
import com.feane.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final CustomerService customerService;
	private final MenuService menuService;
	// 글쓰기
	@GetMapping(value = "/members/qa")
	public String qa(Model model) {
		model.addAttribute("customerDto", new CustomerDto());
		return "member/qa";
	}

	// 글쓰기 등록
	@PostMapping(value = "/members/customer")
	public String qaForm(@Valid CustomerDto customerDto, BindingResult bindingRestult, Model model,
			@RequestParam("menuImgFile") List<MultipartFile> customerImgFileList) {
		if (bindingRestult.hasErrors()) {
			return "member/qa";
		}
		if (customerImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "이미지는 필수입니다.");
			return "member/qa";
		}
		try {
			customerService.saveCustomer(customerDto, customerImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품등록중 에러가 발생했습니다.");
			return "menu/menuForm";
		}
		return "redirect:/";

	}

	// 글쓰기 수정페이지
	@GetMapping(value = "/members/{id}")
	public String getcustomerDtl(@PathVariable("id") Long customerId, Model model) {
		try {
			CustomerDto customerDto= customerService.getCustomerDtl(customerId);
			model.addAttribute("customerDto", customerDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "고객정보를 가져올때 에러가 발생했습니다.");
			model.addAttribute("customerDto", new CustomerDto() );
			return "member/customerDto";
		}
		return "member/CustomerModifyForm";
	};
	// 글쓰기 수정
	@PostMapping(value = "/members/{customerId}")
	public String menuUpdate(@Valid CustomerDto customerDto, Model model, BindingResult bindingResult,
			@RequestParam("customerImgFile") List<MultipartFile> customerImgFileList) {
		if (bindingResult.hasErrors()) {
			return "member/qa";
		}

		if (customerImgFileList.get(0).isEmpty() && customerDto.getId() == null) {
			return "member/qa";
		}
		try {
			customerService.updateCustomer(customerDto, customerImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "메뉴정보를 가져올때 에러가 발생했습니다.");
			return "member/qa";
		}
		return "redirect:/";

	}
	// 로그인 화면
	@GetMapping(value = "/members/login")
	public String memberList() {
		return "member/member";
	}

	// 회원가입 화면
	@GetMapping(value = "/members/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}

	@PostMapping(value = "/members/new")
	public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingRestult, Model model)
	// @valid:유효성을 검증하려는 객체 앞에 붙인다.
	// BindingResult:유효성 검증후의 결과가 들어있음
	{
		if (bindingRestult.hasErrors()) {
			// 에러가 있다면 회원가입 페이지로 이동
			return "member/memberForm";
		}
		try {
			// MemberFormDto -> Member Entity, 비밀번호 암호화
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);

		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}

		return "redirect:/";
	}

	// 로그인 실패했을때
	@GetMapping(value = "/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 입력해주세요");
		return "member/member";

	}
	
	
	
}
