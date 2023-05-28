package share_diary.diray.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import share_diary.diray.config.WebMvcConfig;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;
import share_diary.diray.member.dto.request.MemberUpdateRequestDTO;
import share_diary.diray.member.dto.response.MemberResponseDTO;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MemberController.class)
@MockBean({JpaMetamodelMappingContext.class, WebMvcConfig.class})
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
        mockMvc.perform(post(URL)
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
        memberRepository.save(member);

        MemberResponseDTO from = MemberResponseDTO.from(member);
        when(memberService.findMemberByEmail(any())).thenReturn(from);

        String email = "jipdol2@gamil.com";
        //expect
        mockMvc.perform(get(URL + "/me/id")
                        .param("email", email)
                ).andExpect(jsonPath("$.memberId").value("jipdol2"))
                .andExpect(jsonPath("$.email").value("jipdol2@gmail.com"))
                .andExpect(jsonPath("$.nickName").value("jipdol2"))
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
        memberRepository.save(member);

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
            String memberId,
            String email,
            String password,
            String nickName
    ) {
        return Member.builder()
                .memberId(memberId)
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
    }

    private MemberSignUpRequestDTO createMemberDTO(
            String memberId,
            String email,
            String password,
            String nickName
    ) {
        return MemberSignUpRequestDTO.of(memberId, email, password, nickName);
    }
}