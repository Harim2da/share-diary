package share_diary.diray.auth.domain.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenDB {

    @Id @GeneratedValue
    private Long id;
    private String refreshToken;
    private Long memberId;

    public RefreshTokenDB(String refreshToken, Long memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public static RefreshTokenDB of(String refreshToken, Long memberId){
        return new RefreshTokenDB(refreshToken,memberId);
    }
}
