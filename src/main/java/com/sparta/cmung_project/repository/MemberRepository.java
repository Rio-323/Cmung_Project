package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);

}
