package share_diary.diray.memberInviteHistory.domain.impl;

import static share_diary.diray.member.domain.QMember.member;
import static share_diary.diray.memberInviteHistory.domain.QMemberInviteHistory.memberInviteHistory;
import static share_diary.diray.diaryRoom.QDiaryRoom.diaryRoom;

import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepositoryCustom;

public class MemberInviteHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements
        MemberInviteHistoryRepositoryCustom {

    public MemberInviteHistoryRepositoryCustomImpl() {
        super(MemberInviteHistory.class);
    }

    @Override
    public List<MemberInviteHistory> findAllByEmailAndRoomIdWithMemberAndDiaryRoom(Long roomId,
            List<String> emails) {
        return from(memberInviteHistory)
                .join(memberInviteHistory.member, member).fetchJoin()
                .join(memberInviteHistory.diaryRoom, diaryRoom).fetchJoin()
                .where(
                        memberInviteHistory.diaryRoom.id.eq(roomId)
                                .and(member.email.in(emails)))
                .distinct()
                .fetch();
    }
}
