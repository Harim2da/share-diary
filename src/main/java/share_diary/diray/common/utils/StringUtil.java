package share_diary.diray.common.utils;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class StringUtil {

    public static boolean isNullAndBlank(String str){
        return ObjectUtils.isEmpty(str) || str.trim().equals("");
    }
}
