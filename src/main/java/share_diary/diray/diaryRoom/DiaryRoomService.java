package share_diary.diray.diaryRoom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequestDTO;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomHostModifyRequestDTO;
import share_diary.diray.diaryRoom.controller.response.DiaryRoomMembersResponse;
import share_diary.diray.diaryRoom.controller.response.DiaryRoomDTO;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.domain.DiaryRoomRepository;
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

    public MemberDiaryRoom joinNewMember(Member member, DiaryRoom diaryRoom) {

        MemberDiaryRoom memberDiaryRoom = memberDiaryRoomRepository.findByMemberIdAndDiaryRoomIdWithDiaryRoomHost(member.getId(), diaryRoom.getId())
                .orElseGet(() -> memberDiaryRoomRepository.save(MemberDiaryRoom.of(member, diaryRoom, Role.USER)));
        return memberDiaryRoom;
    }

    public void createDiaryRoom(Long memberId, DiaryRoomCreateRequestDTO request, LocalDateTime now) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 멤버에서 뽑아낸 로그인 아이디
        DiaryRoom diaryRoom = DiaryRoom.of(request.getName(), member.getLoginId(), now);
        diaryRoomRepository.save(diaryRoom);

        memberDiaryRoomRepository.save(MemberDiaryRoom.of(member, diaryRoom, Role.HOST));

        // 만들어지고 나면 이벤트 처리로 멤버 초대 메일 발송하도록 하기
        if (!CollectionUtils.isEmpty(request.getEmails())) {
            publisher.publishEvent(new DiaryRoomCreateEvent(diaryRoom.getId(), member, request.getEmails()));
        }
    }

    @Transactional(readOnly = true)
    public List<DiaryRoomDTO> getDiaryRooms(Long memberId, Long lastDiaryId, int limit) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<DiaryRoom> diaryRooms = diaryRoomRepository.findAllByMemberIdWithMemberDiaryRoom(member.getId(), lastDiaryId, limit);

        return diaryRoomMapper.asDTOList(diaryRooms);
    }

    @Transactional(readOnly = true)
    public DiaryRoomMembersResponse getDiaryRoomMembers(Long diaryRoomId, String stringDate,
            Long memberId) {
        // TODO : DB 타임존 관련 체크 후 수정 필요
        LocalDate searchDate = LocalDate.parse(stringDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByDiaryRoomIdAndSearchDateWithMember(
                diaryRoomId, searchDate);

        // 내가 속한 방만 collect -> Size가 0이면 회원이 없다고 표기
        if (CollectionUtils.isEmpty(memberDiaryRooms)) {
            throw new MemberNotFoundException();
        }
        return new DiaryRoomMembersResponse().of(diaryRoomId, memberDiaryRooms);
    }

    public boolean deleteDiaryRoomMember(Long diaryRoomId, Long memberId) {
        LocalDate searchDate = LocalDate.now(); // zone 관련 수정 필요. 원래는 ZoneId로 가는 게 맞을 듯
        MemberDiaryRoom memberDiaryRoom = memberDiaryRoomRepository.findByDiaryRoomIdAndSearchDateAndMemberId(
                        diaryRoomId, searchDate, memberId)
                .orElseThrow(DiaryRoomNotFoundException::new);

        if (memberDiaryRoom.isHost()) {
            return false;
        } else {
            memberDiaryRoom.exitDiaryRoom();
            return true;
        }
    }

    public void modifyDiaryRoomHost(Long diaryRoomId, DiaryRoomHostModifyRequestDTO request, Long loginId) {

        // 일기방을 가져온 뒤에, 멤버 둘 다 있는지 확인 후 Host 변경, 현 host 탈퇴
        LocalDate searchDate = LocalDate.now(); // zone 관련 수정 필요. 원래는 ZoneId로 가는 게 맞을 듯
        // TODO : 리팩토링 가능하면 루프 두 번 돌리는 것 수정해보기
        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByDiaryRoomIdAndSearchDateWithMember(diaryRoomId, searchDate)
                .stream()
                .filter(md -> md.getMember().getId().equals(loginId) || md.getMember().getId().equals(request.getToBeHostId()))
                .collect(Collectors.toList());

        // 바뀔 host와 현 host 최소 2명은 있어야 함
        if (memberDiaryRooms.size() < 2) {
            throw new MemberNotFoundException();
        }

        for (MemberDiaryRoom md : memberDiaryRooms) {
            // 현재 Host인 경우
            if (md.getMember().getId().equals(loginId)) {
                md.exitDiaryRoom();
            }
            // 바뀔 Host인 경우
            if (md.getMember().getId().equals(request.getToBeHostId())) {
                md.modifyHost();
            }
        }
    }
}
