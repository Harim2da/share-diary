package share_diary.diray.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BooleanResponse {

    private boolean success;

    public BooleanResponse(boolean success) {
        this.success = success;
    }

    public static BooleanResponse of(boolean success){
        return new BooleanResponse(success);
    }
}
