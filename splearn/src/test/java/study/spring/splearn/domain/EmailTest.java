package study.spring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void equality() {
    var email1 = new Email("test@test.com");
    var email2 = new Email("test@test.com");

    assertThat(email1).isEqualTo(email2);
  }
}