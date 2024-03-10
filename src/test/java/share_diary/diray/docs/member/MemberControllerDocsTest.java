package share_diary.diray.docs.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import share_diary.diray.common.email.CertificationNumber;
import share_diary.diray.docs.RestDocsSupport;
import share_diary.diray.member.MemberService;
import share_diary.diray.member.controller.MemberController;
import share_diary.diray.member.controller.request.*;
import share_diary.diray.member.controller.response.MemberDTO;
import share_diary.diray.member.domain.JoinStatus;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerDocsTest extends RestDocsSupport {

    //    mocking
    private final MemberService memberService = Mockito.mock(MemberService.class);

    private static final String SCHEME = "https";
    private static final String HOST = "itsdiary.store";
    private static final int PORT = 443;

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
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
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
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("email")
                                        .description("맴버 Email")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("맴버 ID"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("맴버 로그인 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("맴버 Email"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("맴버 Nickname"),
                                fieldWithPath("joinStatus").type(JsonFieldType.STRING)
                                        .description("맴버 가입 상태"),
                                fieldWithPath("joinTime").type(JsonFieldType.STRING)
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
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
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
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
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

    @Test
    @DisplayName("회원의 정보를 수정한다.")
    void updateMember() throws Exception {
        //given
        MemberUpdateRequestDTO request = MemberUpdateRequestDTO.of("jipdol2@gmail.com", "1234", "1234", "jipdol2");
        String json = objectMapper.writeValueAsString(request);

        given(memberService.updateMember(any()))
                .willReturn(MemberDTO.builder()
                        .id(1L)
                        .loginId("jipdol2")
                        .email("jipdol2@gmail.com")
                        .nickName("jipdol2")
                        .joinStatus(JoinStatus.USER.toString())
                        .joinTime(LocalDateTime.of(2024, Month.FEBRUARY, 25, 23, 0))
                        .build());

        //expected
        mockMvc.perform(patch("/api/member/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-updateMember",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Email"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Password"),
                                fieldWithPath("validationPassword").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("비밀번호 재확인 Password").type(JsonFieldType.STRING),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 Nickname")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("맴버 ID"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("맴버 로그인 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("맴버 Email"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("맴버 Nickname"),
                                fieldWithPath("joinStatus").type(JsonFieldType.STRING)
                                        .description("맴버 가입 상태"),
                                fieldWithPath("joinTime").type(JsonFieldType.STRING)
                                        .description("맴버 가입 시간")
                        ))
                );
    }

    @Test
    @DisplayName("회원가입 시에 아이디 중복체크를 실행한다.")
    void validationLoginId() throws Exception {
        //given
        MemberLoginIdRequestDTO request = MemberLoginIdRequestDTO.of("jipdol2");
        String json = objectMapper.writeValueAsString(request);

        given(memberService.validationMemberLoginId(any()))
                .willReturn(anyBoolean());

        //expected
        mockMvc.perform(post("/api/member/loginId/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-loginId-validation",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("회원가입 Id")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 code"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("응답 status"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 message"),
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                        .description("중복 여부(true : 중복 o ,false : 중복 x)")
                        ))
                );
    }

    @Test
    @DisplayName("회원가입 시에 이메일 중복체크를 실행한다.")
    void validationEmail() throws Exception {
        //given
        MemberEmailRequestDTO request = MemberEmailRequestDTO.of("jipdol2@gmail.com");
        String json = objectMapper.writeValueAsString(request);

        given(memberService.validationMemberEmail(any()))
                .willReturn(anyBoolean());

        //expected
        mockMvc.perform(post("/api/member/email/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-email-validation",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("회원가입 email")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 code"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("응답 status"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 message"),
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                        .description("중복 여부(true : 중복 o ,false : 중복 x)")
                        ))
                );
    }

    @Test
    @DisplayName("비밀번호 초기화를 하기 전제 email 로 인증번호를 전송한다.")
    void sendToCertificationNumber() throws Exception {
        //given
        MemberLoginIdAndEmailRequestDTO request = MemberLoginIdAndEmailRequestDTO.of("jipdol2", "jipdol2@gmail.com");
        String json = objectMapper.writeValueAsString(request);

        given(memberService.sendCertificationNumber(any()))
                .willReturn(anyInt());

        //expected
        mockMvc.perform(post("/api/member/certification-number")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-certification-number",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("회원가입 id"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("회원가입 email")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 code"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("응답 status"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 message"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("인증 번호")
                        ))
                );
    }

    @Test
    @DisplayName("유효한 인증번호를 입력했는지 확인한다.")
    void validationCertificationNumber() throws Exception {
        //given
        int certificationNumber = 470992;
        MemberCertificationNumberDTO request = MemberCertificationNumberDTO.of(certificationNumber);
        String json = objectMapper.writeValueAsString(request);

        given(memberService.validationCertificationNumber(certificationNumber))
                .willReturn(CertificationNumber.of(certificationNumber, 1L));

        //expected
        mockMvc.perform(post("/api/member/validation-certification-number")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-validation-certification-number",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("certificationNumber").type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("인증번호")
                        ),
                        responseFields(
                                fieldWithPath("certificationNumber").type(JsonFieldType.NUMBER)
                                        .description("인증번호"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("맴버 id")
                        ))
                );
    }

    @Test
    @DisplayName("비밀번호를 재설정 한다.")
    void resetPassword() throws Exception {
        //given
        MemberLoginIdAndPasswordRequestDTO request = MemberLoginIdAndPasswordRequestDTO.of("jipdol2", "1234");
        String json = objectMapper.writeValueAsString(request);

        doNothing().when(memberService).resetPassword(any());

        //expected
        mockMvc.perform(post("/api/member/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-resetPassword",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 id"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("맴버 password")
                        ))
                );
    }

    @Test
    @DisplayName("멤버 초대 uuid 유효성을 체크한다.")
    void validateMember() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();

        given(memberService.validateMember(any()))
                .willReturn(MemberDTO.builder()
                        .id(1L)
                        .loginId("jipdol2")
                        .email("jipdol2@gmail.com")
                        .nickName("jipdol2")
                        .joinStatus(JoinStatus.USER.toString())
                        .joinTime(LocalDateTime.of(2024, Month.FEBRUARY, 25, 23, 0))
                        .build());

        //expected
        mockMvc.perform(get("/api/member/uuid/{uuid}", uuid))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-uuid",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("맴버 ID"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("맴버 로그인 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("맴버 Email"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("맴버 Nickname"),
                                fieldWithPath("joinStatus").type(JsonFieldType.STRING)
                                        .description("맴버 가입 상태"),
                                fieldWithPath("joinTime").type(JsonFieldType.STRING)
                                        .description("맴버 가입 시간")
                        ))
                );
    }

    @Test
    @DisplayName("마이 페이지를 조회한다.")
    void findByMyInfo() throws Exception {
        //given
        given(memberService.findMemberById(any()))
                .willReturn(MemberDTO.builder()
                        .id(1L)
                        .loginId("jipdol2")
                        .email("jipdol2@gmail.com")
                        .nickName("jipdol2")
                        .joinStatus(JoinStatus.USER.toString())
                        .joinTime(LocalDateTime.of(2024, Month.FEBRUARY, 25, 23, 0))
                        .build());

        //expected
        mockMvc.perform(get("/api/member/myPage"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-mypage",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("맴버 ID"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("맴버 로그인 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("맴버 Email"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("맴버 Nickname"),
                                fieldWithPath("joinStatus").type(JsonFieldType.STRING)
                                        .description("맴버 가입 상태"),
                                fieldWithPath("joinTime").type(JsonFieldType.STRING)
                                        .description("맴버 가입 시간")
                        ))
                );
    }
}
