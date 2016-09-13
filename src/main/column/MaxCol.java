/**
 * Created by pengan on 16-9-9.
 */
public class MaxCol extends AbstractCol {
    public MaxCol(String colName) {
        super(false, "MAX(" + colName + ")");
    }

    @Override
    public String getColSuffix() {
        return null;
    }

    @Override
    public String getRandValue() {
        return null;
    }
}
