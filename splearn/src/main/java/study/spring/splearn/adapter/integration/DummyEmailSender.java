package study.spring.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import study.spring.splearn.application.member.required.EmailSender;
import study.spring.splearn.domain.shared.Email;

@Component
@Fallback
public class DummyEmailSender implements EmailSender {

  @Override
  public void send(Email email, String subject, String body) {
    System.out.println("DummyEmailSender: send mail: " + email);
  }
}
