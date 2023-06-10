package share_diary.diray.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.auth.domain.token.RefreshToken;
import share_diary.diray.auth.domain.token.TokenRepository;
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
    private final TokenRepository tokenRepository;
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
        String token = jwtManager.makeRefreshToken(id);
        RefreshToken refreshToken = RefreshToken.of(token, id);
        tokenRepository.save(refreshToken);
        return token;
    }

    //TODO : 로그아웃 시에 refreshToken 삭제
    public void removeRefreshToken(){

    }

    public Long extractIdByToken(String token){
        return jwtManager.getIdFromPayLoad(token);
    }

    public void validateToken(String token){
        jwtManager.validatedJwtToken(token);
    }

    public String renewAccessToken(Long id){
        return jwtManager.makeAccessToken(id);
    }

    public RefreshToken getRefreshToken(String token){
        RefreshToken refreshToken = tokenRepository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException());
        System.out.println(refreshToken.getRefreshToken());
        System.out.println(refreshToken.getMemberId());
        return null;
    }
}
