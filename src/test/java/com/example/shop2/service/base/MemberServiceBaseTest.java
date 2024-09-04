package com.example.shop2.service.base;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop2.constant.member.Role;
import com.example.shop2.dto.member.MemberFormDto;
import com.example.shop2.entity.member.Member;
import com.example.shop2.exception.member.DuplicatedEmailException;
import com.example.shop2.repository.member.MemberRepository;
import com.example.shop2.service.member.base.MemberServiceBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionSystemException;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberServiceBaseTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceBase memberServiceBase;


    @BeforeEach
    public void setUp() {
        assertNotNull(memberRepository);
        assertNotNull(memberServiceBase);
    }

    /**
     * '회원파트' 기능 목록
     * - 1. 회원 등록
     * - 2. 로그인
     * - 3. 로그아웃
     *
     * Service 기능 구현 내용
     * - 1. 회원 등록 -> create
     * - 1-1. 이미 등록된 이메일로 회원을 등록할 경우, 이메일 중복됐다는 예외 던지기
     * - 1-2. 필수값이 누락된 DTO로 회원 등록할 경우, 필수값 누락됐다는 예외 던지기
     * - 1-3. DB상의 문제가 발생해서 재시도 처리,
     * - 1-4. 회원 등록 성공
     *
     * - 2. 로그인 처리 -> login
     * - 2-1. 로그인 실패시 false
     * - 2-2. 로그인 성공시 true
     * - 2-3. 해당 이메일로 회원을 조회하지 못한 경우, 회원을 찾을 수 없다는 예외 던지기
     *
     * - 3. 로그아웃 처리 -> logout, 컨트롤러 단에서 세션만 없애기
     *
     *
     */


    @DisplayName("회원 등록 - 1-1. 이미 등록된 이메일로 회원을 등록할 경우, 이메일 중복됐다는 예외 던지기")
    @Test
    public void testCreatingMemberWithDuplicatedEmail() {
        // given
        var duplicatedEmailMemberFormDto = createMemberFormDto(1);
        when(memberRepository.findByEmail(anyString())).thenReturn(new Member());

        assertThrows(DuplicatedEmailException.class,
                () -> memberServiceBase.create(duplicatedEmailMemberFormDto));
    }

    @DisplayName("회원 등록 - 1-2. 필수값이 누락된 DTO로 회원 등록할 경우, 필수값 누락됐다는 예외 던지기")
    @Test
    public void testCreatingMemberWithMissingRequiredValues() {
        var emptyRequiredValuesMemberFormDto = createMemberFormDto(1);
        emptyRequiredValuesMemberFormDto.setEmail(null);

        when(memberRepository.save(any(Member.class)))
                .thenThrow(TransactionSystemException.class);

        assertThrows(TransactionSystemException.class,
                () -> memberServiceBase.create(emptyRequiredValuesMemberFormDto));
    }


    @DisplayName("회원 등록 - 1-4. 회원 등록 성공")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void testCreatingMember(int count) {
        for (int i=0; i<count; i++) {
            var memberFormDto = createMemberFormDto(i);
            var member = Member.createMember(memberFormDto);

            // 중복된 이메일 못찾게 만듦
            when(memberRepository.findByEmail(anyString())).thenReturn(null);
            // save 메서드가 호출되면 member를 반환하게 만듦
            when(memberRepository.save(any(Member.class))).thenReturn(member);

            Member savedMember = memberServiceBase.create(memberFormDto);

            assertSameMember(member, savedMember);
        }


    }

    @DisplayName("로그인 - 2-1. 로그인 실패시 false")
    @Test
    public void testLoginFail() {
        // dto, 엔티티 생성
        var memberFormDto = createMemberFormDto(1);
        var member = Member.createMember(memberFormDto);

        // 리포지토리 세팅
        when(memberRepository.findByEmail(anyString())).thenReturn(member);

        // 실행, 결과가 False
        memberFormDto.setPassword("wrongpassword");
        assertFalse(memberServiceBase.isValidUser(memberFormDto));

    }

    @DisplayName("로그인 - 2-2. 로그인 성공시 true")
    @Test
    public void testLoginSuccess() {
        // dto, 엔티티 생성
        var memberFormDto = createMemberFormDto(1);
        var member = Member.createMember(memberFormDto);

        // 리포지토리 세팅
        when(memberRepository.findByEmail(anyString())).thenReturn(member);

        // 실행, 결과가 true
        assertTrue(memberServiceBase.isValidUser(memberFormDto));
    }

    @DisplayName("로그인 - 2-3. 해당 이메일로 회원을 조회하지 못한 경우, 회원을 찾을 수 없다는 예외 던지기")
    @Test
    public void testLoginWithNonExistentEmail() {
        // dto, 엔티티 생성
        var memberFormDto = createMemberFormDto(1);
        var member = Member.createMember(memberFormDto);

        // 리포지토리 세팅
        when(memberRepository.findByEmail(anyString())).thenReturn(null);

        // 실행, 결과가 False
        assertFalse(memberServiceBase.isValidUser(memberFormDto));
    }


    private MemberFormDto createMemberFormDto(int i) {
        var memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@gmail.com" + i);
        memberFormDto.setAddress("서울시 강남구");
        memberFormDto.setName("홍길동" + i);
        memberFormDto.setPassword("testpassword" + i);
        memberFormDto.setRole(Role.USER.name());

        return memberFormDto;
    }

    private void assertSameMember(Member member, Member savedMember) {
        assertTrue(member.getName().equals(savedMember.getName()) &&
                member.getEmail().equals(savedMember.getEmail()) &&
                member.getPassword().equals(savedMember.getPassword()) &&
                member.getAddress().equals(savedMember.getAddress()) &&
                member.getRole().equals(savedMember.getRole()));
    }

}