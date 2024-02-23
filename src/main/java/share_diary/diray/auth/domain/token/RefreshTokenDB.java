package share_diary.diray.auth.domain.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "refresh_token_db")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenDB {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
