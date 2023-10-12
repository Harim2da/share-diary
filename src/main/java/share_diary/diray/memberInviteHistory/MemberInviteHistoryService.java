package share_diary.diray.memberInviteHistory;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.common.email.EmailSenderComponent;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.diaryRoom.DiaryRoomRepository;
import share_diary.diray.exception.diaryRoom.DiaryRoomNotFoundException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.exception.memberInviteHistory.InvalidInviteHistoryIdException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;
import share_diary.diray.memberInviteHistory.dto.MemberInviteHistoryDTO;
import share_diary.diray.memberInviteHistory.event.InviteAcceptEvent;
import share_diary.diray.memberInviteHistory.mapper.MemberInviteHistoryMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberInviteHistoryService {
    private final MemberRepository memberRepository;
    private final MemberInviteHistoryRepository memberInviteHistoryRepository;
    private final EmailSenderComponent emailSenderComponent;
    private final ApplicationEventPublisher publisher;
    private final MemberInviteHistoryMapper inviteHistoryMapper;
    private final MemberDiaryRoomRepository memberDiaryRoomRepository;

    public void inviteRoomMembers(MemberInviteRequest request) {
        // 일기방 검증 및 host 체크
        MemberDiaryRoom memberDiaryRoom = memberDiaryRoomRepository.findByMemberIdAndDiaryRoomIdWithDiaryRoom(request.getHostId(),
                request.getDiaryRoomId())
                .stream()
                .filter(m -> m.getRole().isHost())
                .findFirst()
                .orElseThrow(DiaryRoomNotFoundException::new);

        DiaryRoom diaryRoom = memberDiaryRoom.getDiaryRoom();

        // 멤버에서 이메일로 조회해오기 -> 일치하는 멤버가 없으면 임시 회원가입 필요
        List<Member> members = memberRepository.findAllByEmail(request.getEmails());
        // 멤버 초대 이력 가져오기
        List<MemberInviteHistory> histories = memberInviteHistoryRepository.findAllByEmailAndRoomIdWithMemberAndDiaryRoom(request.getDiaryRoomId(), request.getEmails());

        for (String email : request.getEmails()) {
            Member member = members.stream()
                    .filter(m -> email.equals(m.getEmail()))
                    .findFirst()
                    .orElse(null);

            MemberInviteHistory thisInviteHistory;

            // 일기방에 해당 메일 초대 이력 있는지 확인 필요
            if(Objects.nonNull(member)) {
                MemberInviteHistory beforeInviteHistory = histories.stream()
                        .filter(memberHistory -> member.getId().equals(memberHistory.getMember().getId()))
                        .findFirst()
                        .orElse(null);

                if(Objects.nonNull(beforeInviteHistory)) {
                    // 메일 재발송 - 기존 초대 상태 Reinvite로 변경 -> Invite 하나 더 쌓기
                    thisInviteHistory = MemberInviteHistory.reInvite(member, beforeInviteHistory);

                } else {
                    // 메일 최초 발송
                    thisInviteHistory = MemberInviteHistory.of(member, diaryRoom, email, request.getHostId());
                }

            } else {
                // 비회원인 경우, 계정 만든 후 초대 진행
                Member newMember = Member.ofCreateInviteMember(email);
                memberRepository.save(newMember);
                thisInviteHistory = MemberInviteHistory.of(newMember, diaryRoom, email, request.getHostId());
            }
            memberInviteHistoryRepository.save(thisInviteHistory);

            Member host = memberRepository.findById(request.getHostId())
                            .orElseThrow(MemberNotFoundException::new);
            // 메일 발송
            emailSenderComponent.sendMemberInviteMail(diaryRoom.getName(), host.getNickName(), email,
                            thisInviteHistory.getUuid())
                    .addCallback(result -> {
                        log.info("일기방 {} 로 email {} 초대 메일 발송 성공", diaryRoom.getName(), email);
                    }, ex -> log.info("일기방 {} 로 email {} 초대 메일 발송 실패", diaryRoom.getName(), email));
        }
    }

    public void updateInviteHistory(Long historyId, InviteAcceptStatus acceptStatus) {
        MemberInviteHistory history = memberInviteHistoryRepository.findByIdWithMemberAndDiaryRoom(historyId)
                .filter(MemberInviteHistory::canUpdateStatus)
                .orElseThrow(InvalidInviteHistoryIdException::new);

        history.updateAcceptStatus(acceptStatus);

        if(InviteAcceptStatus.ACCEPT.equals(acceptStatus)) {
            publisher.publishEvent(new InviteAcceptEvent(history.getMember(), history.getDiaryRoom()));
        }
    }

    public List<MemberInviteHistoryDTO> findByLoginUserInviteHistory(Long loginId){
        List<MemberInviteHistory> inviteHistories = memberInviteHistoryRepository.findAllByMemberInviteHistories(loginId);

        if(inviteHistories.isEmpty()){
            //추후 custom exception 추가
            throw new IllegalArgumentException();
        }
        List<MemberInviteHistoryDTO> dtoList = inviteHistoryMapper.asDTOList(inviteHistories);
        return dtoList;
    }
}
