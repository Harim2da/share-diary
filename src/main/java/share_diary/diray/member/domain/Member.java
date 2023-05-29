package share_diary.diray.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member{

    @Id @GeneratedValue
    private Long id;

    private String memberId;

    private String email;

    private String password;

    private String nickName;

    private String joinStatus;

    private LocalDateTime joinTime;

    //회원,일기방 중간테이블
    @OneToMany(mappedBy = "member")
    private List<MemberDiaryRoom> memberDiaryRooms = new ArrayList<>();

    //맴버초대이력
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MemberInviteHistory> memberInviteHistories = new HashSet<>();

    //이모지
    @OneToMany(mappedBy = "member")
    private List<Emoji> emojis = new ArrayList<>();

    @Builder
    public Member(String memberId, String email, String password, String nickName, String joinStatus, LocalDateTime joinTime) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.joinStatus = joinStatus;
        this.joinTime = joinTime;
    }

    public void encryptPassword(String encryptPassword){
        this.password = encryptPassword;
    }

    public void updateMember(String password,String nickName){
        this.password = password;
        this.nickName = nickName;
    }
}
