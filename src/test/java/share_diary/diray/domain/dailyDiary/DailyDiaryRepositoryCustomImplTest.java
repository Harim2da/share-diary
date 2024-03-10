package share_diary.diray.domain.dailyDiary;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.dailyDiary.domain.DailyDiaryRepository;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.domain.DiaryRoomRepository;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class DailyDiaryRepositoryCustomImplTest {

    @Autowired
    private DailyDiaryRepository dailyDiaryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DiaryRoomRepository diaryRoomRepository;

    @Test
    @DisplayName("지정 날짜에 맞는 일기를 조회 한다.")
    void findByLoginIdDiaryRoomIdAndSearchDate(){
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        String content = "나는 오늘 일기를 작성합니다.";
        LocalDateTime now = LocalDateTime.of(2024, Month.FEBRUARY,10,11,30);
        DailyDiary dailyDiary = DailyDiary.of(content, diaryRoom, MyEmoji.FUN, savedMember.getLoginId(), now);
        dailyDiaryRepository.save(dailyDiary);

        String content2 = "나는 오늘 일기를 작성합니다.";
        LocalDateTime now2 = LocalDateTime.of(2024, Month.FEBRUARY,15,11,30);
        DailyDiary dailyDiary2 = DailyDiary.of(content, diaryRoom, MyEmoji.FUN, savedMember.getLoginId(), now);
        dailyDiaryRepository.save(dailyDiary);

        //when
        List<DailyDiary> diaries = dailyDiaryRepository.findByLoginIdDiaryRoomIdAndSearchDate(savedMember.getLoginId(), diaryRoom, now.toLocalDate());

        //then
        assertThat(diaries).isNotEmpty();
        assertThat(diaries)
                .hasSize(1)
                .extracting("content","feeling","status","writeMember","writeDateTime")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(content,MyEmoji.FUN,DiaryStatus.SHOW,savedMember.getLoginId(),now)
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

    private DiaryRoom createDiaryRoom() {
        return DiaryRoom.of(
                "testDiary",
                "jipdol2",
                LocalDateTime.of(2023, Month.DECEMBER, 28, 12, 30)
        );
    }
}