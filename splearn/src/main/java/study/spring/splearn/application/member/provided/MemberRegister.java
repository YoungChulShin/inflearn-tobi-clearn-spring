package study.spring.splearn.application.member.provided;

import jakarta.validation.Valid;
import study.spring.splearn.domain.member.Member;
import study.spring.splearn.domain.member.MemberRegisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다
 */
public interface MemberRegister {

  Member register(@Valid MemberRegisterRequest registerRequest);

  Member activate(Long memberId);

}
