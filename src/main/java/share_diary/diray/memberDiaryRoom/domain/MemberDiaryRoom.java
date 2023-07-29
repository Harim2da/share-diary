package share_diary.diray.memberDiaryRoom.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.Role;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_diary_room")
public class MemberDiaryRoom {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //일기방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_room_id", referencedColumnName = "id")
    private DiaryRoom diaryRoom;

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

    public static MemberDiaryRoom of(Member member, DiaryRoom diaryRoom) {
        MemberDiaryRoom instance = new MemberDiaryRoom();
        instance.addMember(member);
        instance.addDiaryRoom(diaryRoom);
        instance.role = Role.USER;
        return instance;
    }
}
