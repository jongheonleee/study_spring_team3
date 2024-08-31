package com.example.shop2.service.base;

import static com.example.shop2.error.MemberErrorCode.*;
import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.exception.member.DuplicatedEmailException;
import com.example.shop2.exception.member.MemberNotFoundException;
import com.example.shop2.repository.MemberRepository;
import com.example.shop2.service.MemberService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MemberServiceBase implements MemberService {

    private MemberRepository memberRepository;

    public MemberServiceBase(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // 회원 등록
    @Override
    public Member create(MemberFormDto memberFormDto)
    {
        /**
         * - dto를 entity로 변환
         * - 중복되는 이메일이 존재하는지 확인
         *   - 중복되면, 예외 발생 -> DuplicatedEmailException
         * - 중복되지 않으면, 회원 등록 시도
         *    - 등록 했을때 예외가 발생하지 않으면 엔티티 반환
         *      - 엔티티가 null인 경우, 제대로 처리가 안됐기 때문에 예외 발생
         *      - 그게 아니면 그대로 엔티티 반환
         *  - 예외가 발생하면, MAX_RETRIES만큼 재시도
         *    - 재시도를 해도 실패하면 예외 발생 시키기
         *
         */
        System.out.println("MemberServiceBase.create");
        var member = Member.createMember(memberFormDto);
        checkDuplicatedEmail(memberFormDto);
        return memberRepository.save(member);
    }

    /**
     * - dto에서 이메일 조회
     * - 해당 이메일로 회원 조회
     *   - 없으면, 해당 회원이 존재하지 않는다고 예외 발생
     * - 있으면, 해당 회원의 비밀번호화 dto의 비밀번호 비교
     *   - 다르면, false
     *   - 같으면, true
     *
     */

    @Override
    public boolean isValidUser(MemberFormDto memberFormDto) {
        Member foundMember;

        try {
            foundMember = findMember(memberFormDto);
        } catch (MemberNotFoundException e) {
            log.debug("해당 회원이 존재하지 않습니다.");
            return false;
        }
        return isPasswordMatched(foundMember.getPassword(), memberFormDto.getPassword());
    }

    // 밑에는 서브 메서드들
    private Member findMember(MemberFormDto memberFormDto) {
        var foundMember = memberRepository.findByEmail(memberFormDto.getEmail());
        if (foundMember == null) {
            throw new MemberNotFoundException(null, NotFoundMember);
        }
        return foundMember;
    }

    private boolean isPasswordMatched(String foundMemberPassword, String memberFormDtoPassword) {
        return foundMemberPassword.equals(memberFormDtoPassword);
    }

    private void checkDuplicatedEmail(MemberFormDto memberFormDto) {
        Member foundDuplicatedEmailMember = memberRepository.findByEmail(memberFormDto.getEmail());

        if (foundDuplicatedEmailMember != null) {
            throw new DuplicatedEmailException(null, DuplicatedEmail);
        }
    }



}
