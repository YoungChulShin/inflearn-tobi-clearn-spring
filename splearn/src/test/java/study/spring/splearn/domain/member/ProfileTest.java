package study.spring.splearn.domain.member;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ProfileTest {

  @Test
  void profile() {
    new Profile("test123");
    new Profile("12345");
  }
  
  @Test
  void profileFail() {
    Assertions.assertThatThrownBy(() -> new Profile(""))
      .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> new Profile("A"))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> new Profile("프로필"))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> new Profile("toolongprofileaddress"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void url() {
    var profile = new Profile("shinyoungchul");

    assertThat(profile.url()).isEqualTo("@shinyoungchul");
  }

}