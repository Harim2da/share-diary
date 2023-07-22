package share_diary.diray.memberInviteHistory.domain;

import java.util.List;

public interface MemberInviteHistoryRepositoryCustom {

    List<MemberInviteHistory> findAllByEmailAndRoomIdWithMemberAndDiaryRoom(Long roomId, List<String> emails);
}
