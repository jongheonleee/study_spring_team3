package com.example.shop2.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop2.constant.Role;
import com.example.shop2.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    @DisplayName("주입 확인")
    public void setUp() {
        assertNotNull(memberRepository);
    }

    /**
     * - 기능 목록
     * 1. 이메일로 회원 조회 테스트
     */

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("이메일로 회원 조회 테스트")
    public void readByEmailTest(int count) {
        // 초기화
        memberRepository.deleteAll();

        for (int i=0; i<count; i++) {
            // 더미 데이터 생성 및 저장
            var member = createMember(i);
            Member savedMember = memberRepository.save(member);

            // 이메일로 조회
            Member foundMember = memberRepository.findByEmail(member.getEmail()).get();

            // 조회된 멤버와 생성한 멤버가 동일한지 확인
            assertMember(savedMember, foundMember);
        }
    }

    private Member createMember(int i) {
        var member = new Member();

        member.setName("name" + i);
        member.setEmail("email" + i + "@test.com");
        member.setPassword("password" + i);
        member.setAddress("address" + i);
        member.setRole(Role.USER);

        return member;
    }

    private void assertMember(Member member, Member foundMember) {
        assertEquals(member.getId(), foundMember.getId());
        assertEquals(member.getEmail(), foundMember.getEmail());
        assertEquals(member.getName(), foundMember.getName());
    }
}