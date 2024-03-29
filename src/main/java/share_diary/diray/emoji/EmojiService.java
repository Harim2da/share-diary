package share_diary.diray.emoji;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import share_diary.diray.dailyDiary.domain.DailyDiaryRepository;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.domain.EmojiRepository;
import share_diary.diray.emoji.controller.response.DiaryEmojiDTO;
import share_diary.diray.emoji.mapper.EmojiMapper;
import share_diary.diray.exception.dailyDiary.DailyDiaryNotFoundException;
import share_diary.diray.exception.emoji.EmojiNotFoundException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;

import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmojiService {

    //repository
    private final MemberRepository memberRepository;
    private final DailyDiaryRepository diaryRepository;
    private final EmojiRepository emojiRepository;
    //mapper
    private final EmojiMapper emojiMapper;

    public DiaryEmojiDTO click(Long loginId, Long diaryId,String emojiType) {
        //다이어리 조회
        //만약 다이어리가 없다면,예외
        DailyDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DailyDiaryNotFoundException::new);
        //다이어리에 이모지가 없다면, 이모지 생성
        //입력한 이모지 count 진행
        //save 호출
        if (ObjectUtils.isEmpty(diary.getEmoji())) {
            Member member = memberRepository.findById(loginId)
                    .orElseThrow(MemberNotFoundException::new);
            Emoji emoji = Emoji.of();
            emoji.countEmoji(emojiType);
            emoji.addMember(member);
            emoji.addDailyDiary(diary);
            Emoji saveEmoji = emojiRepository.save(emoji);
            return emojiMapper.asDiaryEmojiDTO(saveEmoji);
        }
        //다이어리에 이모지가 있을때
        //입력한 이모지가 1 인 경우 0으로 변경
        //입력한 이모지가 0 인 경우 1로 변경
        Emoji findEmoji = diary.getEmoji().stream()
                .filter(emoji -> Objects.equals(emoji.getMember().getId(), loginId))
                .findFirst()
                .orElseThrow(EmojiNotFoundException::new);

        findEmoji.countEmoji(emojiType);
        return emojiMapper.asDiaryEmojiDTO(findEmoji);
    }

    @Transactional(readOnly = true)
    public DiaryEmojiDTO findByEmojiCount(Long diaryId){
        //다이어리 조회
        DailyDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DailyDiaryNotFoundException::new);
        //다이어리에 이모지가 없는 경우 (0,0,0,0,0) 리턴
        if(ObjectUtils.isEmpty(diary.getEmoji())){
            return new DiaryEmojiDTO();
        }
        //다이어리 id 로 group by 쿼리문 작성
        return emojiRepository.findBySumEmoji(diaryId);
    }
}
