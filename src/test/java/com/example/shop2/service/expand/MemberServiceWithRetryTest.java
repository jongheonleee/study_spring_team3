package com.example.shop2.service.expand;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.shop2.constant.Role;
import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.exception.global.RetryFailedException;
import com.example.shop2.service.base.MemberServiceBase;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class MemberServiceWithRetryTest {


    @Mock
    private MemberServiceBase memberServiceBase;

    @InjectMocks
    private MemberServiceWithRetry memberServiceWithRetry;


    private static class CustomDataAccessException extends DataAccessException {

        // 기본 생성자
        public CustomDataAccessException(String msg) {
            super(msg);
        }

        // 원인을 포함하는 생성자
        public CustomDataAccessException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    @BeforeEach
    public void setUp() {
        assertNotNull(memberServiceWithRetry);
        this.memberServiceWithRetry = new MemberServiceWithRetry(memberServiceBase);
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


    @DisplayName("회원 등록 - 1-3. DB상의 문제가 발생해서 재시도 처리")
    @Test
    public void testCreatingMemberWithExternalSettingError() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime expectedEndTime = startTime.plusSeconds(9);

        MemberFormDto memberFormDto = createMemberFormDto(1);
        when(memberServiceBase.create(any(MemberFormDto.class)))
                .thenThrow(CustomDataAccessException.class);

        assertThrows(RetryFailedException.class,
                () -> memberServiceWithRetry.create(memberFormDto)); // 10초 걸리게 만듦

        LocalDateTime actualEndTime = LocalDateTime.now();
        assertTrue(actualEndTime.isAfter(expectedEndTime));

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


}