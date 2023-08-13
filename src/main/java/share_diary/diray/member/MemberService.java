package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
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
import share_diary.diray.member.domain.Role;
import share_diary.diray.member.dto.request.*;
import share_diary.diray.member.dto.response.MemberResponseDTO;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberDiaryRoomRepository memberDiaryRoomRepository;

    public void joinMember(MemberSignUpRequestDTO requestDTO){
        Member member = MemberSignUpRequestDTO.fromToMember(requestDTO);
        String encode = passwordEncoder.encode(requestDTO.getPassword());
        member.encryptPassword(encode);
        validationMember(member);
        memberRepository.save(member);
    }

    // - review
    public void joinMemberSocial(MemberSignUpSocialRequestDTO requestDTO){
        Member member = MemberSignUpSocialRequestDTO.fromToMember(requestDTO);
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

    public void passwordCheck(LoginSession loginSession, MemberPasswordRequestDTO requestDTO){
        Long id = loginSession.getId();
        String password = requestDTO.getPassword();

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException());

        if(!passwordEncoder.matches(password,member.getPassword())){
            throw new PasswordNotCoincide();
        }
    }

    public MemberResponseDTO updateMember(MemberUpdateRequestDTO requestDTO) {
        Member member = memberRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new MemberNotFoundException());
        String encode = passwordEncoder.encode(requestDTO.getPassword());
        member.updateMember(encode,requestDTO.getNickName());
        return MemberResponseDTO.from(member);
    }

    public boolean validationMemberLoginId(MemberLoginIdRequestDTO requestDTO){
        return memberRepository.existsByLoginId(requestDTO.getLoginId());
    }

    public boolean validationMemberEmail(MemberEmailRequestDTO requestDTO){
        return memberRepository.existsByEmail(requestDTO.getEmail());
    }

    public Boolean validateCreateDiaryRoom(Long memberId) {

        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByMemberId(memberId)
                .stream()
                .filter(md -> Role.HOST.equals(md.getRole()))
                .collect(Collectors.toList());

        return memberDiaryRooms.size() < 3;
    }
}
