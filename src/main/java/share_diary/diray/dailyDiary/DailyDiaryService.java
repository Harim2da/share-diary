package share_diary.diray.dailyDiary;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.dailyDiary.dto.DailyDiaryDTO;
import share_diary.diray.dailyDiary.mapper.DailyDiaryMapper;
import share_diary.diray.exception.dailyDiary.InvalidRequestException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyDiaryService {

    private final MemberDiaryRoomRepository memberDiaryRoomRepository;
    private final DailyDiaryRepository dailyDiaryRepository;
    private final DailyDiaryMapper dailyDiaryMapper;
    private final MemberRepository memberRepository;

    public void createDailyDiary(Long memberId, DailyDiaryCreateModifyRequest request) {
        // 업로드할 일기방 속한 멤버인지 확인
        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByMemberId(
                memberId);

        if (CollectionUtils.isEmpty(memberDiaryRooms)) {
            throw new MemberNotFoundException();
        } else {
            // 존재하는 일기방에 저장
            for (Long diaryRoomId : request.getDiaryRooms()) {
                memberDiaryRooms.stream()
                        .filter(md -> diaryRoomId.equals(md.getDiaryRoom().getId()))
                        .filter(room -> room.getDiaryRoom().isOpen())
                        .findFirst()
                        .ifPresent(memberDiaryRoom ->
                                dailyDiaryRepository.save(DailyDiary.of(request.getContent(),
                                        memberDiaryRoom.getDiaryRoom(), request.getFeeling(),
                                        memberDiaryRoom.getMember().getLoginId()))
                        );
            }
        }
    }

    public DailyDiaryDTO modifyDailyDiary(Long diaryId, DailyDiaryCreateModifyRequest request, Long memberId) {
        // 하단에 member의 loginId를 뽑아 쓸 용도이자 연관관계를 꺼내 쓸 일이 없기 때문에 쿼리 메소드 사용
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 현재 수정 가능한 내용이 일기 내용과 공개/비공개 전환 뿐이기에 쿼리 메소드 사용
        DailyDiary diary = dailyDiaryRepository.findById(diaryId)
                .filter(dailyDiary -> dailyDiary.getCreateBy().equals(member.getLoginId()))
                .orElseThrow(InvalidRequestException::new);

        return dailyDiaryMapper.asDTO(diary.update(request));
    }
}
