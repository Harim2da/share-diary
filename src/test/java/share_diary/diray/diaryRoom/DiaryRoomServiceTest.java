package share_diary.diray.diaryRoom;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import share_diary.diray.DatabaseCleanup;
import share_diary.diray.diaryRoom.dto.DiaryRoomDTO;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DiaryRoomServiceTest {

    //Database cleanUp
    @Autowired
    private DatabaseCleanup databaseCleanup;

    //Repository
    @Autowired
    private DiaryRoomRepository diaryRoomRepository;
    @Autowired
    private MemberDiaryRoomRepository memberDiaryRoomRepository;
    @Autowired
    private MemberRepository memberRepository;

    //service
    @Autowired
    private DiaryRoomService diaryRoomService;

    @BeforeEach
    void tearDown() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("등록된 일기방들을 최근 생성된 순으로 조회한다. - page 0")
    void getDiaryRooms() {
        //given
        Member savedMember = memberRepository.save(createMember());

        List<DiaryRoom> insertDiaryRooms = IntStream.range(1, 11)
                .mapToObj(idx -> DiaryRoom.of("testDiary" + idx, "jipdol2", LocalDateTime.of(2023, Month.DECEMBER, idx, 12, 30)))
                .collect(Collectors.toList());

        List<DiaryRoom> savedAllDiaryRooms = diaryRoomRepository.saveAll(insertDiaryRooms);

        List<MemberDiaryRoom> memberDiaryRooms = savedAllDiaryRooms.stream()
                .map(diaryRoom -> MemberDiaryRoom.of(savedMember, diaryRoom, Role.HOST))
                .collect(Collectors.toList());

        memberDiaryRoomRepository.saveAll(memberDiaryRooms);

        int limit = 5;
        //when
        List<DiaryRoomDTO> diaryRooms = diaryRoomService.getDiaryRooms(savedMember.getId(), null, limit);

        //then
        assertThat(diaryRooms).isNotEmpty();
        assertThat(diaryRooms)
                .hasSize(5)
                .extracting("name", "status", "createBy", "modifyBy")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("testDiary10", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary9", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary8", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary7", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary6", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2")
                );
    }

    @Test
    @DisplayName("등록된 일기방들을 최근 생성된 순으로 조회한다. - page 1")
    void getDiaryNextPageRooms() {
        //given
        Member savedMember = memberRepository.save(createMember());

        List<DiaryRoom> insertDiaryRooms = IntStream.range(1, 11)
                .mapToObj(idx -> DiaryRoom.of("testDiary" + idx, "jipdol2", LocalDateTime.of(2023, Month.DECEMBER, idx, 12, 30)))
                .collect(Collectors.toList());

        List<DiaryRoom> savedAllDiaryRooms = diaryRoomRepository.saveAll(insertDiaryRooms);

        List<MemberDiaryRoom> memberDiaryRooms = savedAllDiaryRooms.stream()
                .map(diaryRoom -> MemberDiaryRoom.of(savedMember, diaryRoom, Role.HOST))
                .collect(Collectors.toList());

        memberDiaryRoomRepository.saveAll(memberDiaryRooms);

        int limit = 5;
        //when
        List<DiaryRoomDTO> diaryRooms = diaryRoomService.getDiaryRooms(savedMember.getId(), 6L,limit);

        //then
        assertThat(diaryRooms).isNotEmpty();
        assertThat(diaryRooms)
                .hasSize(5)
                .extracting("name", "status", "createBy", "modifyBy")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("testDiary5", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary4", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary3", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary2", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2"),
                        Tuple.tuple("testDiary1", DiaryRoomStatus.OPEN, "jipdol2", "jipdol2")
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
}