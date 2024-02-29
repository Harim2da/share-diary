package share_diary.diray.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, T data) {
        this.code = status.value();
        this.status = status;
        this.message = status.name();
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status,T data) {
        return new ApiResponse<>(status,data);
    }

    public static <T> ApiResponse<T> ok(T data){
        return ApiResponse.of(HttpStatus.OK,data);
    }

}
