package share_diary.diray.memberDiaryRoom.event.listener;

import org.springframework.context.event.EventListener;
import share_diary.diray.memberInviteHistory.event.InviteAcceptEvent;

public interface InviteAcceptEventListener {

    @EventListener
    void handleInviteAcceptEvent(InviteAcceptEvent event);
}
