package share_diary.diray.member.domain;

public enum Role {
    HOST,
    USER
    ;

    public boolean isHost(){
        return this == HOST;
    }

    public boolean isUser(){
        return this == USER;
    }
}
