package study.spring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import study.spring.splearn.SplearnTestConfiguration;
import study.spring.splearn.domain.Member;
import study.spring.splearn.domain.MemberFixture;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(
    MemberFinder memberFinder,
    MemberRegister memberRegister,
    EntityManager entityManager) {

  @Test
  void find() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();

    Member found = memberFinder.find(member.getId());

    assertThat(found.getId()).isEqualTo(member.getId());
  }

  @Test
  void findFail() {
    Assertions.assertThatThrownBy(() -> memberFinder.find(999L))
      .isInstanceOf(IllegalArgumentException.class);
  }


}