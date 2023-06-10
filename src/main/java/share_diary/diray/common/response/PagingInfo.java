package share_diary.diray.common.response;

import lombok.Getter;

@Getter
public abstract class PagingInfo {
    // 토탈 사이즈. ex) 1
    private long totalElements;

    public PagingInfo(long totalElements) {
        this.totalElements = totalElements;
    }
}
