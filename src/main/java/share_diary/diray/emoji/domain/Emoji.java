package share_diary.diray.emoji.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "emoji")
public class Emoji {

    @Id @GeneratedValue
    private Long id;

    private int angryEmojiNumber;

    private int badEmojiNumber;

    private int boringEmojiNumber;

    private int goodEmojiNumber;

    private int refreshEmojiNumber;

    private int funEmojiNumber;

    private int heartEmojiNumber;

    private int thumbSupEmojiNumber;

    private int partyPopperEmojiNumber;

    private int cakeEmojiNumber;

    private int devilEmojiNumber;
}