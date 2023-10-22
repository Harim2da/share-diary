package share_diary.diray.memberInviteHistory.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteUpdateRequest {

    @NotBlank
    private InviteAcceptStatus status;
}
