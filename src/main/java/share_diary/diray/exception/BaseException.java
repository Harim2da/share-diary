package share_diary.diray.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import share_diary.diray.exception.response.ErrorType;

@Getter
public class BaseException extends RuntimeException{

    private final HttpStatus status;
    private final ErrorType errorType = ErrorType.of(this.getClass());

    public BaseException(HttpStatus status) {
        this.status = status;
    }
}
