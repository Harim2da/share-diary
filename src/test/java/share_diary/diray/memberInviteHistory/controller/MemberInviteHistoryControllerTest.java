package share_diary.diray.memberInviteHistory.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import share_diary.diray.auth.AuthService;
import share_diary.diray.auth.dto.request.LoginRequestDTO;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.diaryRoom.DiaryRoomRepository;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberInviteHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DiaryRoomRepository diaryRoomRepository;
    @Autowired
    AuthService authService;
    @Autowired
    MemberInviteHistoryRepository memberInviteHistoryRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String URL = "/api/v0/member-invite-histories";

    @Test
    @DisplayName("알림 내역 조회 API TEST")
    void findByInviteHistoryTest() throws Exception {
        //given
        Member memberA = memberRepository.save(createMember("jipdol2", "jipdol2@gmail.com", passwordEncoder.encode("password123"), "jipdol2"));
        Member memberB = memberRepository.save(createMember("jipsuni", "jipsuni@gmail.com", passwordEncoder.encode("password123"), "jipsuni"));
        DiaryRoom diaryRoom = diaryRoomRepository.save(DiaryRoom.of("First Room", memberA.getLoginId()));

        LoginRequestDTO from = LoginRequestDTO.from(memberB.getLoginId(), "password123");
        String token = authService.makeAccessToken(null, from);

        //when
        memberInviteHistoryRepository.save(MemberInviteHistory.of(memberB, diaryRoom, "jipsuni@gmail.com"));

        //then
        mockMvc.perform(get(URL)
                        .header(HttpHeaders.AUTHORIZATION,token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].email").value(memberB.getEmail()))
                .andExpect(jsonPath("$.result[0].status").value(InviteAcceptStatus.INVITE.toString()))
                .andExpect(jsonPath("$.result[0].memberId").value(memberB.getId()))
                .andExpect(jsonPath("$.result[0].diaryRoomId").value(diaryRoom.getId()))
                .andExpect(jsonPath("$.result[0].createDate").value(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.size").value(1))
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
}