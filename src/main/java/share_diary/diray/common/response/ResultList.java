package share_diary.diray.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResultList<T> {

    private List<T> result;

    private int size;

    public ResultList(List<T> result) {
        this.result = result;
        this.size = result.size();
    }
}
