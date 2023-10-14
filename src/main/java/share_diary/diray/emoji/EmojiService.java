package share_diary.diray.emoji;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.domain.EmojiRepository;
import share_diary.diray.emoji.domain.MyEmoji;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmojiService {

    private final MemberRepository memberRepository;
    private final EmojiRepository emojiRepository;

    public void saveMyEmoji(Long loginId, Long diaryId, MyEmoji myEmoji){
        //user get
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);

        //TODO : diary get
        //TODO : user == diary.user check
            //TODO : if not equals then throw exception

        //save
        Emoji emoji = Emoji.of(myEmoji,0,0,0,0,0);
        emoji.addMember(member);
        //TODO : add diary to emoji
        emojiRepository.save(emoji);
    }
}
