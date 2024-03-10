package share_diary.diray.docs.memberInviteHistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import share_diary.diray.docs.RestDocsSupport;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.controller.MemberInviteHistoryController;
import share_diary.diray.memberInviteHistory.controller.request.InviteUpdateRequestDTO;
import share_diary.diray.memberInviteHistory.controller.request.MemberInviteRequestDTO;
import share_diary.diray.memberInviteHistory.controller.response.MemberInviteHistoryDTO;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberInviteHistoryControllerDocsTest extends RestDocsSupport {

    private final MemberInviteHistoryService memberInviteHistoryService = Mockito.mock(MemberInviteHistoryService.class);

    private static final String SCHEME = "https";
    private static final String HOST = "itsdiary.store";
    private static final int PORT = 443;

    @Override
    protected Object initController() {
        return new MemberInviteHistoryController(memberInviteHistoryService);
    }

    @Test
    @DisplayName("사용자들을 일기방에 초대한다.")
    void inviteRoomMembers() throws Exception {
        //given
        List<String> emails = List.of("jipdol2@gmail.com", "jipsun2@gmail.com");
        MemberInviteRequestDTO request = MemberInviteRequestDTO.of(1L, emails, 1L);

        String json = objectMapper.writeValueAsString(request);

        doNothing().when(memberInviteHistoryService).inviteRoomMembers(any(), any());

        //expected
        mockMvc.perform(post("/api/v0/member-invite-histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("memberInviteHistory-inviteRoomMembers",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("diaryRoomId").type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("일기방 ID"),
                                fieldWithPath("emails").type(JsonFieldType.ARRAY)
                                        .optional()
                                        .description("초대한 사용자 Email들"),
                                fieldWithPath("hostId").type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("일기방 방장 ID")
                        )
                ));
    }

    @Test
    @DisplayName("일기방 초대를 수락/거절 한다.")
    void updateInviteHistory() throws Exception {
        //given
        InviteUpdateRequestDTO request = InviteUpdateRequestDTO.of(InviteAcceptStatus.ACCEPT);

        String json = objectMapper.writeValueAsString(request);

        doNothing().when(memberInviteHistoryService).updateInviteHistory(anyLong(), any());

        //expected
        mockMvc.perform(patch("/api/v0/member-invite-histories/{historyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("memberInviteHistory-updateInviteHistory",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("초대 상태(수락/거절)")
                        )
                ));
    }

    @Test
    @DisplayName("나의 초대내역을 조회한다.")
    void findByInviteHistory() throws Exception {
        //given
        List<MemberInviteHistoryDTO> list = List.of(
                MemberInviteHistoryDTO.builder()
                        .id(1L)
                        .uuid(UUID.randomUUID().toString())
                        .email("jipdol2@gmail.com")
                        .hostUserId(1L)
                        .hostUserNickname("gch03915")
                        .status(InviteAcceptStatus.INVITE)
                        .memberId(2L)
                        .diaryRoomId(1L)
                        .diaryRoomName("TEST 일기방")
                        .inviteDate(LocalDateTime.now())
                        .build()
        );

        given(memberInviteHistoryService.findByLoginUserInviteHistory(any(), anyLong(), anyInt()))
                .willReturn(list);

        //expected
        mockMvc.perform(get("/api/v0/member-invite-histories")
                        .param("inviteHistoryId", "1")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("memberInviteHistory-findByInviteHistory",
                        preprocessRequest(modifyUris()
                                .scheme(SCHEME)
                                .host(HOST)
                                .port(PORT), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("inviteHistoryId")
                                        .description("마지막으로 조회된 초대내역 ID"),
                                parameterWithName("limit")
                                        .optional()
                                        .description("한 페이지에 조회되는 초대내역 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 code"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 message"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("응답 status"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("초대내역 ID"),
                                fieldWithPath("data[].uuid").type(JsonFieldType.STRING)
                                        .description("초대내역 uuid"),
                                fieldWithPath("data[].email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data[].hostUserId").type(JsonFieldType.NUMBER)
                                        .description("일기방 방장 ID"),
                                fieldWithPath("data[].hostUserNickname").type(JsonFieldType.STRING)
                                        .description("일기방 방장 닉네임"),
                                fieldWithPath("data[].status").type(JsonFieldType.STRING)
                                        .description("초대 내역 상태"),
                                fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER)
                                        .description("초대 받은 사용자 ID"),
                                fieldWithPath("data[].diaryRoomId").type(JsonFieldType.NUMBER)
                                        .description("일기방 ID"),
                                fieldWithPath("data[].diaryRoomName").type(JsonFieldType.STRING)
                                        .description("일기방 이름"),
                                fieldWithPath("data[].inviteDate").type(JsonFieldType.STRING)
                                        .description("초대한 날짜")
                        )
                ));

//        verify(memberInviteHistoryService).findByLoginUserInviteHistory(any(), anyLong(), anyInt());
    }
}