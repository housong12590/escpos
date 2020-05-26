package com.xiaom.pos4j.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hous
 */
public class ListUtils {

    public static <T> List<T> partition(List<T> src, int page, int pageSize) {
        int fromIndex = (page < 1 ? 1 : page - 1) * pageSize;
        int toIndex = fromIndex + pageSize;
        if (fromIndex >= src.size()) {
            return new ArrayList<>();
        }
        if (toIndex > src.size()) {
            toIndex = src.size();
        }
        return src.subList(fromIndex, toIndex);
    }

    public static List<byte[]> splitArray(byte[] bytes, int len) {
        List<byte[]> _list = new ArrayList<>();
        int position = 0;
        if (bytes.length > len) {
            int count = bytes.length / len + (bytes.length % len == 0 ? 0 : 1);
            for (int i = 0; i < count; i++) {
                if (bytes.length - position < len) {
                    len = bytes.length - position;
                }
                byte[] new_arr = new byte[len];
                System.arraycopy(bytes, position, new_arr, 0, len);
                _list.add(new_arr);
                position += len;
            }
        } else {
            _list.add(bytes);
        }
        return _list;
    }


    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
