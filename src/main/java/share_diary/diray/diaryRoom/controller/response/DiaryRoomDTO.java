package share_diary.diray.diaryRoom.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import share_diary.diray.diaryRoom.domain.DiaryRoomStatus;

@Getter
@AllArgsConstructor
public class DiaryRoomDTO {

    protected Long id;
    protected String name;
    protected DiaryRoomStatus status;
    protected String createBy;
    protected String modifyBy;
}
