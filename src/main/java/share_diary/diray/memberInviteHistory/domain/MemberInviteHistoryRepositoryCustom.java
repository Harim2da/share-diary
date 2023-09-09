package share_diary.diray.memberInviteHistory.domain;

import java.util.List;
import java.util.Optional;

public interface MemberInviteHistoryRepositoryCustom {

    List<MemberInviteHistory> findAllByEmailAndRoomIdWithMemberAndDiaryRoom(Long roomId, List<String> emails);

    Optional<MemberInviteHistory> findByIdWithMemberAndDiaryRoom(Long historyId);

    Optional<MemberInviteHistory> findByUuidWithMember(String uuid);
}
