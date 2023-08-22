package share_diary.diray.auth.oauth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoogleTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "tokenType")
    private String tokenType;

    @JsonProperty(value = "id_token")
    private String idToken;

    public GoogleTokenResponse() {
    }

    @Builder
    public GoogleTokenResponse(String accessToken, Long expiresIn, String scope, String tokenType, String idToken) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
        this.idToken = idToken;
    }
}
