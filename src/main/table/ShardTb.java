import java.util.List;

/**
 * Created by pengan on 16-9-8.
 * <p/>
 * 仅仅是shard table
 */
public class ShardTb extends AbstractTable {
    public ShardTb(String tbName, List<AbstractCol> cols, List<AbstractIndex> idxs) {
        super(tbName, cols, idxs);
    }

    @Override
    public String getColValue(AbstractCol col, boolean needSpVal) {
        return col.getRandValue();
    }

    /**
     * update in set clause error
     * <p/>
     * <p/>
     * insert without shard key  error
     */

    @Override
    public SqlPair genInsertWithoutKeySql(int recordNum) throws TestCaseException {
        SqlPair pair = super.genInsertWithoutKeySql(recordNum);
        pair.setRstPredict(false);
        return pair;
    }

    @Override
    public SqlPair genUpdateKeyInSetSql(int colValNum) throws TestCaseException {
        SqlPair pair = super.genUpdateKeyInSetSql(colValNum);
        pair.setRstPredict(false);
        return pair;
    }
}
