package share_diary.diray.memberDiaryRoom.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.diaryRoom.DiaryRoom;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_diary_room")
public class MemberDiaryRoom {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;
}
