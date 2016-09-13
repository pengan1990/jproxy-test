import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by pengan on 16-9-8.
 *
 *
 * rule special table
 *
 * 带拆分规则的路由表
 *
 * special_route table 当中的值意外的值 按照正常的拆分规则计算处理
 *
 */
public class RuleSpTb extends AbstractTable {
    private static final Logger logger = Logger.getLogger(RuleSpTb.class);
    private Map<String, Db> colValDbMap;
    public RuleSpTb(String tbName, List<AbstractCol> cols, List<AbstractIndex> idxs) {
        super(tbName, cols, idxs);
        this.colValDbMap = new LinkedHashMap<String, Db>();
    }

    @Override
    public String getColValue(AbstractCol col, boolean needSpVal) throws TestCaseException {
        return col.getRandValue(needSpVal);
    }
    // for rule special table
    /**
     * just like shard not carry special key for inserting error
     *
     * update shard key in set clause
     *
     *
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

    public Map<String, Db> getColValDbMap() {
        return colValDbMap;
    }

    public void setColValDbMap(Map<String, Db> colValDbMap) {
        this.colValDbMap = colValDbMap;
    }

    @Override
    protected void validate(Db jproxyDb, Db contrastDb, SqlPair pair, StringBuilder rst) throws TestCaseException {
        /**
         * if an only if all key values are mapped to one data node
         */
        Set<Db> val2Dbs = new LinkedHashSet<Db>();
        for (Map.Entry<AbstractCol, Set<String>> entry : pair.getColValsMap().entrySet()) {
            AbstractCol col = entry.getKey();
            if (col.isKeyCol) {
                for (String colVal : entry.getValue()) {
                    val2Dbs.add(colValDbMap.get(colVal));
                }
            }
        }

        if (val2Dbs.size() == 1) {
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
                super.validate(jproxyDb, (Db) val2Dbs.toArray()[0], pair, validateRst);
            } catch (TestCaseException exp) {
                throw exp;
            } finally {
                // 到这里已经要换行了
                rst.append("<tr>\n");
                rst.append(validateRst);
                rst.append("</tr>\n</table>\n</td>\n");
            }
        } else {
            super.validate(jproxyDb, contrastDb, pair, rst);
        }
    }
}
