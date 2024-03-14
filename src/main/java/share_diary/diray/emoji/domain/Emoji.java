package share_diary.diray.emoji.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.dailyDiary.domain.MyEmoji;
import share_diary.diray.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "emoji")
public class Emoji extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long heartEmojiNumber;
    private long thumbSupEmojiNumber;
    private long partyPopperEmojiNumber;
    private long cakeEmojiNumber;
    private long devilEmojiNumber;

    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    //일기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_diary_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DailyDiary dailyDiary;

    public Emoji(
            long heartEmojiNumber,
            long thumbSupEmojiNumber,
            long partyPopperEmojiNumber,
            long cakeEmojiNumber,
            long devilEmojiNumber
    ) {
        this.heartEmojiNumber = heartEmojiNumber;
        this.thumbSupEmojiNumber = thumbSupEmojiNumber;
        this.partyPopperEmojiNumber = partyPopperEmojiNumber;
        this.cakeEmojiNumber = cakeEmojiNumber;
        this.devilEmojiNumber = devilEmojiNumber;
    }

    public static Emoji of() {
        return new Emoji(0, 0, 0, 0, 0);
    }

    public static Emoji of(Member member,DailyDiary diary,String emojiType){
        Emoji emoji = Emoji.of();
        emoji.countEmoji(emojiType);
        emoji.addMember(member);
        emoji.addDailyDiary(diary);
        return emoji;
    }

    public void countEmoji(String name) {
        switch (name) {
            case "HEART":
                heartEmojiNumber = (heartEmojiNumber == 0) ? 1 : 0;
                break;
            case "THUMB":
                thumbSupEmojiNumber = (thumbSupEmojiNumber == 0) ? 1 : 0;
                break;
            case "PARTY":
                partyPopperEmojiNumber = (partyPopperEmojiNumber == 0) ? 1 : 0;
                break;
            case "CAKE":
                cakeEmojiNumber = (cakeEmojiNumber == 0) ? 1 : 0;
                break;
            case "DEVIL":
                devilEmojiNumber = (devilEmojiNumber == 0 ) ? 1 : 0;
                break;
        }
    }

    //연관관계 편의메소드
    public void addMember(Member member) {
        if (this.member != null) {
            this.member.getEmojis().remove(this);
        }
        this.member = member;
        member.getEmojis().add(this);
    }

    //연관관계 편의메소드
    public void addDailyDiary(DailyDiary dailyDiary) {
        if (this.dailyDiary != null) {
            this.dailyDiary.getEmoji().remove(this);
        }
        this.dailyDiary = dailyDiary;
        dailyDiary.getEmoji().add(this);
    }
}