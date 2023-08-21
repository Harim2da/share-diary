package share_diary.diray.diaryRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiaryRoomDTO {

    protected Long id;
    protected String name;
    protected String createBy;
    protected String modifyBy;
}
