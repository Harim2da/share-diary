package share_diary.diray.member.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginIdAndEmailRequestDTO {

    private String loginId;

    private String email;

    @Builder
    public MemberLoginIdAndEmailRequestDTO(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }

    public static MemberLoginIdAndEmailRequestDTO of(String loginId,String email){
        return MemberLoginIdAndEmailRequestDTO.builder()
                .loginId(loginId)
                .email(email)
                .build();
    }
}
