package share_diary.diray.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 후 저장 - 성공")
    void joinMemberTest() {
        //given
        MemberSignUpRequestDTO dto = new MemberSignUpRequestDTO("jipdol2", "jipdol2@gmail.com", "1234", "jipdol2");

        //when
        memberService.joinMember(dto);

        //then
        Member findByMember = memberRepository.findByLoginId("jipdol2")
                .orElse(null);

        boolean matches = passwordEncoder.matches(dto.getPassword(), findByMember.getPassword());

        assertThat(dto.getLoginId()).isEqualTo(findByMember.getLoginId());
        assertThat(dto.getEmail()).isEqualTo(findByMember.getEmail());
        assertThat(matches).isTrue();
        assertThat(dto.getNickName()).isEqualTo(findByMember.getNickName());
    }

    private Member createMember(
            String loginId,
            String email,
            String password,
            String nickName
    ) {
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
    }

}