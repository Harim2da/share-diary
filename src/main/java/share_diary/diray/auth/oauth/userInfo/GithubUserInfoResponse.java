package share_diary.diray.auth.oauth.userInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import share_diary.diray.member.domain.Member;

@Getter
public class GithubUserInfoResponse implements OAuthUserInfoResponse{

    @JsonProperty("id")
    private String id;

    @Override
    public Member toMember(){
        return Member.builder()
                .loginId(id)
                .build();
    }
}
