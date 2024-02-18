package share_diary.diray.auth.domain.token;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 3600)
public class RefreshToken {

    @Id
    private String refreshToken;
    private Long memberId;

    public RefreshToken(String refreshToken, Long memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public static RefreshToken of(String refreshToken,Long memberId){
        return new RefreshToken(refreshToken,memberId);
    }
}