package share_diary.diray.memberInviteHistory.domain;

import static javax.persistence.EnumType.STRING;

import javax.persistence.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.exception.memberInviteHistory.AlreadyCheckedInviteException;
import share_diary.diray.exception.memberInviteHistory.InvalidInviteUuidException;
import share_diary.diray.member.domain.Member;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "member_invite_history")
@Entity
@Getter
@Slf4j
public class MemberInviteHistory extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String uuid;

    @Column
    private String email;

    @Column
    @Enumerated(value = STRING)
    private InviteAcceptStatus status;

    @Column
    private Long hostUserId;

    @Column
    private LocalDateTime inviteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DiaryRoom diaryRoom;


    public static MemberInviteHistory of(Member member, DiaryRoom diaryRoom, String email, Long hostUserId,LocalDateTime inviteDate) {
        MemberInviteHistory instance = new MemberInviteHistory();
        instance.uuid = UUID.randomUUID().toString();
        instance.email = email;
        instance.status = InviteAcceptStatus.INVITE;
        instance.member = member;
        instance.diaryRoom = diaryRoom;
        instance.hostUserId = hostUserId;
        instance.inviteDate = inviteDate;
        return instance;
    }

    public static MemberInviteHistory reInvite(Member member, MemberInviteHistory beforeHistory,LocalDateTime inviteDate) {
//        beforeHistory.status = InviteAcceptStatus.RE_INVITE;
        MemberInviteHistory afterHistory = of(member, beforeHistory.getDiaryRoom(), member.getEmail(),
                beforeHistory.getHostUserId(), inviteDate);
        afterHistory.status = InviteAcceptStatus.RE_INVITE;
        return afterHistory;
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
