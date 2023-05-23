package share_diary.diray.memberInviteHistory.domain;

import javax.persistence.*;

import lombok.Getter;
import share_diary.diray.member.domain.Member;

@Table(name = "member_invite_history")
@Entity
@Getter
public class MemberInviteHistory {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    // todo [하림] : 연관관계 추가 필요
    @Column
    @ManyToOne
    private Member member;

    @Column
    private String uuid;

    @Column
    private String email;

    @Column
    private InviteAcceptStatus status;


}
