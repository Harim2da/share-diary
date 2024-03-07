package share_diary.diray.docs.member;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        mockMvc.perform(patch("/api/v0/member-invite-histories/{historyId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("memberInviteHistory-inviteRoomMembers",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("초대 수락 여부(수락/거절)")
                        )
                ));
    }
}