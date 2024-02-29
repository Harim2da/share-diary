package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class MemberPasswordUpdateDTO {

    @NotEmpty(message = "비밀번호 정보가 올바르지 않습니다.")
    private String password;
    @NotEmpty(message = "변경 비밀번호 정보가 올바르지 않습니다.")
    private String updatePassword;

    public MemberPasswordUpdateDTO() {
    }

    public MemberPasswordUpdateDTO(String password, String updatePassword) {
        this.password = password;
        this.updatePassword = updatePassword;
    }

    public static MemberPasswordUpdateDTO of(String password,String updatePassword){
        return new MemberPasswordUpdateDTO(password,updatePassword);
    }
}
