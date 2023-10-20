package share_diary.diray.emoji.domain;

import share_diary.diray.emoji.dto.DiaryEmojiDTO;

public interface EmojiRepositoryCustom {

    DiaryEmojiDTO findBySumEmoji(Long diaryId);
}
