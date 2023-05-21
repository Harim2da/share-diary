package share_diary.diray.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.auth.dto.request.LoginRequestDTO;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.member.MemberIdOrPasswordErrorException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.jwt.JwtManager;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtManager jwtManager;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public String makeAccessToken(LoginRequestDTO loginRequestDTO){

        Member member = memberRepository.findByMemberId(loginRequestDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        validatedPassword(loginRequestDTO.getPassword(),member.getPassword());

        return jwtManager.makeAccessToken(member.getId());
    }

    public void validatedPassword(String loginPassword,String memberPassword){
        boolean match = passwordEncoder.match(loginPassword, memberPassword);
        if(!match){
            throw new MemberIdOrPasswordErrorException();
        }
    }

    public String makeRefreshToken(Long id){
        return jwtManager.makeRefreshToken(id);
    }

    public Long extractIdByToken(String token){
        return jwtManager.getIdFromPayLoad(token);
    }

    public void validateToken(String token){
        jwtManager.validatedJwtToken(token);
    }

}
