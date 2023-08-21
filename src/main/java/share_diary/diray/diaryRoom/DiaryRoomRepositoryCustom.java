package share_diary.diray.diaryRoom;

import java.util.List;

public interface DiaryRoomRepositoryCustom {

    List<DiaryRoom> findAllByMemberIdWithMemberDiaryRoom(Long memberId);
}
