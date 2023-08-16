package com.feane.service;

import org.springframework.stereotype.Service;

import com.feane.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberPageService {
	 private final MemberRepository memberRepository;

	 public String findMember(String email){
	        return memberRepository.findByEmail(email).getEmail();
	    }
}
