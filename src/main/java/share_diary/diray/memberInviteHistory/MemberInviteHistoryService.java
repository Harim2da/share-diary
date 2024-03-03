package share_diary.diray.memberInviteHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.common.email.EmailSenderComponent;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.exception.diaryRoom.DiaryRoomNotFoundException;
import share_diary.diray.exception.member.MemberIdOrPasswordErrorException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.exception.memberInviteHistory.InvalidInviteHistoryIdException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;
import share_diary.diray.memberInviteHistory.controller.request.MemberInviteRequestDTO;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;
import share_diary.diray.memberInviteHistory.controller.response.MemberInviteHistoryDTO;
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

    public void inviteRoomMembers(MemberInviteRequestDTO request, LocalDateTime now) {
        // 일기방 검증 및 host 체크
        Member hostMember = memberRepository.findById(request.getHostId())
                .orElseThrow(MemberNotFoundException::new);

        DiaryRoom diaryRoom = memberDiaryRoomRepository.findByMemberIdAndDiaryRoomIdWithDiaryRoom(request.getHostId(), request.getDiaryRoomId())
                .orElseThrow(DiaryRoomNotFoundException::new)
                .getDiaryRoom();

        // 멤버에서 이메일로 조회해오기 -> 일치하는 멤버가 없으면 임시 회원가입 필요
            //회원가입 되어 있는 사용자들 조회
        List<Member> members = memberRepository.findAllByEmail(request.getEmails());

        // 회원가입 되어 있지 않은 사용자들 임시 회원가입
        List<Member> nonMembers = savedNonMembers(request.getEmails(), members);

        // 초대받은 일기방에 이미 초대가 된 내역이 있는지 조회
        List<MemberInviteHistory> histories = memberInviteHistoryRepository.findAllByEmailAndRoomIdWithMemberAndDiaryRoom(request.getDiaryRoomId(), request.getEmails());
        // 초대내역 저장
        List<MemberInviteHistory> memberInviteHistoryList = getMemberInviteHistories(now, nonMembers, diaryRoom, hostMember, histories, members);
        List<MemberInviteHistory> savedInviteHistories = memberInviteHistoryRepository.saveAll(memberInviteHistoryList);

        // email 전송
        savedInviteHistories.forEach(inviteHistory->{
            emailSenderComponent.sendMemberInviteMail(diaryRoom.getName(),hostMember.getNickName(),inviteHistory.getEmail(),inviteHistory.getUuid())
                    .addCallback(result -> log.info("일기방 {} 로 email {} 초대 메일 발송 성공", diaryRoom.getName(), inviteHistory.getEmail())
                            ,ex -> log.info("일기방 {} 로 email {} 초대 메일 발송 실패", diaryRoom.getName(), inviteHistory.getEmail()));
        });

/*        for (String email : request.getEmails()) {
            Member member = members.stream()
                    .filter(m -> email.equals(m.getEmail()))
                    .findFirst()
                    .orElse(null);

            MemberInviteHistory thisInviteHistory;

            // 일기방에 해당 메일 초대 이력 있는지 확인 필요
            if (Objects.nonNull(member)) {
                MemberInviteHistory beforeInviteHistory = histories.stream()
                        .filter(memberHistory -> member.getId().equals(memberHistory.getMember().getId()))
                        .findFirst()
                        .orElse(null);

                if (Objects.nonNull(beforeInviteHistory)) {
                    // 메일 재발송 - 기존 초대 상태 Reinvite로 변경 -> Invite 하나 더 쌓기
                    thisInviteHistory = MemberInviteHistory.reInvite(member, beforeInviteHistory, now);

                } else {
                    // 메일 최초 발송
                    thisInviteHistory = MemberInviteHistory.of(member, diaryRoom, email, request.getHostId(), now);
                }

            } else {
                // 비회원인 경우, 계정 만든 후 초대 진행
                Member newMember = Member.ofCreateInviteMember(email);
                memberRepository.save(newMember);
                thisInviteHistory = MemberInviteHistory.of(newMember, diaryRoom, email, request.getHostId(), now);
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
        }*/
    }

    private static List<MemberInviteHistory> getMemberInviteHistories(
            LocalDateTime now,
            List<Member> nonMembers,
            DiaryRoom diaryRoom,
            Member hostMember,
            List<MemberInviteHistory> histories,
            List<Member> members
    ) {
        List<MemberInviteHistory> nonMemberHistory = nonMembers.stream()
                .map(member -> MemberInviteHistory.of(member, diaryRoom, member.getEmail(), hostMember.getId(), now))
                .collect(Collectors.toList());

        List<Member> beforeInvitedMembers = histories.stream()
                .map(MemberInviteHistory::getMember)
                .collect(Collectors.toList());

        List<MemberInviteHistory> invitedMembersHistory = members.stream()
                .filter(member -> beforeInvitedMembers.contains(member))
                .map(member -> MemberInviteHistory.of(member, diaryRoom, member.getEmail(), hostMember.getId(), now))
                .collect(Collectors.toList());

        List<MemberInviteHistory> nonInvitedMembersHistory = members.stream()
                .filter(member -> !beforeInvitedMembers.contains(member))
                .map(member -> MemberInviteHistory.of(member, diaryRoom, member.getEmail(), hostMember.getId(), now))
                .collect(Collectors.toList());

        nonMemberHistory.addAll(invitedMembersHistory);
        nonMemberHistory.addAll(nonInvitedMembersHistory);
        return nonMemberHistory;
    }

    private List<Member> savedNonMembers(List<String> emails,List<Member> members){
        List<String> inviteEmails = new ArrayList<>(emails);
        inviteEmails.removeAll(members.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList()));

        List<Member> newMembers = Member.ofCreateInviteMembers(inviteEmails);
        return memberRepository.saveAll(newMembers);
    }

    public void updateInviteHistory(Long historyId, InviteAcceptStatus acceptStatus) {
        MemberInviteHistory history = memberInviteHistoryRepository.findByIdWithMemberAndDiaryRoom(historyId)
                .filter(MemberInviteHistory::canUpdateStatus)
                .orElseThrow(InvalidInviteHistoryIdException::new);

        history.updateAcceptStatus(acceptStatus);

        if (InviteAcceptStatus.ACCEPT.equals(acceptStatus)) {
            publisher.publishEvent(new InviteAcceptEvent(history.getMember(), history.getDiaryRoom()));
        }
    }

    public List<MemberInviteHistoryDTO> findByLoginUserInviteHistory(Long loginId, Long inviteHistoryId, int limit) {
        List<MemberInviteHistory> inviteHistories = memberInviteHistoryRepository.findAllByMemberInviteHistories(loginId, inviteHistoryId, limit);
        List<MemberInviteHistoryDTO> memberInviteHistoryDTOList = inviteHistoryMapper.asMemberInviteHistoryDTOList(inviteHistories);
        for (MemberInviteHistoryDTO dto : memberInviteHistoryDTOList) {
            Member member = memberRepository.findById(dto.getHostUserId())
                    .orElseThrow(MemberIdOrPasswordErrorException::new);
            dto.updateHostUserNickname(member.getNickName());
        }
        return memberInviteHistoryDTOList;
    }
}
