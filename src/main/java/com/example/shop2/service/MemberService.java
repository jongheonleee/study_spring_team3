package com.example.shop2.service;

import static com.example.shop2.error.MemberErrorCode.*;

import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.exception.member.MemberDuplicatedEmailException;
import com.example.shop2.exception.member.MemberNotFoundException;
import com.example.shop2.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * - 1차 기능 목록
     *
     * - 회원 카운트 ✅
     * - 회원 모두 조회 ✅
     * - 회원 조회 -> 이메일, 아이디 ✅
     * - 회원 등록 ✅
     * - 회원 수정 ✅
     * - 회원 삭제 ✅
     *
     * > 추가 요구 사항
     * - 등록시 체크해야할 부분 이메일, 아이디
     * - 아이디/비밀번호 불일치
     * - 비밀번호/ 비빔번호 확인칸 비교
     *
     */

    // UsernameNotFoundException, MemberDuplicatedEmailException은 컨트롤러에서 처리해줄 수 있음
    // 그외의 런타임 예외는 처리가 어려움



    // 📌
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member foundMember = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder()
                .username(foundMember.getEmail())
                .password(foundMember.getPassword())
                .roles(foundMember.getRole().toString())
                .build();
    }

    public Long count() {
        return memberRepository.count();
    }


    public List<Member> readAll() {
        return memberRepository.findAll();
    }


    public Member readByEmail(String email) {
        Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(NotFoundMember));

        return foundMember;
    }

    public Member readById(Long id) {
        Member foundMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(NotFoundMember));

        return foundMember;
    }


    public Member create(Member member) throws Exception {
        // 이메일 중복 체크, 이메일이 중복되면 예외 던지기
        Optional<Member> duplicatedEmailMember = memberRepository.findByEmail(member.getEmail());

        if (duplicatedEmailMember.isPresent()) {
            throw new MemberDuplicatedEmailException(DuplicatedEmail);
        }

        // 중복되지 않으면 회원 등록
        Member createdMember = memberRepository.save(member);
        if (createdMember == null) {
            throw new Exception("회원 등록에 실패했습니다.");
        }

        return createdMember;
    }


    public void modify(MemberFormDto memberFormDto) throws Exception {
        // 해당 아이디의 회원이 있는지 확인
        Member foundMember = memberRepository.findByEmail(memberFormDto.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(NotFoundMember));

        // 없으면 예외 던지고 있으면 수정 처리
        foundMember.updateMember(memberFormDto);

        // 수정 처리 잘 됐는지 확인하고 그게 아니면 예외 발생
        Member updatedMember = memberRepository.findByEmail(foundMember.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(NotFoundMember));

        if (!updatedMember.equals(foundMember)) throw new Exception("회원 수정에 실패했습니다.");

    }


    public void remove(Long id) {
        Member foundMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(NotFoundMember));

        memberRepository.delete(foundMember);
    }

}
