package share_diary.diray.auth.oauth;

import share_diary.diray.member.domain.Member;

public interface OAuthManager {
    Member getMemberInfo(String code);
    String getAccessToken(String code);
    SocialType getType();
}
