package share_diary.diray.memberDiaryRoom.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberDiaryRoomRepositoryCustom {

    Optional<MemberDiaryRoom> findByMemberIdAndDiaryRoomIdWithDiaryRoomHost(Long memberId, Long diaryRoomId);

    List<MemberDiaryRoom> findAllByMemberId(Long memberId);

    List<MemberDiaryRoom> findAllByDiaryRoomIdAndSearchDateWithMember(Long diaryRoomId, LocalDate searchDate);

    Optional<MemberDiaryRoom> findByDiaryRoomIdAndSearchDateAndMemberId(Long diaryRoomId, LocalDate searchDate, Long memberId);
}
