package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class MemberUpdateRequestDTO {

    @NotEmpty(message = "email 정보가 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호 정보가 올바르지 않습니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인 정보가 올바르지 않습니다.")
    private String validationPassword;

    @NotEmpty(message = "닉네임 정보가 올바르지 않습니다.")
    private String nickName;

    public MemberUpdateRequestDTO() {
    }

    public boolean validationPassword(){
        return this.password.equals(this.validationPassword);
    }

    public MemberUpdateRequestDTO(String email, String password, String validationPassword, String nickName) {
        this.email = email;
        this.password = password;
        this.validationPassword = validationPassword;
        this.nickName = nickName;
    }

    public static MemberUpdateRequestDTO of(String email, String password, String validationPassword, String nickName){
        return new MemberUpdateRequestDTO(email,password,validationPassword,nickName);
    }
}
