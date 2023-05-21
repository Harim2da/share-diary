package share_diary.diray.diaryRoom;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Table(name = "diary_room")
@Entity
@Getter
public class DiaryRoom {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    // 방명
    @Column
    private String name;
    // 생성일자
    @Column
    private LocalDateTime createDate;
    // 생성자 - 확인 필요
    @Column
    private Long createBy;
    // 수정일자
    @Column
    private LocalDateTime modifyDate;
    // 수정자 - 확인 필요
    @Column
    private Long modifyBy;
}
