package share_diary.diray.memberInviteHistory.controller.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberInviteRequestDTO {

    @NotNull
    private Long diaryRoomId;
    @NotNull
    private List<String> emails;
    private Long hostId;

    public static MemberInviteRequestDTO of(Long diaryRoomId, List<String> emails, Long hostId) {
        MemberInviteRequestDTO request = new MemberInviteRequestDTO();
        request.diaryRoomId = diaryRoomId;
        request.emails = emails;
        request.hostId = hostId;
        return request;
    }

    public void updateHostId(Long hostId) {
        this.hostId = hostId;
    }
}
