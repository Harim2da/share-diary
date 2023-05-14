package share_diary.diray.exception.http;

import org.springframework.http.HttpStatus;
import share_diary.diray.exception.BaseException;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
