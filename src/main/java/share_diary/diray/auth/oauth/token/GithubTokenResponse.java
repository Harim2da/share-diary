package share_diary.diray.auth.oauth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GithubTokenResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    private String scope;

    @JsonProperty(value = "token_type")
    private String tokenType;

    public GithubTokenResponse() {
    }
}
