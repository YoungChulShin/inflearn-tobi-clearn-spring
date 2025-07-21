package study.spring.splearn.domain.member;

import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

  public static MemberRegisterRequest createMemberRegisterRequest(String email) {
    return new MemberRegisterRequest(email, "shinyoungchul", "longsecret");
  }

  public static MemberRegisterRequest createMemberRegisterRequest() {
    return createMemberRegisterRequest("test@test.com");
  }

  public static Member createMember(Long id) {
    Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
    ReflectionTestUtils.setField(member, "id", id);

    return member;
  }

  public static PasswordEncoder createPasswordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(String password) {
        return password.toUpperCase();
      }

      @Override
      public boolean matches(String password, String passwordHash) {
        return encode(password).equals(passwordHash);
      }
    };
  }
}
