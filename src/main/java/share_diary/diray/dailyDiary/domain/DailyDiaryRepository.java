package share_diary.diray.dailyDiary.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDiaryRepository extends JpaRepository<DailyDiary, Long>, DailyDiaryRepositoryCustom {

}
