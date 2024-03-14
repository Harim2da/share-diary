package share_diary.diray.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class MemberRepositoryCustomImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("등록되어 있는 email 로 회원을 조회합니다.")
    void findAllByEmail(){
        //given
        Member member = createMember();
        memberRepository.save(member);

        //when
        Member findByMember = memberRepository.findByEmail("jipdol2@gmail.com")
                .orElseThrow(MemberNotFoundException::new);

        //then
        assertThat(findByMember)
                .extracting("loginId","email","password","nickName","joinStatus")
                .containsExactlyInAnyOrder(member.getLoginId(),member.getEmail(),member.getPassword(),member.getNickName(),member.getJoinStatus());
    }

    @Test
    @DisplayName("")
    void isJoinedMember(){
        //given
        Member member = Member.ofCreateInviteMember("jipdol2@gmail.com");
        memberRepository.save(member);

        //when
        boolean joinedMember = memberRepository.isJoinedMember("jipdol2@gmail.com");

        //then
        assertThat(joinedMember).isTrue();
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
}