package com.example.shop2.service.expand;

import static com.example.shop2.error.GlobalErrorCode.RetryFailed;
import static com.example.shop2.error.MemberErrorCode.DuplicatedEmail;
import static com.example.shop2.error.MemberErrorCode.EmptyRequiredValue;

import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.exception.global.EmptyRequiredValuesException;
import com.example.shop2.exception.global.RetryFailedException;
import com.example.shop2.exception.member.DuplicatedEmailException;
import com.example.shop2.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

@Slf4j
public class MemberServiceWithRetry implements MemberService {

    private final int MAX_RETRIES = 10;
    private final int INIT_RETRIES = 1;
    private final int RETRY_DELAY = 1_000; // 1초

    private final MemberService memberService;

    public MemberServiceWithRetry(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public Member create(MemberFormDto memberFormDto)
            throws DuplicatedEmailException, EmptyRequiredValuesException, RetryFailedException {
        System.out.println("MemberServiceWithRetry.create");
        return retryWhenSomethingWrong(memberFormDto);
    }

    @Override
    public boolean isValidUser(MemberFormDto memberFormDto) {
        return this.memberService.isValidUser(memberFormDto);
    }

    private Member retryWhenSomethingWrong(MemberFormDto memberFormDto)
        throws DuplicatedEmailException, EmptyRequiredValuesException, RetryFailedException
    {

        int retries = INIT_RETRIES;

        while (retries++ < MAX_RETRIES) {
            try {
                return this.memberService.create(memberFormDto);
            } catch (TransactionSystemException e) {
                log.debug("회원쪽에서 전달한 데이터에 필수 입력값이 누락되었습니다. 회원쪽에게 재요청하겠습니다.");
                log.debug(e.getMessage());
                e.printStackTrace();
                throw new EmptyRequiredValuesException(e, EmptyRequiredValue);

            } catch (DataIntegrityViolationException e) {
                log.debug("데이터 베이스 제약 조건 위배로 회원 등록에 실패했습니다. 회원쪽에게 재요청하겠습니다.");
                log.debug(e.getMessage());
                e.printStackTrace();
                System.out.println("DuplicatedEmailExceptiondddddd");
                throw new DuplicatedEmailException(e, DuplicatedEmail);

            } catch (DuplicatedEmailException e) {
                System.out.println("DuplicatedEmailExceptiondddddd");
                throw e;
            }
            catch (DataAccessException e) {
                log.debug("회원 등록 중 예외 발생, %d 동안 대기했다가 재시도하겠습니다. [재시도 횟수 : %d]", RETRY_DELAY, retries);
                log.debug(e.getMessage());
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException ex) {}
            }
        }

        log.debug("재시도를 했지만, 실패했습니다.");
        throw new RetryFailedException(null, RetryFailed);
    }

}
