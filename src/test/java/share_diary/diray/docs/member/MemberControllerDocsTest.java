package share_diary.diray.docs.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import share_diary.diray.docs.RestDocsSupport;
import share_diary.diray.member.MemberController;
import share_diary.diray.member.MemberService;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
}
