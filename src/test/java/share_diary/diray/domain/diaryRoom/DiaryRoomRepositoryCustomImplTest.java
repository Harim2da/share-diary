package share_diary.diray.domain.diaryRoom;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.domain.DiaryRoomRepository;
import share_diary.diray.diaryRoom.domain.DiaryRoomStatus;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class DiaryRoomRepositoryCustomImplTest {

    @Autowired
    private DiaryRoomRepository diaryRoomRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDiaryRoomRepository memberDiaryRoomRepository;

    @AfterEach
    void tearDown(){
        diaryRoomRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("내가 속해 있는 일기방의 목록들을 limit 만큼 조회한다. - 무한 스크롤")
    void findAllByMemberIdWithMemberDiaryRoom(){
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        List<DiaryRoom> diaryRooms = createDiaryRooms();
        List<DiaryRoom> savedDiaryRooms = diaryRoomRepository.saveAll(diaryRooms);

        diaryRooms.forEach(diaryRoom -> memberDiaryRoomRepository.save(MemberDiaryRoom.of(member,diaryRoom, Role.USER)));

        int limit = 5;
        //when
        List<DiaryRoom> getMemberDiaryRooms = diaryRoomRepository.findAllByMemberIdWithMemberDiaryRoom(savedMember.getId(), null, limit);

        //then
        assertThat(getMemberDiaryRooms).isNotEmpty();
        assertThat(getMemberDiaryRooms)
                .extracting("name","status","registeredDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("test2Diary", DiaryRoomStatus.OPEN,getMemberDiaryRooms.get(0).getRegisteredDate()),
                        Tuple.tuple("test1Diary",DiaryRoomStatus.OPEN,getMemberDiaryRooms.get(1).getRegisteredDate())
                );
    }

    private Member createMember() {
        return Member.ofCreateMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "123",
                "jipdol2",
                JoinStatus.USER,
                LocalDateTime.now()
        );
    }

    private List<DiaryRoom> createDiaryRooms() {
        DiaryRoom diaryRoom1 = DiaryRoom.of(
                "test1Diary",
                "jipdol2",
                LocalDateTime.of(2023, Month.DECEMBER, 28, 12, 30)
        );
        DiaryRoom diaryRoom2 = DiaryRoom.of(
                "test2Diary",
                "jipdol2",
                LocalDateTime.of(2023, Month.DECEMBER, 30, 12, 30)
        );

        return List.of(diaryRoom1,diaryRoom2);
    }
}