package com.feane.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feane.entity.Member;
import com.feane.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional // 쿼리문 수정시 에러가 발생하면 변경된 데이터를 이전상태로 출력
@RequiredArgsConstructor // @autowired를 사용하지 않고 필드의 의존성을 주입시킨다.
public class MemberService implements UserDetailsService {
	private final MemberRepository memberRepository;

	public Member saveMember(Member member) {
		validateDuplicateMember(member);// 이메일중복 체크
		Member savedMember = memberRepository.save(member);// insert
		return savedMember;// 회원가입된 데이터를 리턴해준다.

	}

	// 이메일 중복
	private void validateDuplicateMember(Member member) {

		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);

		if (member == null) {// 사용자가 없다면
			throw new UsernameNotFoundException(email);
		}

		// 사용자가 있다면 userDetails 객체를 만들어서 변환
		return User.builder().username(member.getEmail()).password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}
	
	
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

}
