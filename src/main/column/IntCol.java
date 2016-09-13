import java.util.List;

/**
 * Created by pengan on 16-9-8.
 */
public class IntCol extends AbstractCol {
    public static String COL_SUFFIX = "  bigint(20) ";

    public IntCol(boolean isKeyCol, String colName) {
        super(isKeyCol, colName);
    }

    public IntCol(boolean isKeyCol, String colName, List<String> vals) {
        super(isKeyCol, colName, vals);
    }


    @Override
    public String getColSuffix() {
        return COL_SUFFIX;
    }


    public String getRandValue() {
        return (RAND.nextLong() & Long.MAX_VALUE) + "";
    }
}
