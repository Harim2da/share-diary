package share_diary.diray.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.exception.member.PasswordNotCoincide;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.MemberDTO;
import share_diary.diray.member.dto.request.*;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
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
        MemberSignUpRequestDTO dto =
                new MemberSignUpRequestDTO("jipdol2", "jipdol2@gmail.com", "1234", "jipdol2");

        //when
        memberService.joinMember(dto);

        //then
        Member findByMember = memberRepository.findByLoginId("jipdol2")
                .orElse(null);

        boolean matches = passwordEncoder.matches(dto.getPassword(), findByMember.getPassword());

        assertThat(findByMember.getLoginId()).isEqualTo(dto.getLoginId());
        assertThat(findByMember.getEmail()).isEqualTo(dto.getEmail());
        assertThat(matches).isTrue();
        assertThat(findByMember.getNickName()).isEqualTo(dto.getNickName());
    }

    @Test
    @DisplayName("회원가입 후 저장(소셜) - 성공")
    void joinSocialMemberTest(){
        //given
        MemberSignUpSocialRequestDTO dto =
                new MemberSignUpSocialRequestDTO("177123","jipdol2@gmail.com","github_jipdol2");
        //when
        memberService.joinMemberSocial(dto);

        //then
        Member findByMember = memberRepository.findByLoginId("177123")
                .orElse(null);

        assertThat(findByMember.getLoginId()).isEqualTo(dto.getId());
        assertThat(findByMember.getEmail()).isEqualTo(dto.getEmail());
        assertThat(findByMember.getNickName()).isEqualTo(dto.getNickname());
    }

    @Test
    @DisplayName("회원 아이디로 회원정보 조회")
    void findByMemberToIdTest(){
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "password123",
                "jipdol2");

        //when
        Member findByMember = memberRepository.save(member);
        //then
        MemberDTO response = memberService.findMemberById(member.getId());

        assertThat(response.getEmail()).isEqualTo(findByMember.getEmail());
        assertThat(response.getNickName()).isEqualTo(findByMember.getNickName());
    }

    @Test
    @DisplayName("이메일로 회원정보 조회")
    void findByMemberToEmailTest(){
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                "password123",
                "jipdol2");

        //when
        Member findByMember = memberRepository.save(member);
        //then
        MemberDTO response = memberService.findMemberByEmail(member.getEmail());

        assertThat(response.getLoginId()).isEqualTo(findByMember.getLoginId());
        assertThat(response.getEmail()).isEqualTo(findByMember.getEmail());
        assertThat(response.getNickName()).isEqualTo(findByMember.getNickName());
    }

    @Test
    @DisplayName("비밀번호 체킹 - 성공")
    void passwordEncoderSuccessTest(){

        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                passwordEncoder.encode("password123"),
                "jipdol2");

        Member saveMember = memberRepository.save(member);
        LoginSession session = new LoginSession(saveMember.getId());
        MemberPasswordRequestDTO dto = new MemberPasswordRequestDTO("password123");

        //then
        memberService.passwordCheck(session.getId(),dto);
    }

    @Test
    @DisplayName("비밀번호 체킹 - 실패")
    void passwordEncoderFailTest(){

        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                passwordEncoder.encode("password123"),
                "jipdol2");

        Member saveMember = memberRepository.save(member);
        LoginSession session = new LoginSession(saveMember.getId());
        MemberPasswordRequestDTO dto = new MemberPasswordRequestDTO("password111");

        //expected
        assertThatThrownBy(()-> memberService.passwordCheck(session.getId(),dto))
                .isInstanceOf(PasswordNotCoincide.class);
    }

    @Test
    @DisplayName("비밀번호 변경 체킹")
    void passwordUpdateSuccessTest(){

        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                passwordEncoder.encode("password123"),
                "jipdol2");
        Member saveMember = memberRepository.save(member);

        LoginSession session = new LoginSession(saveMember.getId());
        MemberPasswordUpdateDTO dto = new MemberPasswordUpdateDTO("password123","password111");

        //when
        memberService.updatePassword(session.getId(),dto);

        //then
        Member findByMember = memberRepository.findById(saveMember.getId())
                .orElseThrow(()->new MemberNotFoundException());

        assertThat(passwordEncoder.matches(dto.getUpdatePassword(),findByMember.getPassword())).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 아이디 중복 체크")
    void memberLoginIdValidationCheckTest(){
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                passwordEncoder.encode("password123"),
                "jipdol2");

        memberRepository.save(member);

        MemberLoginIdRequestDTO dto = new MemberLoginIdRequestDTO("jipdol2");
        //when
        boolean flag = memberService.validationMemberLoginId(dto);

        //then
        assertThat(flag).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 이메일 중복 체크")
    void memberEmailValidationCheckTest(){
        //given
        Member member = createMember(
                "jipdol2",
                "jipdol2@gmail.com",
                passwordEncoder.encode("password123"),
                "jipdol2");
        memberRepository.save(member);

        //when
        MemberEmailRequestDTO dto = new MemberEmailRequestDTO("jipdol2@gmail.com");
        boolean flag = memberService.validationMemberEmail(dto);

        //then
        assertThat(flag).isEqualTo(true);
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