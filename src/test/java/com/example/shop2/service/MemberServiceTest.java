package com.example.shop2.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop2.constant.Role;
import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.exception.member.MemberDuplicatedEmailException;
import com.example.shop2.exception.member.MemberNotFoundException;
import com.example.shop2.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private Random random = new Random();


    @BeforeEach
    public void setUp() {
        assertNotNull(memberService);
        assertNotNull(memberRepository);

        // DB 초기화
        memberRepository.deleteAll();
    }


    /**
     * - 기능 목록
     * - (1) 회원 조회 -> 이메일, 아이디 ✅
     * - 이메일로 회원 조회 성공
     * - 없는 이메일로 회원 조회 실패 -> 예외 발생
     * - 아이디로 회원 조회 성공
     * - 없는 아이디로 회원 조회 실패 -> 예외 발생
     *
     * - (2) 회원 등록 ✅
     * - 회원 등록 성공
     * - 중복된 이메일로 회원 등록 실패 -> 예외 발생
     *
     * - (3) 회원 수정 ✅
     * - 회원 수정 성공
     * - 없는 회원 수정 실패 -> 예외 발생
     * - 필수 값들이 비어있는 회원 수정 실패 -> 예외 발생(DTO에서 처리)
     *
     * - (4) 회원 삭제
     * - 회원 삭제 성공
     * - 없는 회원 삭제 실패
     */

    // (1) 회원 조회 -> 이메일, 아이디 ✅
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("이메일로 회원 조회 테스트")
    public void readByEmailTest(int count) {
        // 회원 생성 및 등록
        List<Member> dummy = new ArrayList<>();
        for (int i=0; i<count; i++) {
            Member member = createMember(i);
            Member savedMember = saveMember(member);
            dummy.add(savedMember);
        }

        // 임의의 회원 선택
        int index = random.nextInt(count);
        Member targetMember = dummy.get(index);

        // 이메일로 조회
        Member foundMember = memberService.readByEmail(targetMember.getEmail());

        // 조회했을 때 null이 아님, 결과 확인
        assertMember(targetMember, foundMember);
    }

    @Test
    @DisplayName("이메일로 회원 조회 실패 했을 때 예외 발생 테스트")
    public void readByEmailExceptionTest() {
        // 없는 회원의 이메일로 조회
        String unknownEmail = "xxx";

        // 예외 발생 확인 -> MemberFonFoundException
        assertThrows(MemberNotFoundException.class, () -> memberService.readByEmail(unknownEmail));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("아이디로 회원 조회 테스트")
    public void readByIdTest(int count) {
        // 회원 생성 및 등록
        List<Member> dummy = new ArrayList<>();
        for (int i=0; i<count; i++) {
            Member member = createMember(i);
            Member savedMember = saveMember(member);
            dummy.add(savedMember);
        }

        // 임의의 회원 선택
        int index = random.nextInt(count);
        Member targetMember = dummy.get(index);

        // 아이디로 조회
        Member foundMember = memberService.readById(targetMember.getId());

        // 조회했을 때 null이 아님, 결과 확인
        assertMember(targetMember, foundMember);
    }

    @Test
    @DisplayName("아이디로 회원 조회 실패 했을 때 예외 발생 테스트")
    public void readByIdExceptionTest() {
        // 없는 회원의 아이디로 조회
        Long unknownId = 0L;

        // 예외 발생 확인 -> MemberFonFoundException
        assertThrows(MemberNotFoundException.class, () -> memberService.readById(unknownId));
    }

    // (2) 회원 등록
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("회원 등록 성공")
    public void createMemberTest(int count) throws Exception {
        // 회원 생성
        // 서비스로 회원 등록 시도
        // 등록된 회원 조회 및 내용 비교
        for (int i=0; i<count; i++) {
            Member member = createMember(i);
            Member createdMember = memberService.create(member);
            Member foundMember = memberService.readById(createdMember.getId());

            assertMember(createdMember, foundMember);
        }
    }

    @Test
    @DisplayName("회원 등록 성공")
    public void createMemberExceptionTest() throws Exception {
        // 회원 생성
        // 서비스로 회원 등록 시도 중복 시도 -> 예외 발생
        Member member = createMember(1);
        Member duplicatedMember = createMember(1);

        memberService.create(member);
        assertThrows(MemberDuplicatedEmailException.class, () -> memberService.create(duplicatedMember));

    }

    // (3) 회원 수정
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("회원 수정 성공")
    public void updateMemberTest(int count) throws Exception {
        // 회원 생성 및 등록
        List<Member> dummy = new ArrayList<>();
        for (int i=0; i<count; i++) {
            Member member = createMember(i);
            Member savedMember = saveMember(member);
            dummy.add(savedMember);
        }

        // 임의의 회원 선택하고 필드 수정
        int index = random.nextInt(count);

        Member targetMember = dummy.get(index);
        MemberFormDto updateMemberFormDto = entityToDto(targetMember);

        updateMemberFormDto.setName("updatedName");
        updateMemberFormDto.setEmail("update@gmail.com");
        updateMemberFormDto.setPassword("updatedPassword");

        // 서비스로 회원 정보 업데이트
        memberService.modify(updateMemberFormDto);

        // 조회했을 때 해당 호원 정보가 같은지 비교
        Member updatedMember = memberService.readByEmail(updateMemberFormDto.getEmail());
        assertEquals(updateMemberFormDto.getName(), updatedMember.getName());
        assertEquals(updateMemberFormDto.getEmail(), updatedMember.getEmail());
        assertEquals(updateMemberFormDto.getPassword(), updatedMember.getPassword());
    }

    @Test
    @DisplayName("없는 회원 수정시 예외 발생")
    public void updateMemberExceptionTest() {
        // 없는 회원 수정 시도 -> 예외 발생
        var updateMemberFormDto = new MemberFormDto();

        updateMemberFormDto.setId(0L);
        updateMemberFormDto.setName("updatedName");
        updateMemberFormDto.setEmail("update@gmail.com");
        updateMemberFormDto.setPassword("updatedPassword");

        assertThrows(MemberNotFoundException.class, () -> memberService.modify(updateMemberFormDto));
    }

    // (4) 회원 삭제
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("회원 정상적으로 삭제")
    public void deleteMemberTest(int count) {
        // 회원 생성 및 등록
        List<Member> dummy = new ArrayList<>();
        for (int i=0; i<count; i++) {
            Member member = createMember(i);
            Member savedMember = saveMember(member);
            dummy.add(savedMember);
        }

        // 임의의 회원 선택
        int index = random.nextInt(count);
        Member targetMember = dummy.get(index);

        // 삭제
        memberService.remove(targetMember.getId());

        // 삭제 후 조회 시 예외 발생 확인
        assertThrows(MemberNotFoundException.class, () -> memberService.readById(targetMember.getId()));
    }

    @Test
    @DisplayName("없는 회원 삭제 시도 시 예외 발생")
    public void deleteMemberExceptionTest() {
        // 없는 회원으로 삭제 시도
        Long unknownId = 0L;

        // 예외 발생
        assertThrows(MemberNotFoundException.class, () -> memberService.remove(unknownId));
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

    private Member saveMember(Member member) {
        Member savedMember = memberRepository.save(member);
        assertNotNull(savedMember);
        return savedMember;
    }

    private void assertMember(Member member, Member foundMember) {
//        assertEquals(member.getId(), foundMember.getId());
        assertEquals(member.getPassword(), foundMember.getPassword());
        assertEquals(member.getEmail(), foundMember.getEmail());
        assertEquals(member.getName(), foundMember.getName());
    }

    private MemberFormDto entityToDto(Member member) {
        var memberFormDto = new MemberFormDto();

        memberFormDto.setId(member.getId());
        memberFormDto.setName(member.getName());
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setPassword(member.getPassword());
        memberFormDto.setAddress(member.getAddress());
        memberFormDto.setRole(member.getRole());

        return memberFormDto;
    }
}