package share_diary.diray.member.dto.response;

import lombok.Getter;

@Getter
public class MemberValidationEmailResponseDTO {

    private boolean validationEmail;

    public MemberValidationEmailResponseDTO(boolean validationEmail) {
        this.validationEmail = validationEmail;
    }
}
