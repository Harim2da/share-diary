package share_diary.diray.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginSession {

    private Long id;

    public LoginSession(Long id) {
        this.id = id;
    }

    public static LoginSession from(Long id){
        return new LoginSession(id);
    }
}
