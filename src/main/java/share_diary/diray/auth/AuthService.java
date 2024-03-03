package share_diary.diray.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.auth.domain.token.RefreshToken;
import share_diary.diray.auth.domain.token.RefreshTokenDB;
import share_diary.diray.auth.domain.token.TokenDbRepository;
import share_diary.diray.auth.domain.token.TokenRepository;
import share_diary.diray.auth.controller.request.LoginRequestDTO;
import share_diary.diray.auth.oauth.OAuthManager;
import share_diary.diray.auth.oauth.OAuthManagerFinder;
import share_diary.diray.auth.oauth.SocialType;
import share_diary.diray.common.utils.Utils;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.exception.jwt.TokenExpiredException;
import share_diary.diray.exception.member.MemberIdOrPasswordErrorException;
import share_diary.diray.exception.member.MemberNotFoundException;
import share_diary.diray.auth.jwt.JwtManager;
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
    private final OAuthManagerFinder oAuthManagerFinder;

    public String makeAccessToken(String provider,LoginRequestDTO loginRequestDTO){

        String loginId = null;

        //provider 가 존재하지 않을때는 일반 로그인, 존재하면 소셜 로그인
        if(Utils.isNullAndBlank(provider)){
            loginId = loginRequestDTO.getLoginId();
        }else {
            Member oAuthUser = getOAuthUser(provider,loginRequestDTO.getCode());
            loginId = oAuthUser.getLoginId();
        }

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFoundException::new);

        if(Utils.isNullAndBlank(provider)){
            validatedPassword(loginRequestDTO.getPassword(), member.getPassword());
        }

        return jwtManager.makeAccessToken(member.getId());
    }

    private Member getOAuthUser(String provider,String code){
        SocialType socialType = SocialType.getSocialType(provider);
        OAuthManager oAuthManager = oAuthManagerFinder.findByOAuthManager(socialType);
        return oAuthManager.getMemberInfo(code);
    }

    private void validatedPassword(String loginPassword,String memberPassword){
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