package share_diary.diray.emoji.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import share_diary.diray.emoji.dto.DiaryEmojiDTO;

public interface EmojiRepository extends JpaRepository<Emoji,Long>,
    EmojiRepositoryCustom{

    @Query("SELECT new share_diary.diray.emoji.dto.DiaryEmojiDTO(" +
                "SUM(e.heartEmojiNumber)," +
                "SUM(e.thumbSupEmojiNumber)," +
                "SUM(e.partyPopperEmojiNumber), " +
                "SUM(e.cakeEmojiNumber), " +
                "SUM(e.devilEmojiNumber)) " +
            "FROM DailyDiary d " +
                "INNER JOIN d.emoji e " +
            "GROUP BY d.id " +
            "HAVING d.id = :diaryId")
    DiaryEmojiDTO findBySumEmoji(@Param("diaryId") Long diaryId);
}
