package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void joinMember(MemberSignUpRequestDTO signUpRequestDTO){
        Member member = MemberSignUpRequestDTO.fromToMember(signUpRequestDTO);
        String encode = passwordEncoder.encode(signUpRequestDTO.getPassword());
        member.encryptPassword(encode);
        memberRepository.save(member);
    }

}
