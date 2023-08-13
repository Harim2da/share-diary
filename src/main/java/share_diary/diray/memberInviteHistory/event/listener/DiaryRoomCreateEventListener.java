package share_diary.diray.memberInviteHistory.event.listener;

import org.springframework.context.event.EventListener;
import share_diary.diray.diaryRoom.event.DiaryRoomCreateEvent;

public interface DiaryRoomCreateEventListener {

    @EventListener
    void handleDiaryRoomCreateEvent(DiaryRoomCreateEvent event);
}
