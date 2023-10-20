package share_diary.diray.emoji;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import share_diary.diray.dailyDiary.DailyDiaryRepository;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.domain.EmojiRepository;
import share_diary.diray.emoji.dto.DiaryEmojiDTO;
import share_diary.diray.exception.dailyDiary.DailyDiaryNotFoundException;
import share_diary.diray.exception.diaryRoom.DiaryRoomNotFoundException;
import share_diary.diray.exception.emoji.EmojiNotFoundException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmojiService {

    private final MemberRepository memberRepository;
    private final DailyDiaryRepository diaryRepository;
    private final EmojiRepository emojiRepository;

    public void click(Long loginId, Long diaryId,String emojiType) {
        //다이어리 조회
        //만약 다이어리가 없다면,예외
        DailyDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DailyDiaryNotFoundException::new);
        //다이어리에 이모지가 없다면, 이모지 생성
        //입력한 이모지 count 진행
        //save 호출
        if (diary.getEmoji() == null) {
            Member member = memberRepository.findById(loginId)
                    .orElseThrow(MemberNotFoundException::new);
            Emoji emoji = Emoji.of();
            emoji.countEmoji(emojiType);
            emoji.addMember(member);
            emoji.addDailyDiary(diary);
            emojiRepository.save(emoji);
            return;
        }
        //다이어리에 이모지가 있을때
        //입력한 이모지가 1 인 경우 0으로 변경
        //입력한 이모지가 0 인 경우 1로 변경
        Emoji findEmoji = diary.getEmoji().stream()
                .filter(emoji -> Objects.equals(emoji.getMember().getId(), loginId))
                .findFirst()
                .orElseThrow(EmojiNotFoundException::new);

        findEmoji.countEmoji(emojiType);
    }

    public DiaryEmojiDTO findByEmojiCount(Long diaryId){
        //다이어리 조회
        DailyDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DailyDiaryNotFoundException::new);
        //다이어리에 이모지가 없는 경우 (0,0,0,0,0) 리턴
        if(Objects.isNull(diary.getEmoji())){
            return new DiaryEmojiDTO();
        }
        //다이어리 id 로 group by 쿼리문 작성
        emojiRepository.findBySumEmoji(diaryId);
        return null;
    }
}
