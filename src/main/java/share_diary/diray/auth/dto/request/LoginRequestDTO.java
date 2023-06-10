package share_diary.diray.auth.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequestDTO {

    private String memberId;

    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    public static LoginRequestDTO from(String memberId, String password){
        return new LoginRequestDTO(memberId,password);
    }
}
