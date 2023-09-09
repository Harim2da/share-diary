package share_diary.diray.auth.oauth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "refresh_token_expires_in")
    private String refreshTokenExpiresIn;

    public KakaoTokenResponse() {
    }

    @Builder
    public KakaoTokenResponse(String accessToken, Long expiresIn, String scope, String tokenType, String refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
