package share_diary.diray.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberIdAndEmailRequestDTO {

    private String loginId;

    private String email;

    @Builder
    public MemberIdAndEmailRequestDTO(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }
}
