package share_diary.diray.memberDiaryRoom.domain;

import java.util.Optional;

public interface MemberDiaryRoomRepositoryCustom {

    Optional<MemberDiaryRoom> findByMemberIdAndDiaryRoomIdWithDiaryRoom(Long id, Long id1);
}
