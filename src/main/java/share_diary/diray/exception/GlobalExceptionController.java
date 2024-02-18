package share_diary.diray.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import share_diary.diray.exception.response.ErrorResponse;
import share_diary.diray.exception.response.ErrorType;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BaseException.class})
    protected ResponseEntity<Object> handlerBaseException(BaseException ex,WebRequest request){
        ErrorType errorType = ex.getErrorType();

        String code = errorType.getCode();
        String message = errorType.getMessage();

        log.info("Exception code={},message={}",code,message);

        ErrorResponse errorResponse = ErrorResponse.of(ex);
        return handleExceptionInternal(ex,errorResponse,null,ex.getStatus(),request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String code = String.valueOf(HttpStatus.BAD_REQUEST.value());
        String message = HttpStatus.BAD_REQUEST.getReasonPhrase();

        log.info("Exception code={},message={}",code,message);

        Map<String, String> validation = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        ErrorResponse errorResponse = ErrorResponse.of(code,message,validation);
        return handleExceptionInternal(ex,errorResponse,headers,status,request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}