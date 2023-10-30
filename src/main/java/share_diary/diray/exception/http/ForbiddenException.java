package share_diary.diray.exception.http;

import org.springframework.http.HttpStatus;
import share_diary.diray.exception.BaseException;

public class ForbiddenException extends BaseException {

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }
}
