package share_diary.diray.emoji.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "emoji")
public class Emoji extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    //일기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="daily_diary_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DailyDiary dailyDiary;

    @Builder
    public Emoji(int angryEmojiNumber, int badEmojiNumber, int boringEmojiNumber, int goodEmojiNumber, int refreshEmojiNumber, int funEmojiNumber, int heartEmojiNumber, int thumbSupEmojiNumber, int partyPopperEmojiNumber, int cakeEmojiNumber, int devilEmojiNumber) {
        this.angryEmojiNumber = angryEmojiNumber;
        this.badEmojiNumber = badEmojiNumber;
        this.boringEmojiNumber = boringEmojiNumber;
        this.goodEmojiNumber = goodEmojiNumber;
        this.refreshEmojiNumber = refreshEmojiNumber;
        this.funEmojiNumber = funEmojiNumber;
        this.heartEmojiNumber = heartEmojiNumber;
        this.thumbSupEmojiNumber = thumbSupEmojiNumber;
        this.partyPopperEmojiNumber = partyPopperEmojiNumber;
        this.cakeEmojiNumber = cakeEmojiNumber;
        this.devilEmojiNumber = devilEmojiNumber;
    }

    //연관관계 편의메소드
    public void addMember(Member member){
        if(this.member != null){
            this.member.getEmojis().remove(this);
        }
        this.member = member;
        member.getEmojis().add(this);
    }

    //연관관계 편의메소드
    public void addDailyDiary(DailyDiary dailyDiary){
        if(this.dailyDiary != null){
            this.dailyDiary.getEmoji().remove(this);
        }
        this.dailyDiary = dailyDiary;
        dailyDiary.getEmoji().add(this);
    }
}