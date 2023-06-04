package share_diary.diray.exception.response;

import lombok.Builder;
import lombok.Getter;
import share_diary.diray.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {

    private String code;
    private String message;
    private Map<String,String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName,String errorMessage){
        validation.put(fieldName,errorMessage);
    }

    public static ErrorResponse of(Exception ex,Map<String,String> validation){
        ErrorType errorType = ErrorType.of((Class<? extends BaseException>) ex.getClass());
        return ErrorResponse.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .validation(validation)
                .build();
    }

    public static ErrorResponse of(String code,String message,Map<String,String> validation){
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .validation(validation)
                .build();
    }
}
