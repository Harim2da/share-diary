package share_diary.diray.dailyDiary;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.domain.EmojiRepository;
import share_diary.diray.exception.emoji.EmojiNotFoundException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyDiaryService {

    private final MemberDiaryRoomRepository memberDiaryRoomRepository;
    private final DailyDiaryRepository dailyDiaryRepository;
    private final EmojiRepository emojiRepository;

    public void createDailyDiary(Long memberId, DailyDiaryCreateModifyRequest request) {
        // 업로드할 일기방 속한 멤버인지 확인
        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByMemberId(memberId);

        if(CollectionUtils.isEmpty(memberDiaryRooms)) {
            throw new MemberNotFoundException();
        } else {
            // 이모지 찾기 - 이모지는 신규 추가가 많지 않음
            Emoji todayEmoji = emojiRepository.findAll()
                    .stream()
                    .filter(e -> e.getId().equals((long)request.getEmojiNum()))
                    .findFirst()
                    .orElseThrow(EmojiNotFoundException::new);

            for(Long diaryRoomId : request.getDiaryRooms()) {
                for(MemberDiaryRoom md : memberDiaryRooms) {
                    if(diaryRoomId.equals(md.getDiaryRoom().getId())) {
                        // 일기 생성 후 save
                        DailyDiary diary = dailyDiaryRepository.save(
                                DailyDiary.of(request.getContent(), md.getDiaryRoom(),
                                        md.getMember().getLoginId()));
                        // 이모지에도 추가
                        todayEmoji.addDailyDiary(diary);
                    }
                }
            }
        }
    }
}
