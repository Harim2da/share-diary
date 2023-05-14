package share_diary.diray.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException{

    private final HttpStatus status;

    public BaseException(HttpStatus status) {
        this.status = status;
    }
}
