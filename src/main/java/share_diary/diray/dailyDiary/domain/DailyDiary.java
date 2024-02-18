package share_diary.diray.dailyDiary.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.emoji.domain.Emoji;

@Table(name = "daily_diary")
@Entity
@Getter
public class DailyDiary extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일기 내용
    @Column
    private String content;

    /**
     * MyEmoji enum type 선언 (jipdol2)
     */
    @Enumerated(value = EnumType.STRING)
    private MyEmoji feeling;

    // 삭제 상태 관리
    @Column
    @Enumerated(value = EnumType.STRING)
    private DiaryStatus status;

/*    @Column
    private LocalDateTime createDate;

    @Column
    private String createBy;

    @Column
    private LocalDateTime modifyDate;

    @Column
    private String modifyBy;*/

    // 일기방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;

    // 이모지
    @OneToMany(mappedBy = "dailyDiary")
    private Set<Emoji> emoji = new HashSet<>();

    public static DailyDiary of(String content, DiaryRoom diaryRoom, MyEmoji feeling, String createBy) {
        DailyDiary instance = new DailyDiary();
        instance.content = content;
        instance.status = DiaryStatus.SHOW;
        instance.feeling = feeling;
        instance.diaryRoom = diaryRoom;
//        instance.createBy = createBy;
//        instance.modifyBy = createBy;
        return instance;
    }

    public DailyDiary update(DailyDiaryCreateModifyRequest request) {
        // 현재 수정 가능한 것이 두 케이스이므로 둘만 넣음. 추후 수정 가능한 것들이 늘어나면 아래 추가
        if (request.getContent() != null) {
            content = request.getContent();
        }
        if (request.getStatus() != null) {
            status = request.getStatus();
        }

        return this;
    }
}
