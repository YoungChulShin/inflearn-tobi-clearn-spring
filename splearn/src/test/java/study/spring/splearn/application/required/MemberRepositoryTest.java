package study.spring.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import study.spring.splearn.domain.Member;
import static study.spring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static study.spring.splearn.domain.MemberFixture.createPasswordEncoder;

@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @Test
  void createMember() {
    Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

    assertThat(member.getId()).isNull();

    memberRepository.save(member);

    assertThat(member.getId()).isNotNull();

    entityManager.flush();
  }

  @Test
  void duplicateEmailFail() {
    Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
    memberRepository.save(member);

    Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
    Assertions.assertThatThrownBy(() -> memberRepository.save(member2))
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}