package share_diary.diray.memberInviteHistory.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.member.domain.Member;

@Getter
@AllArgsConstructor
public class InviteAcceptEvent {

    private Member member;
    private DiaryRoom diaryRoom;
}
