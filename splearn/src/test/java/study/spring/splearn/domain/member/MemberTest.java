package study.spring.splearn.domain.member;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static study.spring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static study.spring.splearn.domain.member.MemberFixture.createPasswordEncoder;

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
    assertThat(member.getDetail().getRegisteredAt()).isNotNull();
  }

  @Test
  void activate() {
    member.activate();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    assertThat(member.getDetail().getActivatedAt()).isNotNull();
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
    assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
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
    assertThat(member.verifyPassword("longsecret", passwordEncoder)).isTrue();
    assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
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

  @Test
  void updateInfo() {
    member.activate();

    var request = new MemberInfoUpdateRequest("shinyoungchul2", "profile2", "introduction2");
    member.updateInfo(request);

    assertThat(member.getNickname()).isEqualTo(request.nickname());
    assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
    assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
  }

  @Test
  void updateInfoFail() {
    assertThatThrownBy(() -> {
      var request = new MemberInfoUpdateRequest("shinyoungchul2", "profile2", "introduction2");
      member.updateInfo(request);
    }).isInstanceOf(IllegalStateException.class);
  }
}