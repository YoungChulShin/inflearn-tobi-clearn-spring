package study.spring.splearn.adapter.webapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static study.spring.splearn.AssertThatUtils.equalsTo;
import static study.spring.splearn.AssertThatUtils.notNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonPathValueAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import study.spring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import study.spring.splearn.application.member.provided.MemberRegister;
import study.spring.splearn.application.member.required.MemberRepository;
import study.spring.splearn.domain.member.Member;
import study.spring.splearn.domain.member.MemberFixture;
import study.spring.splearn.domain.member.MemberRegisterRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
public class MemberApiTest {

  final MockMvcTester mvcTester;
  final ObjectMapper objectMapper;
  final MemberRepository memberRepository;
  final MemberRegister memberRegister;

  @Test
  void register() throws JsonProcessingException, UnsupportedEncodingException {
    MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result = mvcTester.post().uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
        .exchange();

    Assertions.assertThat(result)
        .hasStatus(HttpStatus.OK)
        .bodyJson()
        .hasPathSatisfying("$.memberId", notNull())
        .hasPathSatisfying("$.email", equalsTo(request));

    MemberRegisterResponse response =
        objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
    Member member = memberRepository.findById(response.memberId()).orElseThrow();

    Assertions.assertThat(member.getEmail().address()).isEqualTo(request.email());
  }

  @Test
  void duplicateEmail() throws JsonProcessingException {
    memberRegister.register(MemberFixture.createMemberRegisterRequest());

    MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result = mvcTester.post().uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
        .exchange();

    Assertions.assertThat(result)
        .apply(print())
        .hasStatus(HttpStatus.CONFLICT);
  }
}