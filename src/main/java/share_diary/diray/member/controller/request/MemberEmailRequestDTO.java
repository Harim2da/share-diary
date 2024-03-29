package share_diary.diray.member.controller.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class MemberEmailRequestDTO {

    @NotEmpty(message = "email 정보가 올바르지 않습니다.")
    private String email;

    public MemberEmailRequestDTO() {
    }

    public MemberEmailRequestDTO(String email) {
        this.email = email;
    }

    public static MemberEmailRequestDTO of(String email){
        return new MemberEmailRequestDTO(email);
    }
}
