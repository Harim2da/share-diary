package share_diary.diray.memberInviteHistory.domain;

import javax.persistence.*;

import lombok.Getter;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.member.domain.Member;

@Table(name = "member_invite_history")
@Entity
@Getter
public class MemberInviteHistory {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String uuid;

    @Column
    private String email;

    @Column
    private InviteAcceptStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;
}
