package study.spring.splearn.adapter.integration;

import study.spring.splearn.application.required.EmailSender;
import study.spring.splearn.domain.Email;

public class DummyEmailSender implements EmailSender {

  @Override
  public void send(Email email, String subject, String body) {
    System.out.println("DummyEmailSender: send mail: " + email);
  }
}
