package share_diary.diray.domain.memberInviteHistory;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import share_diary.diray.DatabaseCleanup;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.diaryRoom.DiaryRoomRepository;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class MemberInviteHistoryRepositoryCustomImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DiaryRoomRepository diaryRoomRepository;
    @Autowired
    private MemberInviteHistoryRepository memberInviteHistoryRepository;

    @AfterEach
    void tearDown() {
        memberInviteHistoryRepository.deleteAllInBatch();
        diaryRoomRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
//        databaseCleanup.execute();
    }

    @Test
    @DisplayName("일기방에 사용자를 초대한 뒤 초대받은 사용자의 초대내역을 확인한다.")
    void findAllByMemberInviteHistories() {
        //given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        DiaryRoom diaryRoom = createDiaryRoom();
        DiaryRoom savedDiaryRoom = diaryRoomRepository.save(diaryRoom);

        long hostId = 112L;
        LocalDateTime pastTime = LocalDateTime.of(2024, Month.JANUARY, 4, 10, 30);
        LocalDateTime lastTime = LocalDateTime.of(2024, Month.JANUARY, 5, 20, 30);

        List<MemberInviteHistory> memberInviteHistories = List.of(
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, pastTime),
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, pastTime),
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, pastTime),
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, lastTime),
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, lastTime),
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, lastTime),
                MemberInviteHistory.of(savedMember, savedDiaryRoom, "jipdol2@gmail.com", hostId, lastTime)
        );

        memberInviteHistoryRepository.saveAll(memberInviteHistories);

        int limit = 5;
        //when
        List<MemberInviteHistory> findByInviteHistories1Page = memberInviteHistoryRepository.findAllByMemberInviteHistories(savedMember.getId(), null, limit);

        //then
        assertThat(findByInviteHistories1Page)
                .hasSize(5)
                .extracting("email", "status", "hostUserId", "inviteDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, lastTime),
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, lastTime),
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, lastTime),
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, lastTime),
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, pastTime)
                );

        //2번째 페이지
        //다음 페이지를 조회하기 위해 이전페이지의 마지막 PK 값을 사용(무한스크롤)
        Long lastFindId = findByInviteHistories1Page.get(findByInviteHistories1Page.size() - 1).getId();
        List<MemberInviteHistory> findByInviteHistories2Page = memberInviteHistoryRepository.findAllByMemberInviteHistories(savedMember.getId(), lastFindId, limit);

        System.out.println("lastFindId = " + lastFindId);
        findByInviteHistories1Page.forEach(param-> System.out.println(param.getId()));
        findByInviteHistories2Page.forEach(param-> System.out.println(param.getId()));

        assertThat(findByInviteHistories2Page)
                .hasSize(2)
                .extracting("email", "status", "hostUserId", "inviteDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, pastTime),
                        Tuple.tuple("jipdol2@gmail.com", InviteAcceptStatus.INVITE, hostId, pastTime)
                );
    }

    private Member createMember() {
        return Member.ofCreateMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "1234",
                "집돌2",
                JoinStatus.USER,
                LocalDateTime.now()
        );
    }

    private DiaryRoom createDiaryRoom() {
        return DiaryRoom.of(
                "testDiaryRoom",
                "jipdol2",
                LocalDateTime.now()
        );
    }

}