package study.spring.splearn.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import static java.util.Objects.requireNonNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @NaturalId
  private Email email;

  private String nickname;

  private String passwordHash;

  @Enumerated(EnumType.STRING)
  private MemberStatus status;

  public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
    Member member = new Member();

    member.email = new Email(registerRequest.email());
    member.nickname = requireNonNull(registerRequest.nickname());
    member.passwordHash =  requireNonNull(passwordEncoder.encode(registerRequest.password()));

    member.status = MemberStatus.PENDING;

    return member;
  }

  public void activate() {
    state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

    this.status = MemberStatus.ACTIVE;
  }

  public void deactivate() {
    state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

    this.status = MemberStatus.DEACTIVATED;
  }

  public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, passwordHash);
  }

  public void changeNickname(String nickname) {
    this.nickname = requireNonNull(nickname);
  }

  public void changePassword(String password, PasswordEncoder passwordEncoder) {
    this.passwordHash = passwordEncoder.encode(requireNonNull(password));
  }

  public boolean isActive() {
    return this.status == MemberStatus.ACTIVE;
  }
}
