package share_diary.diray.emoji.domain.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.domain.EmojiRepositoryCustom;

public class EmojiRepositoryCustomImpl extends QuerydslRepositorySupport implements
        EmojiRepositoryCustom {

    public EmojiRepositoryCustomImpl() {
        super(Emoji.class);
    }


}
