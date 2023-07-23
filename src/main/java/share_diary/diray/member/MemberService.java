package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.exception.member.PasswordNotCoincide;
import share_diary.diray.exception.member.ValidationMemberEmailException;
import share_diary.diray.exception.member.ValidationMemberIdException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.request.*;
import share_diary.diray.member.dto.response.MemberResponseDTO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void joinMember(MemberSignUpRequestDTO signUpRequestDTO){
        Member member = MemberSignUpRequestDTO.fromToMember(signUpRequestDTO);
        String encode = passwordEncoder.encode(signUpRequestDTO.getPassword());
        member.encryptPassword(encode);
        validationMember(member);
        memberRepository.save(member);
    }

    private void validationMember(Member member){
        boolean flag = memberRepository.existsByLoginId(member.getLoginId());
        if(flag){
            throw new ValidationMemberIdException();
        }
        flag = memberRepository.existsByEmail(member.getEmail());
        if(flag){
            throw new ValidationMemberEmailException();
        }
    }

    public MemberResponseDTO findMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException());
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.from(member);
        return memberResponseDTO;
    }

    public void passwordCheck(LoginSession loginSession, MemberPasswordRequestDTO memberPasswordRequestDTO){
        Long id = loginSession.getId();
        String password = memberPasswordRequestDTO.getPassword();

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException());

        if(!passwordEncoder.matches(password,member.getPassword())){
            throw new PasswordNotCoincide();
        }
    }

    public MemberResponseDTO updateMember(MemberUpdateRequestDTO memberUpdateRequestDTO) {
        Member member = memberRepository.findByEmail(memberUpdateRequestDTO.getEmail())
                .orElseThrow(() -> new MemberNotFoundException());
        String encode = passwordEncoder.encode(memberUpdateRequestDTO.getPassword());
        member.updateMember(encode,memberUpdateRequestDTO.getNickName());
        return MemberResponseDTO.from(member);
    }

    public boolean validationMemberLoginId(MemberLoginIdRequestDTO loginIdDTO){
        return memberRepository.existsByLoginId(loginIdDTO.getLoginId());
    }

    public boolean validationMemberEmail(MemberEmailRequestDTO emailDTO){
        return memberRepository.existsByEmail(emailDTO.getEmail());
    }
}