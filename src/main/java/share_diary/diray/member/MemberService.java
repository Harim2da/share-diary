package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.common.email.CertificationNumber;
import share_diary.diray.common.email.CertificationNumberRepository;
import share_diary.diray.common.email.EmailSenderComponent;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.certification.CertificationNotFoundException;
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

import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderComponent emailSenderComponent;
    private final CertificationNumberRepository certificationNumberRepository;
    private final MemberDiaryRoomRepository memberDiaryRoomRepository;

    public void joinMember(MemberSignUpRequestDTO requestDTO){
        Member member = MemberSignUpRequestDTO.fromToMember(requestDTO);
        String encode = passwordEncoder.encode(requestDTO.getPassword());
        member.encryptPassword(encode);
        validationMember(member);
        memberRepository.save(member);
    }

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
  
    public void sendCertificationNumber(LoginSession session){
        String email = memberRepository.findById(session.getId())
                .orElseThrow(() -> new MemberNotFoundException())
                .getEmail();

        int certificationNumber = (int)(Math.random()*(int)1e8);

        //redis 에 저장
        certificationNumberRepository.save(CertificationNumber.of(certificationNumber,session.getId()));

        emailSenderComponent.sendCertificationNumber(certificationNumber,email)
                .addCallback(result -> log.info("email : {} 로 발송 성공",email), ex -> {
                    //TODO: email 실패 exception 생성
                    throw new IllegalArgumentException();
                });
    }

    public void validationCertificationNumber(int certificationNumber){

        certificationNumberRepository.findById(certificationNumber)
                .orElseThrow(()->new CertificationNotFoundException());
    }

    public void resetPasswordAndSendEmailToMember(LoginSession session){
        String email = memberRepository.findById(session.getId())
                .orElseThrow(() -> new MemberNotFoundException())
                .getEmail();

        String tempPassword = UUID.randomUUID().toString().substring(0,8);

        emailSenderComponent.sendTempPasswordMail(tempPassword,email)
                .addCallback(result -> log.info("email : {} 로 발송 성공",email),ex->{
                    //TODO: email 실패 exception 생성
                    throw new IllegalArgumentException();
                });
    }
  
    public Boolean validateCreateDiaryRoom(Long memberId) {

        List<MemberDiaryRoom> memberDiaryRooms = memberDiaryRoomRepository.findAllByMemberId(memberId)
                .stream()
                .filter(md -> Role.HOST.equals(md.getRole()))
                .collect(Collectors.toList());

        return memberDiaryRooms.size() < 3;
    }
}
