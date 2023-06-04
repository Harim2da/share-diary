package share_diary.diray.diaryRoom;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;

@Table(name = "diary_room")
@Entity
@Getter
public class DiaryRoom {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 방명
    @Column
    private String name;

    // 생성일자
    @Column
    private LocalDateTime createDate;
    // 생성자 - 확인 필요
    @Column
    private String createBy;
    // 수정일자
    @Column
    private LocalDateTime modifyDate;
    // 수정자 - 확인 필요
    @Column
    private String modifyBy;

//    @OneToMany(mappedBy = "diary_room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "diaryRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MemberDiaryRoom> memberDiaryRooms = new HashSet<>();

    @OneToMany(mappedBy = "diaryRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MemberInviteHistory> memberInviteHistories = new HashSet<>();
}
