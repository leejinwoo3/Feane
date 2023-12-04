package com.feane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.feane.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);
	
	@Query("select m from Member m where m.role = 'ADMIN'")
	List<Member> getAdminList();
}
