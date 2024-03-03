package share_diary.diray.diaryRoom.domain;

import java.util.List;

public interface DiaryRoomRepositoryCustom {

    List<DiaryRoom> findAllByMemberIdWithMemberDiaryRoom(Long memberId, Long diaryId, int limit);
}
