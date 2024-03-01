package share_diary.diray.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import share_diary.diray.exception.jwt.TokenExpiredException;
import share_diary.diray.exception.jwt.TokenIsNotValidException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtManager {

    @Value("${jwt.secret}")
    private String KEY;

    private static final int ACCESS_TIME = 60*10*1000;  //10분(임시 30분 조정)

    private static final int REFRESH_TIME = 60*30*1000; //20분

    public String makeAccessToken(Long id){
        return makeToken(id,ACCESS_TIME);
    }

    public String makeRefreshToken(Long id){
        return makeToken(id,REFRESH_TIME);
    }

    public String makeToken(Long id,int tokenTime){

        return Jwts.builder()
                .claim("id",id)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+tokenTime))
                .compact();
    }

    public Long getIdFromPayLoad(String token){
        return getClaims(token).get("id",Long.class);
    }

    public Claims getClaims(String token){

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody();
    }

    public void validatedJwtToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
        }catch(ExpiredJwtException e){
            throw new TokenExpiredException();
        }catch (JwtException e){
            throw new TokenIsNotValidException();
        }
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
    }
}
