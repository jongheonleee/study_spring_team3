package com.example.shop2.repository.member;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop2.constant.member.Role;
import com.example.shop2.entity.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionSystemException;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // 테스트용 데이터 베이스 연결 h2 데이터베이스 사용
class MemberReposiotryTest {

    @Autowired
    private MemberRepository memberReposiotry; // 추후에 인터페이스로 변환

    @BeforeEach
    public void setUp() {
        assertNotNull(memberReposiotry);
        memberReposiotry.deleteAll();
    }

    /**
     * '회원파트' 기능 목록
     * - 1. 회원 등록
     * - 2. 로그인
     * - 3. 로그아웃
     *
     * Repository 기능 구현 내용
     * - 1. 회원 등록 -> save ✅
     * - 1-1. 필수값 누락
     * - 1-2. 중복된 email로 회원 등록
     * - 1-3. 중복된 id로 회원 등록 x(회원 id는 현재 시퀀스(자동증분)임)
     * - 1-4. 회원 등록 성공
     *
     * - 2. 회원 조회 -> id, email 찾기 ✅
     * - 2-1. 없는 id로 회원 조회, 실패 -> ORM은 EntityNotFounException 던짐, 이 부분 추상화
     * - 2-2. id로 회원 조회
     * - 2-3. 없는 email로 회원 조회, 실패 -> ORM은 EntityNotFounException 던짐, 이 부분 추상화
     * - 2-4. email로 회원 조회
     */


    @DisplayName("회원 등록 - 1-1. 필수값 누락")
    @Test
    public void testCreatingMemberWithMissingRequiredValues() {
        var member = createMember(1);
        member.setEmail(null);
        assertThrows(TransactionSystemException.class,
                () -> memberReposiotry.save(member));
    }

    @DisplayName("회원 등록 - 1-2. 중복된 email로 회원 등록")
    @Test
    public void testCreatingMemberWithDuplicatedEmail() {
        var member = createMember(1);
        memberReposiotry.save(member);
        var member2 = createMember(1);
        // member 로 해버리면 예외 안터짐
        // 엔티티의 관리 상태: 동일한 엔티티 객체(member)를 두 번 save 하는 경우, JPA는 동일한 객체가 이미 관리되고 있다고 간주하고 데이터베이스에 새로운 INSERT 쿼리를 보내지 않음
        assertThrows(DataIntegrityViolationException.class, () -> memberReposiotry.save(member2));
    }

    @DisplayName("회원 등록 - 1-3. 중복된 id로 회원 등록")
    @Test
    public void testCreatingMemberWithDuplicatedId() {
    }

    @DisplayName("회원 등록 - 1-4. 회원 등록 성공")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void testCreatingMember(int count) {
        for (int i=0; i<count; i++) {
            var member = createMember(i);
            Member savedMember = memberReposiotry.save(member);

            assertNotNull(savedMember);
            assertSameMember(member, savedMember);
        }
    }

    @DisplayName("회원 조회 - 2-1. 없는 id로 회원 조회, 실패")
    @Test
    public void testFindingMemberWithNonExistingId() {
        assertTrue(memberReposiotry.findById(1L).isEmpty());
    }

    @DisplayName("회원 조회 - 2-2. id로 회원 조회")
    @Test
    public void testFindingMemberById() {
        var member = createMember(1);
        Member savedMember = memberReposiotry.save(member);
        Member foundMember = memberReposiotry.findById(savedMember.getId()).orElseThrow();
        assertSameMember(member, foundMember);
    }

    @DisplayName("회원 조회 - 2-3. 없는 email로 회원 조회, 실패")
    @Test
    public void testFindingMemberWithNonExistingEmail() {
        assertNull(memberReposiotry.findByEmail("xxx"));
    }

    @DisplayName("회원 조회 - 2-4. email로 회원 조회")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void testFindingMemberByEmail(int count) {
        for (int i=0; i<count; i++) {
            var member = createMember(i);
            Member savedMember = memberReposiotry.save(member);
            Member foundMember = memberReposiotry.findByEmail(savedMember.getEmail());
            assertSameMember(member, foundMember);
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

    private Member createAdmin(int i) {
        var member = new Member();

        member.setName("name" + i);
        member.setEmail("email" + i + "@test.com");
        member.setPassword("password" + i);
        member.setAddress("address" + i);
        member.setRole(Role.ADMIN);

        return member;
    }

    private void assertSameMember(Member member, Member savedMember) {
         assertTrue(member.getName().equals(savedMember.getName()) &&
                member.getEmail().equals(savedMember.getEmail()) &&
                member.getPassword().equals(savedMember.getPassword()) &&
                member.getAddress().equals(savedMember.getAddress()) &&
                member.getRole().equals(savedMember.getRole()));
    }

}

