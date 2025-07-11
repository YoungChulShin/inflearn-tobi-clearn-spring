package study.spring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import study.spring.splearn.application.required.EmailSender;
import study.spring.splearn.domain.MemberFixture;
import study.spring.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {

  @Bean
  public EmailSender emailSender() {
    return (email, subject, body) -> System.out.println("sending email: " + email);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return MemberFixture.createPasswordEncoder();
  }
}
