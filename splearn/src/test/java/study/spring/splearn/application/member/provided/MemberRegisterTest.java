package study.spring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import study.spring.splearn.SplearnTestConfiguration;
import study.spring.splearn.domain.member.*;

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
    Member member = registerMember();

    member = memberRegister.activate(member.getId());
    entityManager.flush();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    assertThat(member.getDetail().getActivatedAt()).isNotNull();
  }

  private Member registerMember() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();
    return member;
  }

  @Test
  void deactivate() {
    Member member = registerMember();

    member = memberRegister.activate(member.getId());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.deactivate(member.getId());

    assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
  }

  @Test
  void updateInfo() {
    Member member = registerMember();

    memberRegister.activate(member.getId());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("shintest2", "test2", "hello2"));

    assertThat(member.getDetail().getProfile().address()).isEqualTo("test2");
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
