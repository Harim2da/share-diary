package share_diary.diray.dailyDiary.domain.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.dailyDiary.domain.DailyDiaryRepositoryCustom;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import static share_diary.diray.dailyDiary.domain.QDailyDiary.dailyDiary;

public class DailyDiaryRepositoryCustomImpl extends QuerydslRepositorySupport implements DailyDiaryRepositoryCustom {

    public DailyDiaryRepositoryCustomImpl() {
        super(DailyDiary.class);
    }

    @Override
    public List<DailyDiary> findByLoginIdDiaryRoomIdAndSearchDate(
            String writeMember,
            DiaryRoom diaryRoom,
            LocalDate searchingDate
    ) {
        return from(dailyDiary)
                .where(
                        dailyDiary.diaryRoom.eq(diaryRoom)
                                .and(dailyDiary.writeMember.eq(writeMember))
                                .and(searchPeriodBetween(searchingDate))
                )
                .fetch();
    }

    private BooleanExpression searchPeriodBetween(LocalDate searchingDate) {
        if(searchingDate == null) {
            return null;
        }

        LocalDateTime start = LocalDateTime.of(searchingDate, LocalTime.of(0,0,0));
        LocalDateTime end = LocalDateTime.of(searchingDate, LocalTime.of(23,59,59));

        return dailyDiary.writeDateTime.between(start, end);
    }
}
