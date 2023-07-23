package share_diary.diray.diaryRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRoomRepository extends JpaRepository<DiaryRoom, Long>, DiaryRoomRepositoryCustom {
}
