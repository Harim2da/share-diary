package share_diary.diray.memberInviteHistory;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import share_diary.diray.member.domain.Member;

@Getter
public class MemberInviteRequest {

    @NotNull
    private Long diaryRoomId;
    @NotNull
    private List<String> emails;
    private Long hostId;

    public static MemberInviteRequest of(Long diaryRoomId, List<String> emails, Long hostId) {
        MemberInviteRequest request = new MemberInviteRequest();
        request.diaryRoomId = diaryRoomId;
        request.emails = emails;
        request.hostId = hostId;
        return request;
    }

    public void updateHostId(Long hostId) {
        this.hostId = hostId;
    }
}
