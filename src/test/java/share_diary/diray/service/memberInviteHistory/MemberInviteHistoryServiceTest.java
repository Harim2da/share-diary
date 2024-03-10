package share_diary.diray.service.memberInviteHistory;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.AsyncResult;
import share_diary.diray.DatabaseCleanup;
import share_diary.diray.common.email.EmailSenderComponent;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.domain.DiaryRoomRepository;
import share_diary.diray.exception.diaryRoom.DiaryRoomNotFoundException;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.controller.request.MemberInviteRequestDTO;
import share_diary.diray.memberInviteHistory.controller.response.MemberInviteHistoryDTO;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;
import share_diary.diray.service.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.anyString;

class MemberInviteHistoryServiceTest extends IntegrationTestSupport {

    //Database cleanUp
    @Autowired
    private DatabaseCleanup databaseCleanup;

    //Service
    @Autowired
    private MemberInviteHistoryService memberInviteHistoryService;

    //Repository
    @Autowired
    private MemberInviteHistoryRepository memberInviteHistoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DiaryRoomRepository diaryRoomRepository;
    @Autowired
    private MemberDiaryRoomRepository memberDiaryRoomRepository;

    //Mocking
    @MockBean
    private EmailSenderComponent emailSenderComponent;

    @AfterEach
    void tearDown(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("사용자를 일기방에 초대한다. 그리고 초대내역을 조회해서 검증한다.")
    void inviteRoomMembers(){
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        MemberDiaryRoom memberDiaryRoom = MemberDiaryRoom.of(savedMember,savedDiaryRoom, Role.HOST);
        MemberDiaryRoom savedMemberDiaryRoom = memberDiaryRoomRepository.save(memberDiaryRoom);

        List<String> emails = List.of("gch03915@gmail.com", "unionfind@kakao.com");

        given(emailSenderComponent.sendMemberInviteMail(anyString(),anyString(),anyString(),anyString()))
                .willAnswer(invocation -> new AsyncResult<>(Boolean.TRUE));

        MemberInviteRequestDTO request = MemberInviteRequestDTO.of(savedDiaryRoom.getId(),emails,member.getId());
        LocalDateTime time = LocalDateTime.of(2024,Month.FEBRUARY,10,10,20);

        //when
        memberInviteHistoryService.inviteRoomMembers(request,time);

        //then
        List<MemberInviteHistory> findAllByInviteHistory = memberInviteHistoryRepository.findAll();

        assertThat(findAllByInviteHistory).isNotEmpty();
        assertThat(findAllByInviteHistory)
                .hasSize(2)
                .extracting("email","status","inviteDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("gch03915@gmail.com", InviteAcceptStatus.INVITE,time),
                        Tuple.tuple("unionfind@kakao.com", InviteAcceptStatus.INVITE,time)
                );
    }

    @Test
    @DisplayName("초대 받은 사용자가 초대를 수락/거절 한다. 그 후 일기방 일원이 되었는지 검증한다.")
    void updateInviteHistory(){
        //given
        Member member = createMember();
        Member hostMember = memberRepository.save(member);

        Member member2 = Member.ofCreateInviteMember("unionfind@kakao.com");
        Member invitedMember = memberRepository.save(member2);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        MemberDiaryRoom memberDiaryRoom = MemberDiaryRoom.of(hostMember,savedDiaryRoom, Role.HOST);
        MemberDiaryRoom savedMemberDiaryRoom = memberDiaryRoomRepository.save(memberDiaryRoom);

        LocalDateTime time = LocalDateTime.of(2024,Month.FEBRUARY,10,10,20);

        MemberInviteHistory inviteHistory = MemberInviteHistory.of(invitedMember,diaryRoom,invitedMember.getEmail(),hostMember.getId(),time);
        MemberInviteHistory savedHistory = memberInviteHistoryRepository.save(inviteHistory);

        //when
        memberInviteHistoryService.updateInviteHistory(savedHistory.getId(),InviteAcceptStatus.ACCEPT);

        //then
        MemberInviteHistory findInviteHistory = memberInviteHistoryRepository.findById(savedHistory.getId())
                .orElseThrow(() -> new IllegalArgumentException("초대 내역이 없습니다."));

        assertThat(findInviteHistory).extracting("email","status")
                .containsExactlyInAnyOrder("unionfind@kakao.com",InviteAcceptStatus.ACCEPT);

        List<MemberDiaryRoom> findMemberDiaryRooms = memberDiaryRoomRepository.findAllByMemberId(invitedMember.getId());
        MemberDiaryRoom findMemberDiaryRoom = findMemberDiaryRooms.get(0);

        Member findMember = findMemberDiaryRoom.getMember();
        assertThat(findMember.getId()).isEqualTo(invitedMember.getId());
        assertThat(findMember.getEmail()).isEqualTo(invitedMember.getEmail());
    }

    @Test
    @DisplayName("사용자의 모든 초대내역을 조회한다.")
    void findByLoginUserInviteHistory(){
        //given
        Member member = createMember();
        Member hostMember = memberRepository.save(member);

        Member member2 = Member.ofCreateInviteMember("unionfind@kakao.com");
        Member invitedMember = memberRepository.save(member2);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        MemberDiaryRoom memberDiaryRoom = MemberDiaryRoom.of(hostMember,savedDiaryRoom, Role.HOST);
        MemberDiaryRoom savedMemberDiaryRoom = memberDiaryRoomRepository.save(memberDiaryRoom);

        LocalDateTime time = LocalDateTime.of(2024,Month.FEBRUARY,10,10,20);

        MemberInviteHistory inviteHistory = MemberInviteHistory.of(invitedMember,diaryRoom,invitedMember.getEmail(),hostMember.getId(),time);
        MemberInviteHistory savedHistory = memberInviteHistoryRepository.save(inviteHistory);

        MemberInviteHistory reInviteHistory = MemberInviteHistory.reInvite(invitedMember,savedHistory,time);
        memberInviteHistoryRepository.save(reInviteHistory);

        //when
        List<MemberInviteHistoryDTO> findAllInviteHistory = memberInviteHistoryService.findByLoginUserInviteHistory(invitedMember.getId(), null, 5);

        //then
        assertThat(findAllInviteHistory).isNotEmpty();
        assertThat(findAllInviteHistory)
                .hasSize(2)
                .extracting("email","hostUserId","hostUserNickname","status","diaryRoomName","inviteDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("unionfind@kakao.com",hostMember.getId(),hostMember.getNickName(),InviteAcceptStatus.INVITE,"testDiary",time),
                        Tuple.tuple("unionfind@kakao.com",hostMember.getId(),hostMember.getNickName(),InviteAcceptStatus.RE_INVITE,"testDiary",time)
                );
    }

    private Member createMember() {
        return Member.ofCreateMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "123",
                "jipdol2",
                JoinStatus.USER,
                LocalDateTime.now()
        );
    }

    private DiaryRoom createDiaryRoom(){
        return DiaryRoom.of(
                "testDiary",
                "jipdol2",
                LocalDateTime.of(2023, Month.DECEMBER, 28, 12, 30)
        );
    }
}