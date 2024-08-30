//package com.example.shop2.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.example.shop2.constant.Role;
//import com.example.shop2.dto.MemberFormDto;
//import com.example.shop2.entity.Member;
//import com.example.shop2.exception.global.RetryFailedException;
//import com.example.shop2.repository.MemberRepository;
//import java.time.LocalDateTime;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
//class MemberServiceWithRetryTest {
//
////    @Mock
////    private MemberRepository memberRepository;
//
//    @Mock
//    private MemberService memberService;
//
//    @InjectMocks
//    private MemberServiceWithRetry memberServiceWithRetry;
//
//
//    @DisplayName("회원 등록 - 1-3. DB상의 문제가 발생해서 재시도 처리")
//    @Test
//    public void testCreatingMemberWithExternalSettingError() {
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime expectedEndTime = startTime.plusSeconds(9);
//
//        MemberFormDto memberFormDto = createMemberFormDto(1);
//        when(memberService.create(any(MemberFormDto.class)))
//                .thenReturn(null);
//
//        assertThrows(RetryFailedException.class,
//                () -> memberServiceWithRetry.create(memberFormDto)); // 10초 걸리게 만듦
//
//        LocalDateTime actualEndTime = LocalDateTime.now();
//        assertTrue(actualEndTime.isAfter(expectedEndTime));
//
//    }
//
//    private MemberFormDto createMemberFormDto(int i) {
//        var memberFormDto = new MemberFormDto();
//
//        memberFormDto.setEmail("test@gmail.com" + i);
//        memberFormDto.setAddress("서울시 강남구");
//        memberFormDto.setName("홍길동" + i);
//        memberFormDto.setPassword("testpassword" + i);
//        memberFormDto.setRole(Role.USER.name());
//
//        return memberFormDto;
//    }
//
//    private void assertSameMember(Member member, Member savedMember) {
//        assertTrue(member.getName().equals(savedMember.getName()) &&
//                member.getEmail().equals(savedMember.getEmail()) &&
//                member.getPassword().equals(savedMember.getPassword()) &&
//                member.getAddress().equals(savedMember.getAddress()) &&
//                member.getRole().equals(savedMember.getRole()));
//    }
//
//}