import java.util.List;

/**
 * Created by pengan on 16-9-8.
 */
public class KeyIndex extends AbstractIndex {

    public KeyIndex(String idxName, List<String> idxColNames) {
        super(idxName, idxColNames);
    }

    public KeyIndex(List<String> idxColNames) {
        super(idxColNames);
    }

    @Override
    public String genCreatePart() {
        StringBuilder unionIdx = new StringBuilder();
        StringBuilder name = new StringBuilder(idxName);
        for (String colName : this.idxColNames) {
            unionIdx.append(colName + ",");
        }
        name.append("idx");
        unionIdx.deleteCharAt(unionIdx.length() - 1);
        return " KEY " + name + '(' + unionIdx + ')';
    }
}
