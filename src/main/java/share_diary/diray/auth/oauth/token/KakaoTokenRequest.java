package share_diary.diray.auth.oauth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoTokenRequest {

    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String code;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("grant_type")
    private String grantType;

    @Builder
    public KakaoTokenRequest(String clientId, String clientSecret, String code, String redirectUri, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }
}
