package study.spring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import study.spring.splearn.application.member.required.EmailSender;
import study.spring.splearn.domain.member.MemberFixture;
import study.spring.splearn.domain.member.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {

  @Bean
  public EmailSender emailSender() {
    return (email, subject, body) -> System.out.println("sending email: " + email);
  }

  @Bean
  @Primary
  public PasswordEncoder passwordEncoder() {
    return MemberFixture.createPasswordEncoder();
  }
}
