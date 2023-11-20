package share_diary.diray.exception.http;

import org.springframework.http.HttpStatus;
import share_diary.diray.exception.BaseException;

public class InternalServerException extends BaseException {

    public InternalServerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
