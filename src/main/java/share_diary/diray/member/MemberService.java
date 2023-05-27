package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.exception.member.ValidationMemberEmailException;
import share_diary.diray.exception.member.ValidationMemberIdException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;
import share_diary.diray.member.dto.request.MemberUpdateRequestDTO;
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
        long count = memberRepository.countByMemberId(member.getMemberId());
        if(count>0){
            throw new ValidationMemberIdException();
        }
        count = memberRepository.countByEmail(member.getEmail());
        if(count>0){
            throw new ValidationMemberEmailException();
        }
    }

    public MemberResponseDTO findMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException());
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.from(member);
        return memberResponseDTO;
    }

    //TODO: 회원 정보 수정
    public MemberResponseDTO updateMember(MemberUpdateRequestDTO memberUpdateRequestDTO) {
        Member member = memberRepository.findByEmail(memberUpdateRequestDTO.getEmail())
                .orElseThrow(() -> new MemberNotFoundException());
        String encode = passwordEncoder.encode(memberUpdateRequestDTO.getPassword());
        member.updateMember(encode,memberUpdateRequestDTO.getNickName());
        return MemberResponseDTO.from(member);
    }
}