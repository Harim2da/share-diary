package share_diary.diray.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import share_diary.diray.auth.AuthService;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.config.WebMvcConfig;
import share_diary.diray.exception.member.PasswordNotCoincide;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.request.MemberPasswordRequestDTO;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;
import share_diary.diray.member.dto.request.MemberUpdateRequestDTO;
import share_diary.diray.member.dto.response.MemberResponseDTO;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MemberController.class)
@MockBean({JpaMetamodelMappingContext.class})
/**
 * JpaMetamodelMappingContext.class 를 mock 으로 지정해야하는 이유
 * https://velog.io/@cjh8746/%EC%97%90%EB%9F%AC-JPA-metamodel-must-not-be-empty-%ED%95%B4%EA%B2%B0%EA%B8%B0
 */
class MemberControllerTest {

    @Autowired
    private MemberController memberController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private AuthService authService;

    private static final String URL = "/api/member";

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        //given
        MemberSignUpRequestDTO member = createMemberDTO(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "jipdol2"
        );
        String json = objectMapper.writeValueAsString(member);

        //expect
        mockMvc.perform(post(URL+"/signUp")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findMemberIdTest() throws Exception {
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "jipdol2"
        );
//        memberRepository.save(member);

        MemberResponseDTO from = MemberResponseDTO.from(member);
        when(memberService.findMemberByEmail(any())).thenReturn(from);

        String email = "jipdol2@gamil.com";
        //expect
        mockMvc.perform(get(URL + "/me/id")
                        .param("email", email)
                ).andExpect(jsonPath("$.loginId").value("jipdol2"))
                .andExpect(jsonPath("$.email").value("jipdol2@gmail.com"))
                .andExpect(jsonPath("$.nickName").value("jipdol2"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 수정 전 비밀번호 확인 - 성공")
    void passwordCheckTest() throws Exception {
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "jipdol2"
        );
//        memberRepository.save(member);
        MemberPasswordRequestDTO memberPasswordRequestDTO = MemberPasswordRequestDTO.from("1234");
        String json = objectMapper.writeValueAsString(memberPasswordRequestDTO);

        //expected
        mockMvc.perform(post(URL + "/me/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    //출처 : https://stackoverflow.com/questions/54650555/exception-is-not-thrown-when-using-mockitos-dothrow-method#:~:text=If%20you%20have%20implemented%20an%20equals%20and%2For%20hashCode,recipe.setId%20%281%29%3B%20recipe.setName%20%28%22name%22%29%3B%20doThrow%20%28Exception.class%29.when%20%28recipeService%29.save%20%28recipe%29%3B
    @Test
    @DisplayName("회원 수정 전 비밀번호 확인 - 실패")
    void passwordCheckTestFail() throws Exception {
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "jipdol2"
        );
//        memberRepository.save(member);
        MemberPasswordRequestDTO memberPasswordRequestDTO = MemberPasswordRequestDTO.from("4444");
        String json = objectMapper.writeValueAsString(memberPasswordRequestDTO);

        doThrow(new PasswordNotCoincide()).when(memberService).passwordCheck(any(LoginSession.class),any(MemberPasswordRequestDTO.class));

        //expected
        mockMvc.perform(post(URL + "/me/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(status().isBadRequest())
                .andExpect(
                        (rslt)-> Assertions.assertTrue(rslt.getResolvedException().getClass().isAssignableFrom(PasswordNotCoincide.class))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void updateMemberTest() throws Exception {
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "jipdol2"
        );
//        memberRepository.save(member);

        MemberUpdateRequestDTO memberUpdateRequestDTO = MemberUpdateRequestDTO.of(
                "jipdol2@gmail.com",
                "4321",
                "4321",
                "jipsun2"
        );
        String json = objectMapper.writeValueAsString(memberUpdateRequestDTO);

        MemberResponseDTO memberResponseDTO = new MemberResponseDTO(
                "jipdol2",
                "jipdol2@gmail.com",
                "jipsun2"
        );
        when(memberService.updateMember(any())).thenReturn(memberResponseDTO);
        //expected
        mockMvc.perform(patch(URL + "/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(jsonPath("$.nickName").value("jipsun2"))
                .andDo(print());
    }

    private Member createMember(
            String loginId,
            String email,
            String password,
            String nickName
    ) {
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
    }

    private MemberSignUpRequestDTO createMemberDTO(
            String loginId,
            String email,
            String password,
            String nickName
    ) {
        return MemberSignUpRequestDTO.of(loginId, email, password, nickName);
    }
}