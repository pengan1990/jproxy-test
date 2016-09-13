import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pengan on 16-9-9.
 */
public class SqlPair {
    // true represent success and false represent failure
    private boolean rstPredict = true;  // default success
    private String validateSql;
    private String callMethod;
    private String[] fields;
    private List<String> exeSql;
    private Map<AbstractCol, Set<String>> colValsMap;

    public SqlPair(List<String> exeSql, Map<AbstractCol, Set<String>> colNameValuesMap) {
        this.exeSql = exeSql;
        this.colValsMap = colNameValuesMap;
    }

    public Map<AbstractCol, Set<String>> getColValsMap() {
        return colValsMap;
    }

    public List<String> getExeSql() {
        return exeSql;
    }

    public String getValidateSql() {
        return validateSql;
    }

    public void setValidateSql(String validateSql) {
        this.validateSql = validateSql;
    }

    public boolean isRstPredict() {
        return rstPredict;
    }

    public void setRstPredict(boolean rstPredict) {
        this.rstPredict = rstPredict;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(String callMethod) {
        this.callMethod = callMethod;
    }

    /**
     * merge just for @exeSql and @colValsMap
     * @param pair
     */
    public void merge(SqlPair pair) {
        this.exeSql.addAll(pair.exeSql);
        for (Map.Entry<AbstractCol, Set<String>> entry : pair.colValsMap.entrySet()) {
            if (this.colValsMap.containsKey(entry.getKey())) {
                this.colValsMap.get(entry.getKey()).addAll(entry.getValue());
                continue;
            }
            this.colValsMap.put(entry.getKey(), entry.getValue());
        }
    }
}
