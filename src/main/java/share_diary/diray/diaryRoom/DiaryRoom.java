package share_diary.diray.diaryRoom;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;

@Table(name = "diary_room")
@Entity
@Getter
public class DiaryRoom extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 방명
    @Column
    private String name;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DiaryRoomStatus status;

    @Column
    private String createBy;

    @Column
    private String modifyBy;

    @OneToMany(mappedBy = "diaryRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MemberDiaryRoom> memberDiaryRooms = new HashSet<>();

    @OneToMany(mappedBy = "diaryRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MemberInviteHistory> memberInviteHistories = new HashSet<>();

    public static DiaryRoom of(String name, String createBy) {
        DiaryRoom instance = new DiaryRoom();
        instance.name = name;
        instance.status = DiaryRoomStatus.OPEN;
        instance.createBy = createBy;
        instance.modifyBy = createBy;
        return instance;
    }

    public boolean isOpen() {
        return status.equals(DiaryRoomStatus.OPEN);
    }
}
