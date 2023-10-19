package share_diary.diray.emoji;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import share_diary.diray.emoji.domain.EmojiRepository;
import share_diary.diray.member.domain.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmojiService {

    private final MemberRepository memberRepository;
    private final EmojiRepository emojiRepository;

    public void click(Long loginId, Long diaryId){
        //다이어리 조회
            //만약 다이어리가 없다면,예외
        //다이어리에 이모지가 없다면, 이모지 생성
            //입력한 이모지 count 진행
            //save 호출
        //다이어리에 이모지가 있을때
            //입력한 이모지가 1 인 경우 0으로 변경
            //입력한 이모지가 0 인 경우 1로 변경
    }

    public void findByEmojiCount(){
        //다이어리에 이모지가 없는 경우 (0,0,0,0,0) 리턴
        //다이어리 id 로 group by 쿼리문 작성
    }
}
