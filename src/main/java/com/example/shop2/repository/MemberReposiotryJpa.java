package com.example.shop2.repository;


import com.example.shop2.entity.Member;
import com.example.shop2.exception.EmptyRequriedValuesException;
import org.springframework.data.jpa.repository.JpaRepository;

// 기본적인 CRUD 제공, UPDATE 부분을 일부 구현해야함
// 회원 조회 방법 -> id, email(이 부분은 메서드 이름으로 구현)
public interface MemberReposiotryJpa extends JpaRepository<Member, Long> {
    Member findByEmail(String email); // email로 회원 조회
}
