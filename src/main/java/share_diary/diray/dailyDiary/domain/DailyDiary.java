package share_diary.diray.dailyDiary.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.emoji.domain.Emoji;

@Table(name = "daily_diary")
@Entity
@Getter
public class DailyDiary {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일기 내용
    @Column
    private String content;

    // 삭제 상태 관리
    @Column
    private DiaryStatus status;

    @Column
    private LocalDateTime createDate;

    @Column
    private String createBy;

    @Column
    private LocalDateTime modifyDate;

    @Column
    private String modifyBy;

    // 일기방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;

    // 이모지
    @OneToMany(mappedBy = "dailyDiary")
    private Set<Emoji> emoji = new HashSet<>();
}
