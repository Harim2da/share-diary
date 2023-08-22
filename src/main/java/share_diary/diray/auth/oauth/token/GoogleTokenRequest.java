package share_diary.diray.auth.oauth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GoogleTokenRequest {

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    private String code;

    @JsonProperty(value = "redirect_uri")
    private String redirectUri;

    @JsonProperty(value = "grant_type")
    private String grantType;

    @Builder
    public GoogleTokenRequest(String clientId, String clientSecret, String code, String redirectUri, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }
}
