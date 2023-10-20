package share_diary.diray.emoji.domain.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.domain.EmojiRepositoryCustom;
import share_diary.diray.emoji.dto.DiaryEmojiDTO;

import java.util.List;

import static share_diary.diray.emoji.domain.QEmoji.emoji;

public class EmojiRepositoryCustomImpl extends QuerydslRepositorySupport implements
        EmojiRepositoryCustom {

    public EmojiRepositoryCustomImpl(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    public DiaryEmojiDTO findBySumEmoji(Long diaryId) {
        List<Emoji> fetch = from(emoji)
                .join(emoji.dailyDiary).fetchJoin()
                .groupBy(emoji.dailyDiary.id)
                .having(emoji.dailyDiary.id.eq(diaryId))
                .fetch();
        return null;
    }
}
