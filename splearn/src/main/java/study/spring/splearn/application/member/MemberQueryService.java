package study.spring.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.spring.splearn.application.member.provided.MemberFinder;
import study.spring.splearn.application.member.required.MemberRepository;
import study.spring.splearn.domain.member.Member;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

  private final MemberRepository memberRepository;

  @Override
  public Member find(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. id: " + memberId));
  }

}
