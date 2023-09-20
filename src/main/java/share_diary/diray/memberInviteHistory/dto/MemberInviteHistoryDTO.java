package share_diary.diray.memberInviteHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

@Getter
@AllArgsConstructor
public class MemberInviteHistoryDTO {

    private Long id;
    private String uuid;
    private String email;
    private String hostUserId;
    private InviteAcceptStatus status;
    private Long memberId;
    private Long diaryRoomId;

}
