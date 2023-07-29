package share_diary.diray.memberDiaryRoom.event.listener.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import share_diary.diray.diaryRoom.DiaryRoomService;
import share_diary.diray.memberDiaryRoom.event.listener.InviteAcceptEventListener;
import share_diary.diray.memberInviteHistory.event.InviteAcceptEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class InviteAcceptEventListenerImpl implements InviteAcceptEventListener {

    private final DiaryRoomService diaryRoomService;
    @Override
    public void handleInviteAcceptEvent(InviteAcceptEvent event) {
        diaryRoomService.joinNewMember(event.getMember(), event.getDiaryRoom());
    }
}
