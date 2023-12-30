package share_diary.diray.diaryRoom;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;
import share_diary.diray.diaryRoom.controller.response.DiaryRoomMembersResponse;
import share_diary.diray.diaryRoom.dto.DiaryRoomDTO;
import share_diary.diray.diaryRoom.event.DiaryRoomCreateEvent;
import share_diary.diray.diaryRoom.mapper.DiaryRoomMapper;
import share_diary.diray.exception.diaryRoom.DiaryRoomNotFoundException;
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
            publisher.publishEvent(new DiaryRoomCreateEvent(diaryRoom.getId(), member, request.getEmails()));
        }
    }

    @Transactional(readOnly = true)
    public List<DiaryRoomDTO> getDiaryRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<DiaryRoom> diaryRooms = diaryRoomRepository.findAllByMemberIdWithMemberDiaryRoom(member.getId());

        return diaryRoomMapper.asDTOList(diaryRooms);
    }

    @Transactional(readOnly = true)
    public DiaryRoomMembersResponse getDiaryRoomMembers(Long diaryRoomId, String stringDate, Long memberId) {
        // TODO : DB 타임존 관련 체크 후 수정 필요
        LocalDate searchDate = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 내 아이디를 빼고 조회한 뒤에, 내 아이디가 없는 방이면 예외를 내려야함
        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByDiaryRoomIdAndSearchDateWithMember(diaryRoomId, searchDate)
                .stream()
                .filter(md -> md.getMember().getId().equals(memberId))
                .collect(Collectors.toList());

        // 내가 속한 방만 collect -> Size가 0이면 회원이 없다고 표기
        if(CollectionUtils.isEmpty(memberDiaryRooms)) {
            throw new MemberNotFoundException();
        }
        return new DiaryRoomMembersResponse().of(diaryRoomId, memberDiaryRooms);
    }

    public void deleteDiaryRoomMember(Long diaryRoomId, Long memberId) {
        LocalDate searchDate = LocalDate.now(); // zone 관련 수정 필요. 원래는 ZoneId로 가는 게 맞을 듯
        memberDiaryRoomRepository.findByDiaryRoomIdAndSearchDateAndMemberId(diaryRoomId, searchDate, memberId)
                .map(MemberDiaryRoom::exitDiaryRoom)
                .orElseThrow(DiaryRoomNotFoundException::new);
    }
}
