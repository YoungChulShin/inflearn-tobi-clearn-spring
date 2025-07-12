package study.spring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import study.spring.splearn.SplearnTestConfiguration;
import study.spring.splearn.domain.DuplicateEmailException;
import study.spring.splearn.domain.Member;
import study.spring.splearn.domain.MemberFixture;
import study.spring.splearn.domain.MemberRegisterRequest;
import study.spring.splearn.domain.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
record MemberRegisterTest(
    MemberRegister memberRegister,
    EntityManager entityManager) {

  @Test
  void register() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void duplicateEmailFail() {
    memberRegister.register(MemberFixture.createMemberRegisterRequest());

    assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
            .isInstanceOf(DuplicateEmailException.class);
  }

  @Test
  void activate() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.activate(member.getId());
    entityManager.flush();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
  }

  @Test
  void memberRegisterRequestFail() {
    invalidRequest(new MemberRegisterRequest("test@test.com", "shin", "secret"));
    invalidRequest(new MemberRegisterRequest("test@test.com", "shinyoungchul-----------", "longsecret"));
    invalidRequest(new MemberRegisterRequest("test.com", "shinyoungchul-----------", "longsecret"));
  }

  private void invalidRequest(MemberRegisterRequest invalid) {
    assertThatThrownBy(() -> memberRegister.register(invalid))
      .isInstanceOf(ConstraintViolationException.class);
  }

}
