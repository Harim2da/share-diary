package share_diary.diray.member.domain;

/**
 * JoinStatus : 회원 여부
 * - USER : 회원
 * - WAITING : 비회원
 * - DELETE : 탈퇴 회원
 */
public enum JoinStatus {
    USER,
    WAITING,
    DELETE
}
