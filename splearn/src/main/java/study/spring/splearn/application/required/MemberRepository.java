package study.spring.splearn.application.required;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import study.spring.splearn.domain.Email;
import study.spring.splearn.domain.Member;

/**
 * 회원 정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> {

  Member save(Member member);

  Optional<Member> findByEmail(Email email);

  Optional<Member> findById(Long memberId);
}
