package share_diary.diray.memberInviteHistory;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberInviteRequest {

    @NotNull
    private Long diaryRoomId;
    @NotNull
    private List<String> emails;

    public static MemberInviteRequest of(Long diaryRoomId, List<String> emails) {
        MemberInviteRequest request = new MemberInviteRequest();
        request.diaryRoomId = diaryRoomId;
        request.emails = emails;
        return request;
    }
}
