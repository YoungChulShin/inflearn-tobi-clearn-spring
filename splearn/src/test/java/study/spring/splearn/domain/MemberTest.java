package study.spring.splearn.domain;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static study.spring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static study.spring.splearn.domain.MemberFixture.createPasswordEncoder;

class MemberTest {

  Member member;
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    passwordEncoder = createPasswordEncoder();
    member = Member.register(createMemberRegisterRequest(), passwordEncoder);
  }

  @Test
  void registerMember() {
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void activate() {
    member.activate();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
  }

  @Test
  void activateFail() {
    member.activate();

    assertThatThrownBy(() -> {
      member.activate();
    }).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void deactivate() {
    member.activate();

    member.deactivate();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
  }

  @Test
  void deactivateFail() {
    assertThatThrownBy(() -> {
      member.deactivate();
    }).isInstanceOf(IllegalStateException.class);

    member.activate();
    member.deactivate();

    assertThatThrownBy(() -> {
      member.deactivate();
    }).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void verifyPassword() {
    assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
    assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
  }
  @Test
  void changeNickname() {
    assertThat(member.getNickname()).isEqualTo("shin");

    member.changeNickname("shin2");

    assertThat(member.getNickname()).isEqualTo("shin2");
  }

  @Test
  void changePassword() {
    member.changePassword("verysecret", passwordEncoder);

    assertThat(passwordEncoder.matches("verysecret", member.getPasswordHash())).isTrue();
  }

  @Test
  void isActive() {
    assertThat(member.isActive()).isFalse();

    member.activate();

    assertThat(member.isActive()).isTrue();

    member.deactivate();

    assertThat(member.isActive()).isFalse();
  }

  @Test
  void invalidEmail() {
    assertThatThrownBy(() ->
        Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder)
    ).isInstanceOf(IllegalArgumentException.class);
  }
}