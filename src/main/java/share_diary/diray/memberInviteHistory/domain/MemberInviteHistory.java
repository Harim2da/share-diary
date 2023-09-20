package share_diary.diray.memberInviteHistory.domain;

import static javax.persistence.EnumType.STRING;

import javax.persistence.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.exception.BaseException;
import share_diary.diray.exception.memberInviteHistory.AlreadyCheckedInviteException;
import share_diary.diray.exception.memberInviteHistory.InvalidInviteHistoryIdException;
import share_diary.diray.exception.memberInviteHistory.InvalidInviteUuidException;
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

    @Column
    private String hostUserId;

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

    public MemberInviteHistory updateAcceptStatus(InviteAcceptStatus status) {
        this.status = status;
        return this;
    }

    public boolean canUpdateStatus() {
        return this.status == InviteAcceptStatus.INVITE || this.status == InviteAcceptStatus.RE_INVITE;
    }

    public void validateUuid() {
        if (status.equals(InviteAcceptStatus.ACCEPT) || status.equals(InviteAcceptStatus.DENY)) {
            throw new AlreadyCheckedInviteException();
        } else if(status.equals(InviteAcceptStatus.RE_INVITE) || status.equals(InviteAcceptStatus.CANCEL)){
            throw new InvalidInviteUuidException();
        }
    }
}
