package share_diary.diray.docs.member;

import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import share_diary.diray.docs.RestDocsSupport;
import share_diary.diray.member.MemberController;
import share_diary.diray.member.MemberService;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.dto.MemberDTO;
import share_diary.diray.member.dto.request.MemberPasswordRequestDTO;
import share_diary.diray.member.dto.request.MemberPasswordUpdateDTO;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerDocsTest extends RestDocsSupport {

    //    mocking
    private final MemberService memberService = Mockito.mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService);
    }

    @Test
    @DisplayName("회원가입(아이디,비밀번호,이메일,닉네임) 을 진행하는 API")
    void signUp() throws Exception {
        //given
        MemberSignUpRequestDTO request
                = MemberSignUpRequestDTO.of("jipdol2", "jipdol2@gmail.com", "1234", "jipdol2");

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(
                        post("/api/member/signUp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-signUp",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Email"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Password"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Nickname")
                        )
                ));
    }

    @Test
    @DisplayName("가입된 사용자의 아이디를 찾는다")
    void findMemberId() throws Exception {
        //given
        String param = "jipdol2@gmail.com";

        given(memberService.findMemberByEmail(any(String.class)))
                .willReturn(MemberDTO.builder()
                        .id(1L)
                        .loginId("jipdol2")
                        .email("jipdol2@gmail.com")
                        .nickName("jipdol2")
                        .joinStatus(JoinStatus.USER.toString())
                        .joinTime(LocalDateTime.of(2024, Month.FEBRUARY, 25, 23, 0))
                        .build()
                );

        //expected
        mockMvc.perform(
                        get("/api/member/me/id")
                                .param("email", param)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-findMemberId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("email")
                                        .description("맴버 Email")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("code"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("message"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("맴버 ID"),
                                fieldWithPath("data.loginId").type(JsonFieldType.STRING)
                                        .description("맴버 로그인 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("맴버 Email"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("맴버 Nickname"),
                                fieldWithPath("data.joinStatus").type(JsonFieldType.STRING)
                                        .description("맴버 가입 상태"),
                                fieldWithPath("data.joinTime").type(JsonFieldType.STRING)
                                        .description("맴버 가입 시간")
                        ))
                );
    }

    @Test
    @DisplayName("회원의 비밀번호가 일치한지 확인한다.")
    void passwordCheck() throws Exception {
        //given
        MemberPasswordRequestDTO request = MemberPasswordRequestDTO.from("1234");

        String json = objectMapper.writeValueAsString(request);

        doNothing().when(memberService).passwordCheck(anyLong(), any());

        //expected
        mockMvc.perform(post("/api/member/me/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-passwordCheck",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Password")
                        ))
                );
    }

    @Test
    @DisplayName("회원의 비밀번호를 변경한다.")
    void updatePassword() throws Exception {
        //given
        MemberPasswordUpdateDTO request = MemberPasswordUpdateDTO.of("1234", "4321");
        String json = objectMapper.writeValueAsString(request);

        doNothing().when(memberService).updatePassword(anyLong(), any());

        //expected
        mockMvc.perform(post("/api/member/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-updatePassword",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("기존 Password"),
                                fieldWithPath("updatePassword").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("변경할 Password")
                        ))
                );
    }
}
