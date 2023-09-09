package share_diary.diray.auth.oauth.userInfo;

import lombok.Getter;
import lombok.Setter;
import share_diary.diray.member.domain.Member;

@Getter
public class KakaoUserInfoResponse implements OAuthUserInfoResponse{

    private String id;

    @Override
    public Member toMember(){
        return Member.builder()
                .loginId(id)
                .build();
    }
}
