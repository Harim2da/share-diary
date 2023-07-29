package share_diary.diray.memberInviteHistory.controller.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

@Getter
public class InviteUpdateRequest {

    @NotBlank
    private InviteAcceptStatus status;
}
