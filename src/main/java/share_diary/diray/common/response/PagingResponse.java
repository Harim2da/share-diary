package share_diary.diray.common.response;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PagingResponse<T> {

    protected PagingInfo paging;
    protected List<T> data;

    /**
     * 오프셋 기반 페이징 방식의 응답 객체
     * @param pagingData Page<T> 페이징된 데이터
     * @return
     * @param <T>
     */
    public static <T> PagingResponse<T> ofOffset(@NotNull Page<T> pagingData) {
        OffsetPagingInfo pagingInfo = OffsetPagingInfo.of(pagingData.getTotalElements(), pagingData.getTotalPages());

        PagingResponse<T> pagingResponse = new PagingResponse();
        pagingResponse.paging = pagingInfo;
        pagingResponse.data = pagingData.getContent();
        return pagingResponse;
    }

    /**
     * 커서 기반 페이징 방식의 응답 객체
     * prev, next 는 링크일지 id 일지는 추후 페이징 방식이 결정되면 재정의 해서 사용하도록 합시다.
     * @param data 결과
     * @param prev 이전 페이징 링크
     * @param next 다음 페이지 링크
     * @return
     * @param <T>
     */
    public static <T> PagingResponse<T> ofCursor(@NotNull List<T> data, String prev, String next) {
        CursorPagingInfo pagingInfo = CursorPagingInfo.of(data.size(), prev, next);

        PagingResponse pagingResponse = new PagingResponse();
        pagingResponse.paging = pagingInfo;
        pagingResponse.data = data;
        return pagingResponse;
    }
}
