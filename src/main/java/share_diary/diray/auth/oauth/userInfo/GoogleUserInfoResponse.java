package share_diary.diray.auth.oauth.userInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import share_diary.diray.member.domain.Member;

@Getter
public class GoogleUserInfoResponse implements OAuthUserInfoResponse{

    @JsonProperty(value = "id")
    private String id;

    @Override
    public Member toMember() {
        return Member.builder()
                .loginId(id)
                .build();
    }
}
