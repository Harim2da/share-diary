package share_diary.diray.dailyDiary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import share_diary.diray.dailyDiary.domain.DailyDiary;

@Repository
public interface DailyDiaryRepository extends JpaRepository<DailyDiary, Long> {

}
