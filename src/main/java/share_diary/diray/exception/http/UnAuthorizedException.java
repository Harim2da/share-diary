package share_diary.diray.exception.http;

import org.springframework.http.HttpStatus;
import share_diary.diray.exception.BaseException;

public class UnAuthorizedException extends BaseException {
    public UnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}