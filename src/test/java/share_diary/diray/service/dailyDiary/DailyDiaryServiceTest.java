package share_diary.diray.service.dailyDiary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import share_diary.diray.DatabaseCleanup;
import share_diary.diray.dailyDiary.DailyDiaryService;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequestDTO;
import share_diary.diray.dailyDiary.controller.response.DailyDiaryDTO;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.dailyDiary.domain.DailyDiaryRepository;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.domain.DiaryRoomRepository;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;
import share_diary.diray.service.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DailyDiaryServiceTest extends IntegrationTestSupport {

    //Database cleanUp
    @Autowired
    private DatabaseCleanup databaseCleanup;

    //Repository
    @Autowired
    private DailyDiaryRepository dailyDiaryRepository;
    @Autowired
    private DiaryRoomRepository diaryRoomRepository;
    @Autowired
    private MemberDiaryRoomRepository memberDiaryRoomRepository;
    @Autowired
    private MemberRepository memberRepository;

    //service
    @Autowired
    private DailyDiaryService dailyDiaryService;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("작성한 일기를 조회한다.")
    void getDailyDiary(){
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        MemberDiaryRoom memberDiaryRoom = MemberDiaryRoom.of(savedMember, savedDiaryRoom, Role.HOST);
        MemberDiaryRoom savedMemberDiaryRoom = memberDiaryRoomRepository.save(memberDiaryRoom);

        String content = "나는 오늘 일기를 작성합니다.";
        LocalDateTime now = LocalDateTime.of(2024, Month.DECEMBER, 13, 10,20);
        DailyDiary dailyDiary = DailyDiary.of(content, diaryRoom, MyEmoji.FUN, savedMember.getNickName(), now);
        DailyDiary diary = dailyDiaryRepository.save(dailyDiary);

        //when
        DailyDiaryDTO findDailyDiary = dailyDiaryService.getDailyDiary(savedMember.getId(), diaryRoom.getId(), now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), savedMember.getId());

        //then
        assertThat(findDailyDiary)
                .extracting("content","feeling","status")
                .containsExactlyInAnyOrder(content,MyEmoji.FUN,DiaryStatus.SHOW);
    }

    @Test
    @DisplayName("일기를 작성한다.")
    void writeDailyDiary() {
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        MemberDiaryRoom memberDiaryRoom = MemberDiaryRoom.of(savedMember, savedDiaryRoom, Role.HOST);
        MemberDiaryRoom savedMemberDiaryRoom = memberDiaryRoomRepository.save(memberDiaryRoom);

        String content = "나는 오늘 일기를 작성합니다.";
        DailyDiaryCreateModifyRequestDTO request = DailyDiaryCreateModifyRequestDTO.of(
                content,
                MyEmoji.FUN,
                List.of(savedDiaryRoom.getId()),
                DiaryStatus.SHOW
        );
        LocalDateTime now = LocalDateTime.of(2024,Month.FEBRUARY,10,11,30);

        //when
        dailyDiaryService.writeDailyDiary(savedMember.getId(),request,now);

        //then
        List<DailyDiary> diaries = dailyDiaryRepository.findAll();
        DailyDiary dailyDiary = diaries.get(0);

        assertThat(dailyDiary)
                .extracting("content","feeling","status","writeMember","writeDateTime")
                .containsExactlyInAnyOrder(
                        content,MyEmoji.FUN,DiaryStatus.SHOW,savedMember.getLoginId(),now
                );
    }

    @Test
    @DisplayName("작성한 일기를 수정한다.")
    void modifyDailyDiary(){
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        MemberDiaryRoom memberDiaryRoom = MemberDiaryRoom.of(savedMember, savedDiaryRoom, Role.HOST);
        MemberDiaryRoom savedMemberDiaryRoom = memberDiaryRoomRepository.save(memberDiaryRoom);

        String content = "나는 오늘 일기를 작성합니다.";
        LocalDateTime now = LocalDateTime.of(2024, Month.DECEMBER, 13, 10,20);
        DailyDiary dailyDiary = DailyDiary.of(content, diaryRoom, MyEmoji.FUN, savedMember.getNickName(), now);
        DailyDiary diary = dailyDiaryRepository.save(dailyDiary);

        String modifyContent = "나는 일기를 수정했습니다.";
        DailyDiaryCreateModifyRequestDTO request = DailyDiaryCreateModifyRequestDTO.of(modifyContent,MyEmoji.BAD,List.of(savedDiaryRoom.getId()),DiaryStatus.SHOW);

        //when
        dailyDiaryService.modifyDailyDiary(diary.getId(),request, savedMember.getId());

        //then
        List<DailyDiary> diaries = dailyDiaryRepository.findAll();
        assertThat(diaries).isNotEmpty();

        DailyDiary findDiary = diaries.get(0);
        assertThat(findDiary)
                .extracting("content","feeling","status")
                .containsExactlyInAnyOrder(modifyContent,MyEmoji.BAD,DiaryStatus.SHOW);
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

    private DiaryRoom createDiaryRoom() {
        return DiaryRoom.of(
                "testDiary",
                "jipdol2",
                LocalDateTime.of(2023, Month.DECEMBER, 28, 12, 30)
        );
    }

    private DailyDiary createDailyDiary(DiaryRoom diaryRoom) {
        return DailyDiary.of(
                "test 일기입니다",
                diaryRoom,
                MyEmoji.FUN,
                "jipdol2",
                LocalDateTime.now()
        );
    }
}