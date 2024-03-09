package share_diary.diray.memberInviteHistory.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberInviteHistoryDTO {

    private Long id;
    private String uuid;
    private String email;
    private Long hostUserId;
    private String hostUserNickname;
    private InviteAcceptStatus status;
    private Long memberId;
    private Long diaryRoomId;
    private String diaryRoomName;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Seoul")
    private LocalDateTime inviteDate;

    @Builder
    public MemberInviteHistoryDTO(Long id, String uuid, String email, Long hostUserId, String hostUserNickname, InviteAcceptStatus status, Long memberId, Long diaryRoomId, String diaryRoomName, LocalDateTime inviteDate) {
        this.id = id;
        this.uuid = uuid;
        this.email = email;
        this.hostUserId = hostUserId;
        this.hostUserNickname = hostUserNickname;
        this.status = status;
        this.memberId = memberId;
        this.diaryRoomId = diaryRoomId;
        this.diaryRoomName = diaryRoomName;
        this.inviteDate = inviteDate;
    }

    public void updateHostUserNickname(String hostUserNickname){
        this.hostUserNickname = hostUserNickname;
    }

}
