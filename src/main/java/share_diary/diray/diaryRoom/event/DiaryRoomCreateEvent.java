package share_diary.diray.diaryRoom.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import share_diary.diray.member.domain.Member;

@Getter
@AllArgsConstructor
public class DiaryRoomCreateEvent {
    private Long diaryRoomId;
    private Member host;
    private List<String> emails;
}
