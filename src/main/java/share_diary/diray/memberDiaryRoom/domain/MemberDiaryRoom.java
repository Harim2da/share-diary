package share_diary.diray.memberDiaryRoom.domain;

import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.common.BaseTimeEntity;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.exception.memberDiaryRoom.AlreadyExitedDiaryRoomException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.Role;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_diary_room")
public class MemberDiaryRoom extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    //일기방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DiaryRoom diaryRoom;

    // 일기방 가입일
    @Column
    private LocalDate joinDate;

    // 일기방 탈퇴일
    @Column
    private LocalDate exitDate;

    public MemberDiaryRoom(Role role) {
        this.role = role;
    }

    public static MemberDiaryRoom from(Role role){
        return new MemberDiaryRoom(role);
    }

    //연관관계 편의 메소드
    public void addMember(Member member){
        if(this.member != null){
            this.member.getMemberDiaryRooms().remove(this);
        }
        this.member = member;
        member.getMemberDiaryRooms().add(this);
    }

    //연관관계 편의 메소드
    public void addDiaryRoom(DiaryRoom diaryRoom){
        if(this.diaryRoom!=null){
            this.diaryRoom.getMemberDiaryRooms().remove(this);
        }
        this.diaryRoom = diaryRoom;
        diaryRoom.getMemberDiaryRooms().add(this);
    }

    public static MemberDiaryRoom of(Member member, DiaryRoom diaryRoom, Role role) {
        MemberDiaryRoom instance = new MemberDiaryRoom();
        instance.addMember(member);
        instance.addDiaryRoom(diaryRoom);
        instance.role = role;
        instance.joinDate = LocalDate.now(); // zone 지정 관련 논의 필요
        return instance;
    }

    public MemberDiaryRoom exitDiaryRoom() {
        if(Objects.isNull(getExitDate())) {
            this.exitDate = LocalDate.now(); // zone 지정 관련 논의 필요
            return this;
        } else {
            throw new AlreadyExitedDiaryRoomException();
        }
    }

    public boolean isHost() {
        return getRole().isHost();
    }

    public void modifyHost() {
        this.role = Role.HOST;
    }
}
