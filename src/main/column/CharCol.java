import java.util.List;

/**
 * Created by pengan on 16-9-8.
 */
public class CharCol extends AbstractCol {
    public static String COL_SUFFIX = " varchar(255) ";

    public CharCol(boolean isKeyCol, String colName) {
        super(isKeyCol, colName);
    }

    public CharCol(boolean isKeyCol, String colName, List<String> vals) {
        super(isKeyCol, colName, vals);
    }

    @Override
    public String getColSuffix() {
        return COL_SUFFIX;
    }

    public String getRandValue() {
        StringBuilder value = new StringBuilder();
        long rst = (RAND.nextLong() & Long.MAX_VALUE);
        int index = CHAR.length - 1;
        while (rst > 0) {
            if ((rst & 0x01) == 1) {
                value.append(CHAR[index--]);
            }
            rst = (rst >>> 1);
        }
        value.append(CHAR[0]);
        return value.toString();
    }

}
