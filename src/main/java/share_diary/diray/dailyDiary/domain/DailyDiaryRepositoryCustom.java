package share_diary.diray.dailyDiary.domain;

import java.time.LocalDate;
import java.util.List;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.diaryRoom.domain.DiaryRoom;

public interface DailyDiaryRepositoryCustom {

    List<DailyDiary> findByLoginIdDiaryRoomIdAndSearchDate(String loginId, DiaryRoom diaryRoom, LocalDate searchingDate);
}
