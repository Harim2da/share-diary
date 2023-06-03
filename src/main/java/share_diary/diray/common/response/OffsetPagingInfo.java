package share_diary.diray.common.response;

public class OffsetPagingInfo extends PagingInfo{

    private int totalPages;

    public OffsetPagingInfo(long totalElements, int totalPages) {
        super(totalElements);
        this.totalPages = totalPages;
    }

    public static OffsetPagingInfo of(long totalElements, int totalPages) {
        OffsetPagingInfo pagingInfo = new OffsetPagingInfo(totalElements, totalPages);
        return pagingInfo;
    }
}
