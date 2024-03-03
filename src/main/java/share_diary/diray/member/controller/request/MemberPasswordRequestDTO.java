package share_diary.diray.member.controller.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class MemberPasswordRequestDTO {

    @NotEmpty(message = "비밀번호 정보가 올바르지 않습니다.")
    private String password;

    public MemberPasswordRequestDTO() {
    }

    public MemberPasswordRequestDTO(String password) {
        this.password = password;
    }

    public static MemberPasswordRequestDTO from(String password){
        return new MemberPasswordRequestDTO(password);
    }
}
