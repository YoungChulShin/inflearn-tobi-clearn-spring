package study.spring.splearn.application.required;

import study.spring.splearn.domain.Email;

/**
 * 이메일을 발송한다.
 */
public interface EmailSender {

  void send(Email email, String subject, String body);

}
