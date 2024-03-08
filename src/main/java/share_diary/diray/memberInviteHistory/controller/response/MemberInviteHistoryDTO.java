package share_diary.diray.memberInviteHistory.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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

    public void updateHostUserNickname(String hostUserNickname){
        this.hostUserNickname = hostUserNickname;
    }

}