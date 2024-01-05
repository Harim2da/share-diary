package share_diary.diray.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> of(T data){
        return new ApiResponse<>(data);
    }

}
