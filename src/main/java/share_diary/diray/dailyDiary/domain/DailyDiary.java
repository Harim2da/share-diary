package share_diary.diray.dailyDiary.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import share_diary.diray.diaryRoom.DiaryRoom;

@Table(name = "daily_diary")
@Entity
@Getter
public class DailyDiary {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    // 일기방
    @Column
    @ManyToOne
    private DiaryRoom diaryRoom;

    // 일기 내용
    @Column
    private String content;

    // 좋아요, 하트
//    @Column

    // 이모지
//    @Column

    // 삭제 상태 관리

    @Column
    private LocalDateTime createDate;

    @Column
    private Long createBy;

    @Column
    private LocalDateTime modifyDate;

    @Column
    private Long modifyBy;
}
