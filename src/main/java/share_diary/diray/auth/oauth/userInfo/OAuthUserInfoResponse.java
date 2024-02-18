package share_diary.diray.auth.oauth.userInfo;

import share_diary.diray.member.domain.Member;

public interface OAuthUserInfoResponse {
    Member toMember();
}
