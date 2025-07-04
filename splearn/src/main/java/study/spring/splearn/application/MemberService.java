package study.spring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.spring.splearn.application.provided.MemberRegister;
import study.spring.splearn.application.required.EmailSender;
import study.spring.splearn.application.required.MemberRepository;
import study.spring.splearn.domain.Member;
import study.spring.splearn.domain.MemberRegisterRequest;
import study.spring.splearn.domain.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

  private final MemberRepository memberRepository;
  private final EmailSender emailSender;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Member register(MemberRegisterRequest registerRequest) {
    // check

    Member member = Member.register(registerRequest, passwordEncoder);

    memberRepository.save(member);

    emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

    return member;
  }
}
