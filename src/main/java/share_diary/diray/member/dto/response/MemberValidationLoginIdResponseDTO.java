package share_diary.diray.member.dto.response;

import lombok.Getter;

@Getter
public class MemberValidationLoginIdResponseDTO {

    private boolean validationLoginId;

    public MemberValidationLoginIdResponseDTO(boolean validationLoginId) {
        this.validationLoginId = validationLoginId;
    }
}
