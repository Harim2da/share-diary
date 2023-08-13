package share_diary.diray.diaryRoom.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DiaryRoomCreateEvent {
    private Long diaryRoomId;
    private List<String> emails;
}
