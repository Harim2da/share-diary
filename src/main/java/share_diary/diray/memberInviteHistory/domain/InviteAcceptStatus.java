package share_diary.diray.memberInviteHistory.domain;

import lombok.Getter;

@Getter
public enum InviteAcceptStatus {
    INVITE,
    ACCEPT,
    DENY,
    RE_INVITE,
    CANCEL  // 일기방 사라진 경우
}
