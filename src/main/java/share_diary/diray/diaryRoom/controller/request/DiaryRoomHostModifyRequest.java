package share_diary.diray.diaryRoom.controller.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DiaryRoomHostModifyRequest {

    private Long asIdHostId;
    @NotNull
    private Long toBeHostId;
}
