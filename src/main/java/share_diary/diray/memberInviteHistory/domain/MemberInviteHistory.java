package share_diary.diray.memberInviteHistory.domain;

import static javax.persistence.EnumType.STRING;

import javax.persistence.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.member.domain.Member;

import java.util.UUID;

@Table(name = "member_invite_history")
@Entity
@Getter
@Slf4j
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
    @Enumerated(value = STRING)
    private InviteAcceptStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;


    public static MemberInviteHistory of(Member member, DiaryRoom diaryRoom, String email) {
        MemberInviteHistory instance = new MemberInviteHistory();
        instance.uuid = UUID.randomUUID().toString();
        instance.email = email;
        instance.status = InviteAcceptStatus.INVITE;
        instance.member = member;
        instance.diaryRoom = diaryRoom;
        return instance;
    }

    public static MemberInviteHistory reInvite(Member member, MemberInviteHistory beforeHistory) {
        beforeHistory.status = InviteAcceptStatus.RE_INVITE;
        return of(member, beforeHistory.getDiaryRoom(), member.getEmail());
    }

}
