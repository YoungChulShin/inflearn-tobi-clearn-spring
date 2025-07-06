package study.spring.splearn.application.provided;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import study.spring.splearn.application.MemberService;
import study.spring.splearn.application.required.EmailSender;
import study.spring.splearn.application.required.MemberRepository;
import study.spring.splearn.domain.Email;
import study.spring.splearn.domain.Member;
import study.spring.splearn.domain.MemberFixture;
import study.spring.splearn.domain.MemberStatus;

class MemberRegisterTest {

  @Test
  void registerTestStub() {
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(),
        new EmailSenderStub(),
        MemberFixture.createPasswordEncoder());

    Member member = register.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void registerTestMock() {
    EmailSenderMock emailSenderMock = new EmailSenderMock();
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(),
        emailSenderMock,
        MemberFixture.createPasswordEncoder());

    Member member = register.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    assertThat(emailSenderMock.tos).hasSize(1);
    assertThat(emailSenderMock.tos.getFirst()).isEqualTo(member.getEmail());
  }

  @Test
  void registerTestMockito() {
    EmailSender emailSenderMock = Mockito.mock(EmailSender.class);

    MemberRegister register = new MemberService(
        new MemberRepositoryStub(),
        emailSenderMock,
        MemberFixture.createPasswordEncoder());

    Member member = register.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
  }

  static class MemberRepositoryStub implements MemberRepository {

    @Override
    public Member save(Member member) {
      ReflectionTestUtils.setField(member, "id", 1L);
      return member;
    }
  }

  static class EmailSenderStub implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {

    }
  }

  static class EmailSenderMock implements EmailSender {
    List<Email> tos = new ArrayList<>();

    @Override
    public void send(Email email, String subject, String body) {
      tos.add(email);
    }


  }
}