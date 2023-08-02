package share_diary.diray.auth.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequestDTO {

//    @NotEmpty(message = "Id 정보가 올바르지 않습니다.")
    private String loginId;
//    @NotEmpty(message = "password 정보가 올바르지 않습니다.")
    private String password;

    private String code;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public static LoginRequestDTO from(String loginId, String password){
        return new LoginRequestDTO(loginId,password);
    }
}
