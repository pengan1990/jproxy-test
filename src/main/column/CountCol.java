/**
 * Created by pengan on 16-9-9.
 */
public class CountCol extends AbstractCol {
    public CountCol() {
        super(false, "COUNT(*)");
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
