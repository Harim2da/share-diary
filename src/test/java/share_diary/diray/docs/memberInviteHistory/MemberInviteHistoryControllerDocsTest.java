package share_diary.diray.docs.memberInviteHistory;

import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import share_diary.diray.docs.RestDocsSupport;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.controller.MemberInviteHistoryController;
import share_diary.diray.memberInviteHistory.controller.request.InviteUpdateRequestDTO;
import share_diary.diray.memberInviteHistory.controller.request.MemberInviteRequestDTO;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberInviteHistoryControllerDocsTest extends RestDocsSupport {

    private final MemberInviteHistoryService memberInviteHistoryService = mock(MemberInviteHistoryService.class);

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
                        preprocessRequest(prettyPrint()),
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
                .andDo(document("memberInviteHistory-inviteRoomMembers",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("나의 초대내역을 조회한다.")
    void findByInviteHistory() throws Exception {
        //given

        //expected
        mockMvc.perform(get("/api/v0/member-invite-histories")
                        .param("inviteHistoryId", "1")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("memberInviteHistory-findByInviteHistory",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("inviteHistoryId")
                                        .description("마지막으로 조회된 초대내역 ID"),
                                parameterWithName("limit")
                                        .optional()
                                        .description("한 페이지에 조회되는 초대내역 개수")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("초대내역 ID"),
                                fieldWithPath("uuid").type(JsonFieldType.STRING)
                                        .description("초대내역 uuid"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("hostId").type(JsonFieldType.NUMBER)
                                        .description("일기방 방장 ID"),
                                fieldWithPath("hostUserNickname").type(JsonFieldType.STRING)
                                        .description("일기방 방장 닉네임"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("초대 내역 상태"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("초대 받은 사용자 ID"),
                                fieldWithPath("diaryRoomId").type(JsonFieldType.NUMBER)
                                        .description("일기방 ID"),
                                fieldWithPath("diaryRoomName").type(JsonFieldType.STRING)
                                        .description("일기방 이름"),
                                fieldWithPath("inviteDate").type(JsonFieldType.STRING)
                                        .description("초대한 날짜")
                        )
                ));
    }
}