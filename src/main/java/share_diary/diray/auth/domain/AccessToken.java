package share_diary.diray.auth.domain;

import lombok.Getter;

@Getter
public class AccessToken {

    private String accessToken;

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static AccessToken of(String accessToken){
        return new AccessToken(accessToken);
    }
}
