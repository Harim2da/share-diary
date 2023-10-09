package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.common.email.CertificationNumber;
import share_diary.diray.common.email.CertificationNumberRepository;
import share_diary.diray.common.email.EmailSenderComponent;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.certification.CertificationNotFoundException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.exception.member.PasswordNotCoincide;
import share_diary.diray.exception.member.ValidationMemberEmailException;
import share_diary.diray.exception.member.ValidationMemberIdException;
import share_diary.diray.exception.memberInviteHistory.InvalidInviteUuidException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.member.dto.MemberDTO;
import share_diary.diray.member.dto.request.*;
import share_diary.diray.member.dto.response.MemberMyPageDTO;
import share_diary.diray.member.dto.response.MemberResponseDTO;
import share_diary.diray.member.mapper.MemberMapper;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

import java.util.UUID;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    //mapper
    private final MemberMapper memberMapper;

    //passwordEncoder
    private final PasswordEncoder passwordEncoder;

    //emailSend
    private final EmailSenderComponent emailSenderComponent;

    //repository
    private final MemberRepository memberRepository;
    private final MemberDiaryRoomRepository memberDiaryRoomRepository;
    private final CertificationNumberRepository certificationNumberRepository;
    private final MemberInviteHistoryRepository memberInviteHistoryRepository;

    public void joinMember(MemberSignUpRequestDTO requestDTO){
        Member member = MemberSignUpRequestDTO.fromToMember(requestDTO);
        String encode = passwordEncoder.encode(requestDTO.getPassword());
        member.updatePassword(encode);
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

    public MemberMyPageDTO findMemberById(Long loginId){
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);
        return MemberMyPageDTO.toDto(member);
    }

    public MemberResponseDTO findMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponseDTO.from(member);
    }

    public void passwordCheck(Long loginId, MemberPasswordRequestDTO requestDTO){
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);

        String password = requestDTO.getPassword();

        if(!passwordEncoder.matches(password,member.getPassword())){
            throw new PasswordNotCoincide();
        }
    }

    public void updatePassword(Long loginId, MemberPasswordUpdateDTO requestDTO){
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);

        String password = requestDTO.getPassword();

        if(!passwordEncoder.matches(password,member.getPassword())){
            throw new PasswordNotCoincide();
        }

        String encode = passwordEncoder.encode(requestDTO.getUpdatePassword());
        member.updatePassword(encode);
    }

    public MemberResponseDTO updateMember(MemberUpdateRequestDTO requestDTO) {
        Member member = memberRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(MemberNotFoundException::new);
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
                .orElseThrow(MemberNotFoundException::new)
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
                .orElseThrow(CertificationNotFoundException::new);
    }

    public void resetPasswordAndSendEmailToMember(LoginSession session){
        String email = memberRepository.findById(session.getId())
                .orElseThrow(MemberNotFoundException::new)
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

    public MemberDTO validateMember(String uuid) {

        MemberInviteHistory memberInviteHistory = memberInviteHistoryRepository.findByUuidWithMember(uuid)
                .orElseThrow(InvalidInviteUuidException::new); // uuid가 없는 경우
        memberInviteHistory.validateUuid();
        return memberMapper.asDTO(memberInviteHistory.getMember());
    }
}
