package share_diary.diray.dailyDiary.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.dailyDiary.DailyDiaryRepositoryCustom;
import share_diary.diray.dailyDiary.domain.DailyDiary;

public class DailyDiaryRepositoryCustomImpl extends QuerydslRepositorySupport implements DailyDiaryRepositoryCustom {

    public DailyDiaryRepositoryCustomImpl() {
        super(DailyDiary.class);
    }
}
