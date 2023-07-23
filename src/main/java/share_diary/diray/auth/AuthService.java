package share_diary.diray.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.auth.domain.token.RefreshToken;
import share_diary.diray.auth.domain.token.RefreshTokenDB;
import share_diary.diray.auth.domain.token.TokenDbRepository;
import share_diary.diray.auth.domain.token.TokenRepository;
import share_diary.diray.auth.dto.request.LoginRequestDTO;

import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.jwt.TokenExpiredException;
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
    private final TokenDbRepository tokenDbRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public String makeAccessToken(LoginRequestDTO loginRequestDTO){

        Member member = memberRepository.findByLoginId(loginRequestDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        validatedPassword(loginRequestDTO.getPassword(),member.getPassword());

        return jwtManager.makeAccessToken(member.getId());
    }

    public void validatedPassword(String loginPassword,String memberPassword){
        boolean match = passwordEncoder.matches(loginPassword, memberPassword);
        if(!match){
            throw new MemberIdOrPasswordErrorException();
        }
    }
/** ------------------------------------------Redis------------------------------------------**/
    public String makeRefreshToken(Long id){
        String token = jwtManager.makeRefreshToken(id);
        RefreshToken refreshToken = RefreshToken.of(token, id);
        tokenRepository.save(refreshToken);
        return token;
    }

    public void removeRefreshToken(String refreshToken){
        RefreshToken token = findByRefreshToken(refreshToken);
        tokenRepository.delete(token);
    }

    public RefreshToken findByRefreshToken(String refreshToken){
        return tokenRepository.findById(refreshToken)
                .orElseThrow(()->new TokenExpiredException());
    }

/**  ------------------------------------------DB------------------------------------------**/
    public String makeRefreshTokenToDB(Long id){
        String token = jwtManager.makeRefreshToken(id);
        RefreshTokenDB refreshTokenDB = RefreshTokenDB.of(token,id);
        tokenDbRepository.save(refreshTokenDB);
        return token;
    }

    public void removeRefreshTokenToDB(String refreshToken){
        RefreshTokenDB token = findByRefreshTokenToDB(refreshToken);
        tokenDbRepository.delete(token);
    }

    public RefreshTokenDB findByRefreshTokenToDB(String refreshToken){
        return tokenDbRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new TokenExpiredException());
    }
/** ---------------------------------------------------------------------------------------**/
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