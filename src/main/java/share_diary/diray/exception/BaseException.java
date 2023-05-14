package share_diary.diray.exception;

import org.springframework.http.HttpStatus;
import share_diary.diray.exception.response.ErrorType;

public class BaseException extends RuntimeException{

    private final HttpStatus status;
    private final ErrorType errorType = ErrorType.of(this.getClass());

    public BaseException(HttpStatus status) {
        this.status = status;
    }
}
