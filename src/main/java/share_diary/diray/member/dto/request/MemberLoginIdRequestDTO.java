package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class MemberLoginIdRequestDTO {

    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;

    public MemberLoginIdRequestDTO() {
    }

    public MemberLoginIdRequestDTO(String loginId) {
        this.loginId = loginId;
    }

    public MemberLoginIdRequestDTO from(String loginId){
        return new MemberLoginIdRequestDTO(loginId);
    }
}
