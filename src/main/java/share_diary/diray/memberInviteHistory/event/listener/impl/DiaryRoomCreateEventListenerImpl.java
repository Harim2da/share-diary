package share_diary.diray.memberInviteHistory.event.listener.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import share_diary.diray.diaryRoom.event.DiaryRoomCreateEvent;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.MemberInviteRequest;
import share_diary.diray.memberInviteHistory.event.listener.DiaryRoomCreateEventListener;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryRoomCreateEventListenerImpl implements DiaryRoomCreateEventListener {

    private final MemberInviteHistoryService memberInviteHistoryService;

    @Override
    public void handleDiaryRoomCreateEvent(DiaryRoomCreateEvent event) {
        LocalDateTime now = LocalDateTime.now();
        memberInviteHistoryService.inviteRoomMembers(MemberInviteRequest.of(event.getDiaryRoomId(), event.getEmails(), event.getHost().getId()),now);
    }
}
