package study.spring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.spring.splearn.application.provided.MemberFinder;
import study.spring.splearn.application.provided.MemberRegister;
import study.spring.splearn.application.required.EmailSender;
import study.spring.splearn.application.required.MemberRepository;
import study.spring.splearn.domain.DuplicateEmailException;
import study.spring.splearn.domain.Email;
import study.spring.splearn.domain.Member;
import study.spring.splearn.domain.MemberRegisterRequest;
import study.spring.splearn.domain.PasswordEncoder;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {

  private final MemberFinder memberFinder;
  private final MemberRepository memberRepository;
  private final EmailSender emailSender;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Member register(MemberRegisterRequest registerRequest) {
    checkDuplicateEmail(registerRequest);

    Member member = Member.register(registerRequest, passwordEncoder);

    memberRepository.save(member);

    sendWelcomeEmail(member);

    return member;
  }

  @Override
  public Member activate(Long memberId) {
    Member member = memberFinder.find(memberId);

    member.activate();

    return memberRepository.save(member);
  }

  private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
    if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
      throw new DuplicateEmailException("이미 사용중인 이메일 입니다.");
    }
  }

  private void sendWelcomeEmail(Member member) {
    emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
  }
}
