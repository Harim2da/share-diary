package share_diary.diray.common.utils;

import org.springframework.util.ObjectUtils;

public class Utils {

    public static String arrayToString(Object[] list) {
        if (list == null)
            return "[]";

        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (Object object : list) {
            sb.append(toString(object));
            sb.append(",");
        }
        sb.append("]");

        return sb.toString();
    }

    public static String toString(Object object) {
        return toString(object, 1000);
    }

    public static String toString(Object object, int size) {
        if (object == null)
            return "null";

        String str = object.toString();
        int maxsize = str.length() < size ? str.length() : size;
        return str.substring(0, maxsize);
    }

    public static boolean isNullAndBlank(String str){
        return ObjectUtils.isEmpty(str) || str.trim().equals("");
    }
}
