package share_diary.diray.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String memberId;

    private String email;

    private String password;

    private String nickName;

    private String joinStatus;

    private LocalDateTime joinTime;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MemberInviteHistory> memberInviteHistories = new HashSet<>();
    @Builder
    public Member(String memberId, String email, String password, String nickName) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.joinStatus = "N";
        this.joinTime = LocalDateTime.now();
    }

    public void encryptPassword(String encryptPassword){
        this.password = encryptPassword;
    }
}
