package com.feane.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feane.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	Member findByEmail(String email);
}
