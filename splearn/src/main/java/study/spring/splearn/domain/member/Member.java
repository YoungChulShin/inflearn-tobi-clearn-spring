package study.spring.splearn.domain.member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import static org.springframework.util.Assert.state;
import study.spring.splearn.domain.AbstractEntity;
import study.spring.splearn.domain.shared.Email;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

  @NaturalId
  private Email email;

  private String nickname;

  private String passwordHash;

  private MemberStatus status;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private MemberDetail detail;

  public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
    Member member = new Member();

    member.email = new Email(registerRequest.email());
    member.nickname = requireNonNull(registerRequest.nickname());
    member.passwordHash =  requireNonNull(passwordEncoder.encode(registerRequest.password()));

    member.status = MemberStatus.PENDING;

    member.detail = MemberDetail.create();

    return member;
  }

  public void activate() {
    state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

    this.status = MemberStatus.ACTIVE;
    this.detail.activate();
  }

  public void deactivate() {
    state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

    this.status = MemberStatus.DEACTIVATED;
    this.detail.deactivate();
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

  public void updateInfo(MemberInfoUpdateRequest updateRequest) {
    this.nickname = Objects.requireNonNull(updateRequest.nickname());

    this.detail.updateInfo(updateRequest);
  }

  public boolean isActive() {
    return this.status == MemberStatus.ACTIVE;
  }
}
