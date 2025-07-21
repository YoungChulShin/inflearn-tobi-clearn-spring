package study.spring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import study.spring.splearn.application.member.provided.MemberRegister;
import study.spring.splearn.domain.member.Member;
import study.spring.splearn.domain.member.MemberFixture;
import study.spring.splearn.domain.member.MemberRegisterRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiTest {

    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;

    @MockitoBean
    private MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException {
        Member member = MemberFixture.createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        Assertions.assertThat(
                mvcTester.post().uri("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail() throws JsonProcessingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email");
        String requestJson = objectMapper.writeValueAsString(request);

        Assertions.assertThat(
                        mvcTester.post().uri("/api/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

}