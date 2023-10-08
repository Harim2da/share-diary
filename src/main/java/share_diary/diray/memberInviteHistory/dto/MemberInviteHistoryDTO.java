package share_diary.diray.memberInviteHistory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.time.LocalDateTime;

//@Setter
//@NoArgsConstructor
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
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

}