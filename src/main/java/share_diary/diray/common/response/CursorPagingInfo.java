package share_diary.diray.common.response;

public class CursorPagingInfo extends PagingInfo{
    private String prev;
    private String next;

    public CursorPagingInfo(long totalElements, String prev, String next) {
        super(totalElements);
        this.prev = prev;
        this.next = next;
    }

    public static CursorPagingInfo of(long totalElements, String prev, String next) {
        CursorPagingInfo pagingInfo = new CursorPagingInfo(totalElements, prev, next);
        return pagingInfo;
    }
}
