package com.example.shop2.service;

import static com.example.shop2.error.MemberErrorCode.DuplicatedEmail;
import static com.example.shop2.error.MemberErrorCode.EmptyRequiredValue;

import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.error.GlobalErrorCode;
import com.example.shop2.exception.global.EmptyRequiredValuesException;
import com.example.shop2.exception.global.RetryFailedException;
import com.example.shop2.exception.member.DuplicatedEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceWithRetry extends MemberService {

    private final int MAX_RETRIES = 10;
    private final int INIT_RETRIES = 1;
    private final int RETRY_DELAY = 1_000; // 1초

    private final MemberService memberService;

    public Member create(
            MemberFormDto memberFormDto) throws DuplicatedEmailException, EmptyRequiredValuesException, RetryFailedException {
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
        int retries = INIT_RETRIES;
        while (retries++ < MAX_RETRIES) {
            try {
                Member savedMember = this.memberService.create(memberFormDto);
                if (savedMember == null) {
                    throw new RuntimeException();
                }

                return savedMember;
            } catch (TransactionSystemException e) {
                log.debug("회원쪽에서 전달한 데이터에 필수 입력값이 누락되었습니다. 회원쪽에게 재요청하겠습니다.");
                log.debug(e.getMessage());
                e.printStackTrace();
                throw new EmptyRequiredValuesException(e, EmptyRequiredValue);

            } catch (DataIntegrityViolationException e) {
                log.debug("데이터 베이스 제약 조건 위배로 회원 등록에 실패했습니다. 회원쪽에게 재요청하겠습니다.");
                log.debug(e.getMessage());
                e.printStackTrace();
                throw new DuplicatedEmailException(e, DuplicatedEmail);

            } catch (RuntimeException e) {
                log.debug("회원 등록 중 예외 발생, %d 동안 대기했다가 재시도하겠습니다. [재시도 횟수 : %d]", RETRY_DELAY, retries);
                log.debug(e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException ex) {}
            }
        }

        log.debug("재시도를 했지만, 실패했습니다.");
        throw new RetryFailedException(null, GlobalErrorCode.RetryFailed);
    }
}
