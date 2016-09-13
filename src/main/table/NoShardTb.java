import java.util.List;

/**
 * Created by pengan on 16-9-8.
 * <p/>
 * <p/>
 * no shard table
 */
public class NoShardTb extends AbstractTable {
    private Db db;

    public NoShardTb(String tbName, List<AbstractCol> cols, List<AbstractIndex> idxs) {
        super(tbName, cols, idxs);
    }

    @Override
    public String getColValue(AbstractCol col, boolean needSpVal) {
        return col.getRandValue();
    }

    /**
     * no shard table for all operation is ok
     */

    public Db getDb() {
        return db;
    }

    public void setDb(Db db) {
        this.db = db;
    }

    /**
     * noshard table 需要两次验证 分别是对照库和中间件 物理库和中间件
     *
     * @param jproxyDb
     * @param contrastDb
     * @param pair
     * @param rst
     * @throws TestCaseException
     */
    @Override
    protected void validate(Db jproxyDb, Db contrastDb, SqlPair pair, StringBuilder rst) throws TestCaseException {

        StringBuilder validateRst = new StringBuilder();
        try {
            super.validate(jproxyDb, contrastDb, pair, validateRst);
        } catch (TestCaseException exp) {
            rst.append(validateRst); // 直接将结果拼接上去
            throw exp;
        }

        rst.append("<td>\n<table>\n<tr>"); // 到这里的话需要用table来格式话
        rst.append(validateRst);
        rst.append("</tr>\n");

        validateRst.setLength(0);
        try {
            super.validate(jproxyDb, db, pair, validateRst);
        } catch (TestCaseException exp) {
            throw exp;
        } finally {
            // 到这里已经要换行了
            rst.append("<tr>\n");
            rst.append(validateRst);
            rst.append("</tr>\n</table>\n</td>\n");
        }
    }
}
