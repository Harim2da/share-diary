package share_diary.diray.auth.oauth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class GithubTokenRequest {

    private String code;

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String secret;

    public GithubTokenRequest() {
    }

    @Builder
    public GithubTokenRequest(String code, String clientId, String secret) {
        this.code = code;
        this.clientId = clientId;
        this.secret = secret;
    }
}
