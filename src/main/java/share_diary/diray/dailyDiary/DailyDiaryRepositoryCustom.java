package share_diary.diray.dailyDiary;

import java.time.LocalDate;
import java.util.List;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.diaryRoom.DiaryRoom;

public interface DailyDiaryRepositoryCustom {

    List<DailyDiary> findByLoginIdDiaryRoomIdAndSearchDate(String loginId, DiaryRoom diaryRoom, LocalDate searchingDate);
}
