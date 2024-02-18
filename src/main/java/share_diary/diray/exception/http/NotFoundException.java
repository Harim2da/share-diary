package share_diary.diray.exception.http;

import org.springframework.http.HttpStatus;
import share_diary.diray.exception.BaseException;

public class NotFoundException extends BaseException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
