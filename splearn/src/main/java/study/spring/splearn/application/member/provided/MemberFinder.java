package study.spring.splearn.application.member.provided;

import study.spring.splearn.domain.member.Member;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {

  Member find(Long memberId);

}
