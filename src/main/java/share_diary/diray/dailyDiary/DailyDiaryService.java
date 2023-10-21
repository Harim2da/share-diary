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

    public void createDailyDiary(Long memberId, DailyDiaryCreateModifyRequest request) {
        // 업로드할 일기방 속한 멤버인지 확인
        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByMemberId(memberId);

        if(CollectionUtils.isEmpty(memberDiaryRooms)) {
            throw new MemberNotFoundException();
        } else {
            // 존재하는 일기방에 저장
            for(Long diaryRoomId : request.getDiaryRooms()) {
                memberDiaryRooms.stream()
                        .filter(md -> diaryRoomId.equals(md.getDiaryRoom().getId()))
                        .filter(room -> room.getDiaryRoom().isOpen())
                        .findFirst()
                        .ifPresent(memberDiaryRoom -> {
                            DailyDiary dailyDiary = DailyDiary.of(request.getContent(), memberDiaryRoom.getDiaryRoom(), request.getFeeling(),
                                    memberDiaryRoom.getMember().getLoginId());
                            dailyDiaryRepository.save(dailyDiary);
                        });
            }
        }
    }
}
