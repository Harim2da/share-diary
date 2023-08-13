package share_diary.diray.memberDiaryRoom.domain;

import java.util.List;
import java.util.Optional;

public interface MemberDiaryRoomRepositoryCustom {

    Optional<MemberDiaryRoom> findByMemberIdAndDiaryRoomIdWithDiaryRoom(Long memberId, Long diaryRoomId);

    List<MemberDiaryRoom> findAllByMemberId(Long memberId);
}
