
/**
 * Created by pengan on 16-9-9.
 */
public class MinCol extends AbstractCol {


    public MinCol(String colName) {
        super(false, "MIN(" + colName + ")");
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
