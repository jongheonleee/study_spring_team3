package com.example.shop2.repository.member;


import com.example.shop2.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// 기본적인 CRUD 제공, UPDATE 부분을 일부 구현해야함
// 회원 조회 방법 -> id, email(이 부분은 메서드 이름으로 구현)
// 이 부분도 처리해줘야 스프링이 예외 추상화해줌
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email); // email로 회원 조회
}
