import java.util.Date;
import java.util.List;

/**
 * Created by pengan on 16-9-8.
 */
public class DateCol extends AbstractCol {
    public static String COL_SUFFIX = " timestamp DEFAULT '0000-00-00 00:00:00'";

    public DateCol(boolean isKeyCol, String colName) {
        super(isKeyCol, colName);
    }

    public DateCol(boolean isKeyCol, String colName, List<String> vals) {
        super(isKeyCol, colName, vals);
    }

    @Override
    public String getColSuffix() {
        return COL_SUFFIX;
    }

    public String getRandValue() {
        return SDF.format(new Date((RAND.nextLong() & MAX_TIME) + 1000L));
    }
}
