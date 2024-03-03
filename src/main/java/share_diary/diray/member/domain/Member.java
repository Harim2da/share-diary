package share_diary.diray.member.domain;

import static javax.persistence.EnumType.STRING;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.member.controller.request.MemberSignUpRequestDTO;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String email;

    private String password;

    private String nickName;

    @Enumerated(value = STRING)
    private JoinStatus joinStatus;

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
    public Member(String loginId, String email, String password, String nickName, JoinStatus joinStatus, LocalDateTime joinTime) {
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.joinStatus = joinStatus;
        this.joinTime = joinTime;
    }

    public static Member ofCreateMember(String loginId, String email, String password, String nickName, JoinStatus joinStatus, LocalDateTime joinTime){
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .nickName(nickName)
                .joinStatus(joinStatus)
                .joinTime(joinTime)
                .build();
    }

    public void updatePassword(String encryptPassword){
        this.password = encryptPassword;
    }

    public void updateMember(String password,String nickName){
        this.password = password;
        this.nickName = nickName;
    }

    public static Member ofCreateInviteMember(String email) {
        Member instance = new Member();
        instance.email = email;
        instance.joinStatus = JoinStatus.WAITING;
        return instance;
    }

    public static List<Member> ofCreateInviteMembers(List<String> emails){
        return emails.stream()
                .map(Member::ofCreateInviteMember)
                .collect(Collectors.toList());
    }

    public void updateJoinMember(MemberSignUpRequestDTO requestDTO,String encodedPassword,LocalDateTime now) {
        this.loginId = requestDTO.getLoginId();
        this.password = encodedPassword;
        this.nickName = requestDTO.getNickName();
        this.joinStatus = JoinStatus.USER;
        this.joinTime = now;
    }
}
