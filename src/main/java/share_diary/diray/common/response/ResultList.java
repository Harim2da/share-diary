package share_diary.diray.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResultList<T> {

    private T result;

    private int size;

    public ResultList(T result,int size) {
        this.result = result;
        this.size = size;
    }
}
