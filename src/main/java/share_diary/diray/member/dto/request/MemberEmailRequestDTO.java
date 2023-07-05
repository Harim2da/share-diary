package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberEmailRequestDTO {

    private String email;

    public MemberEmailRequestDTO() {
    }

    public MemberEmailRequestDTO(String email) {
        this.email = email;
    }

    public MemberEmailRequestDTO from(String email){
        return new MemberEmailRequestDTO(email);
    }
}
