package share_diary.diray.auth.domain.token;

import lombok.Getter;

@Getter
public class AccessToken {

    private String accessToken;
    private Long memberId;

    public AccessToken(String accessToken,Long memberId) {
        this.accessToken = accessToken;
        this.memberId = memberId;
    }

    public static AccessToken of(String accessToken,Long memberId){
        return new AccessToken(accessToken,memberId);
    }
}
