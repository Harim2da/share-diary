package share_diary.diray.dailyDiary.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequestDTO;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.emoji.domain.Emoji;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_diary")
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

    //일기 작성자
    @Column
    private String writeMember;

    //작성 시간
    @Column
    private LocalDateTime writeDateTime;

    // 일기방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;

    // 이모지
    @OneToMany(mappedBy = "dailyDiary")
    private Set<Emoji> emoji = new HashSet<>();

    public DailyDiary(Long id, String content, MyEmoji feeling, DiaryStatus status, String writeMember, LocalDateTime writeDateTime, DiaryRoom diaryRoom, Set<Emoji> emoji) {
        this.id = id;
        this.content = content;
        this.feeling = feeling;
        this.status = status;
        this.writeMember = writeMember;
        this.writeDateTime = writeDateTime;
        this.diaryRoom = diaryRoom;
        this.emoji = emoji;
    }

    public static DailyDiary of(String content, DiaryRoom diaryRoom, MyEmoji feeling, String writeMember, LocalDateTime writeDateTime) {
        DailyDiary instance = new DailyDiary();
        instance.content = content;
        instance.status = DiaryStatus.SHOW;
        instance.feeling = feeling;
        instance.writeMember = writeMember;
        instance.writeDateTime = writeDateTime;
        instance.diaryRoom = diaryRoom;
        return instance;
    }

    public DailyDiary update(DailyDiaryCreateModifyRequestDTO request) {
        // 현재 수정 가능한 것이 두 케이스이므로 둘만 넣음. 추후 수정 가능한 것들이 늘어나면 아래 추가
        if (request.getContent() != null) {
            content = request.getContent();
        }
        if(request.getFeeling() != null){
            feeling = request.getFeeling();
        }
        if (request.getStatus() != null) {
            status = request.getStatus();
        }
        return this;
    }
}

