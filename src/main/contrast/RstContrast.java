import java.util.Iterator;
import java.util.List;

/**
 * Created by pengan on 16-9-1.
 */
public class RstContrast {
    public static boolean compare(List<List<String>> rst1, List<List<String>> rst2) {
        Iterator<List<String>> recordsIter1 = rst1.iterator();
        Iterator<List<String>> recordsIter2 = rst2.iterator();
        List<String> vals1;
        List<String> vals2;
        if (rst1.size() != rst2.size()) {
            return false;
        }

        if (rst1.size() == 0) {
            return true;
        }

        while ((recordsIter1.hasNext() && (vals1 = recordsIter1.next()) != null) &&
                (recordsIter2.hasNext() && (vals2 = recordsIter2.next()) != null)) {
            if (vals1.size() != vals2.size()) {
                return false;
            }
            Iterator<String> colValIter1 = vals1.iterator();
            Iterator<String> colValIter2 = vals2.iterator();
            String val1;
            String val2;
            // 迭代list 判断获取的值是否一致
            while ((colValIter1.hasNext() && (val1 = colValIter1.next()) != null) &&
                    (colValIter2.hasNext() && (val2 = colValIter2.next()) != null)) {
                if (val1.equals(val2)) {
                    continue;
                }
                return false;
            }
        }
        if ((recordsIter1.hasNext() && recordsIter1.next() != null) ||
                (recordsIter2.hasNext() && recordsIter2.next() != null)) {
            return false;
        }
        return true;
    }
}
