package share_diary.diray.auth.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class LoginRequestDTO {

    @NotEmpty(message = "Id 정보가 올바르지 않습니다.")
    private String memberId;
    @NotEmpty(message = "password 정보가 올바르지 않습니다.")
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
