package share_diary.diray.diaryRoom;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;
import share_diary.diray.diaryRoom.dto.DiaryRoomDTO;
import share_diary.diray.diaryRoom.event.DiaryRoomCreateEvent;
import share_diary.diray.diaryRoom.mapper.DiaryRoomMapper;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiaryRoomService {

    private final MemberDiaryRoomRepository memberDiaryRoomRepository;
    private final DiaryRoomRepository diaryRoomRepository;
    private final ApplicationEventPublisher publisher;
    private final MemberRepository memberRepository;
    private final DiaryRoomMapper diaryRoomMapper;

    public void joinNewMember(Member member, DiaryRoom diaryRoom) {
        Optional<MemberDiaryRoom> memberDiaryRoom = memberDiaryRoomRepository.findByMemberIdAndDiaryRoomIdWithDiaryRoom(member.getId(), diaryRoom.getId());
        if(Objects.isNull(memberDiaryRoom)) {
            memberDiaryRoomRepository.save(MemberDiaryRoom.of(member, diaryRoom, Role.USER));
        }
    }

    public void createDiaryRoom(Long memberId, DiaryRoomCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 멤버에서 뽑아낸 로그인 아이디
        DiaryRoom diaryRoom = DiaryRoom.of(request.getName(), member.getLoginId());
        diaryRoomRepository.save(diaryRoom);

        memberDiaryRoomRepository.save(MemberDiaryRoom.of(member, diaryRoom, Role.HOST));

        // 만들어지고 나면 이벤트 처리로 멤버 초대 메일 발송하도록 하기
        if(!CollectionUtils.isEmpty(request.getEmails())) {
            publisher.publishEvent(new DiaryRoomCreateEvent(diaryRoom.getId(), request.getEmails()));
        }
    }

    @Transactional(readOnly = true)
    public List<DiaryRoomDTO> getDiaryRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<DiaryRoom> diaryRooms = diaryRoomRepository.findAllByMemberIdWithMemberDiaryRoom(member.getId());

        return diaryRoomMapper.asDTOList(diaryRooms);
    }
}
