package share_diary.diray.service.emoji;

import net.bytebuddy.asm.Advice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import share_diary.diray.DatabaseCleanup;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.dailyDiary.domain.DailyDiaryRepository;
import share_diary.diray.dailyDiary.domain.MyEmoji;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.domain.DiaryRoomRepository;
import share_diary.diray.emoji.EmojiService;
import share_diary.diray.emoji.controller.response.DiaryEmojiDTO;
import share_diary.diray.emoji.domain.EmojiRepository;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;
import share_diary.diray.service.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class EmojiServiceTest extends IntegrationTestSupport {

    //Database cleanUp
    @Autowired
    private DatabaseCleanup databaseCleanup;

    //Service
    @Autowired
    private EmojiService emojiService;

    //Repository
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DiaryRoomRepository diaryRoomRepository;
    @Autowired
    private MemberDiaryRoomRepository memberDiaryRoomRepository;
    @Autowired
    private DailyDiaryRepository dailyDiaryRepository;
    @Autowired
    private EmojiRepository emojiRepository;

    @AfterEach
    void tearDown(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("다른사람이 작성한 일기에 이모지를 선택할 수 있다.")
    void clickEmoji(){
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        String content = "나는 오늘 일기를 씁니다.";
        LocalDateTime time = LocalDateTime.of(2024,Month.MARCH,14,15,20);
        DailyDiary diary = DailyDiary.of(content,savedDiaryRoom, MyEmoji.BAD,savedMember.getNickName(),time);
        dailyDiaryRepository.save(diary);

        //when
        DiaryEmojiDTO diaryEmoji = emojiService.clickEmoji(savedMember.getId(), diary.getId(), "HEART");

        //then
        assertThat(diaryEmoji)
                .extracting("heart","thumb","party","cake","devil")
                .containsExactly(1L,0L,0L,0L,0L);
    }

    private Member createMember() {
        return Member.ofCreateMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "집돌2",
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