package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import share_diary.diray.exception.memberInviteHistory.InvalidInviteUuidException;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.member.domain.Role;
import share_diary.diray.member.dto.MemberDTO;
import share_diary.diray.member.dto.request.*;
import share_diary.diray.member.mapper.MemberMapper;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

import java.time.LocalDateTime;
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

    public void joinMember(MemberSignUpRequestDTO requestDTO, LocalDateTime now) {
        //빵
        validationLoginId(requestDTO.getLoginId());

        //속
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

            //일기방 초대를 통해 생성된 Member 객체 회원가입 진행
        if (joinedMemberUpdate(requestDTO, now, encodedPassword)) return;

        validationEmail(requestDTO.getEmail());
        Member member = MemberSignUpRequestDTO.fromToMember(requestDTO,encodedPassword,now);

        //빵
        memberRepository.save(member);
    }

    public void joinMemberSocial(MemberSignUpSocialRequestDTO requestDTO) {

        validationEmail(requestDTO.getEmail());

        Member member = MemberSignUpSocialRequestDTO.fromToMember(requestDTO);

        memberRepository.save(member);
    }

    private void validationLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new ValidationMemberIdException();
        }
    }

    private void validationEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new ValidationMemberEmailException();
        }
    }

    private boolean joinedMemberUpdate(MemberSignUpRequestDTO requestDTO, LocalDateTime now, String encodedPassword) {
        if(memberRepository.isJoinedMember(requestDTO.getEmail())){
            Member findMember = memberRepository.findByEmail(requestDTO.getEmail())
                    .orElseThrow(MemberNotFoundException::new);
            findMember.updateJoinMember(requestDTO, encodedPassword, now);
            return true;
        }
        return false;
    }

    public MemberDTO findMemberById(Long loginId) {
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);
        return memberMapper.asDTO(member);
    }

    public MemberDTO findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        return memberMapper.asDTO(member);
    }

    public void passwordCheck(Long loginId, MemberPasswordRequestDTO requestDTO) {
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);

        String password = requestDTO.getPassword();

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new PasswordNotCoincide();
        }
    }

    public void updatePassword(Long loginId, MemberPasswordUpdateDTO requestDTO) {
        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberNotFoundException::new);

        String password = requestDTO.getPassword();

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new PasswordNotCoincide();
        }

        String encode = passwordEncoder.encode(requestDTO.getUpdatePassword());
        member.updatePassword(encode);
    }

    public MemberDTO updateMember(MemberUpdateRequestDTO requestDTO) {
        Member member = memberRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if(!passwordEncoder.matches(requestDTO.getPassword(),member.getPassword())){
            throw new PasswordNotCoincide();
        }

        String encode = passwordEncoder.encode(requestDTO.getPassword());
        member.updateMember(encode, requestDTO.getNickName());
        return memberMapper.asDTO(member);
    }

    public boolean validationMemberLoginId(MemberLoginIdRequestDTO requestDTO) {
        return memberRepository.existsByLoginId(requestDTO.getLoginId());
    }

    public boolean validationMemberEmail(MemberEmailRequestDTO requestDTO) {
        return memberRepository.existsByEmail(requestDTO.getEmail());
    }

    public void sendCertificationNumber(MemberIdAndEmailRequestDTO requestDTO) {
        Member member = memberRepository.findByLoginId(requestDTO.getLoginId())
                .orElseThrow(MemberNotFoundException::new);

        if(!requestDTO.getEmail().equals(member.getEmail())){
            throw new IllegalArgumentException("아이디 혹은 이메일의 정보가 일치하지 않습니다.");
        }

        int certificationNumber = createMailVerifyNumber();

        //redis 에 저장
        certificationNumberRepository.save(CertificationNumber.of(certificationNumber, member.getId()));

//        emailSenderComponent.sendCertificationNumber(certificationNumber, email)
//                .addCallback(result -> log.info("email : {} 로 발송 성공", email), ex -> {
//                    //TODO: email 실패 exception 생성
//                    throw new IllegalArgumentException();
//                });
    }

    private static int createMailVerifyNumber() {
        return (int) (Math.random() * (int) 1e8);
    }

    public void validationCertificationNumber(int certificationNumber) {
        CertificationNumber findCertificationNumber = certificationNumberRepository.findById(certificationNumber)
                .orElseThrow(CertificationNotFoundException::new);
        log.info("findCertificationNumber = {}",findCertificationNumber.getCertificationNumber());
        log.info("findMemberId = {}",findCertificationNumber.getMemberId());
    }

    public void resetPassword(LoginSession session,String rawPassword) {
        Member member = memberRepository.findById(session.getId())
                .orElseThrow(MemberNotFoundException::new);

        member.updatePassword(passwordEncoder.encode(rawPassword));
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
