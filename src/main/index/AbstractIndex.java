import java.util.List;

/**
 * Created by pengan on 16-9-8.
 */
public abstract class AbstractIndex implements IndexAction {

    protected String idxName;
    protected List<String> idxColNames;

    public AbstractIndex(String idxName, List<String> idxColNames) {
        this.idxName = idxName;
        this.idxColNames = idxColNames;
    }

    public AbstractIndex(List<String> idxColNames) {
        this.idxColNames = idxColNames;
        StringBuilder idxBuilder = new StringBuilder();
        for (String colName : idxColNames) {
            int last = colName.length();
            idxBuilder.append(colName.charAt(0));
            idxBuilder.append(colName.charAt(last - 1));
            idxBuilder.append("_");
        }
        idxBuilder.append("idx");
        this.idxName = idxBuilder.toString();
    }

}
