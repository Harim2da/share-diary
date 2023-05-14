package share_diary.diray.auth.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequestDTO {

    private String memberId;

    private String password;
}
