package share_diary.diray.diaryRoom.controller.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DiaryRoomHostModifyRequestDTO {

    private Long asIsHostId;
    @NotNull
    private Long toBeHostId;
}
