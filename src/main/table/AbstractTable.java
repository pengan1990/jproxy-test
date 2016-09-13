import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pengan on 16-9-8.
 */
public abstract class AbstractTable implements TableAction {
    private static final Logger logger = Logger.getLogger(AbstractTable.class);
    protected static final int INIT_NUM = 100;
    protected static final String TABLE_HEAD = "CREATE TABLE IF NOT EXISTS ";
    protected static final String TABLE_TAIL = "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    protected static Random RAND;

    static {
        RAND = new Random();
    }

    protected String tbName;
    protected List<AbstractCol> tbCols;
    protected List<AbstractIndex> idxs;
    protected List<AbstractCol> keyCols;
    protected List<AbstractCol> noKeyCols;
    protected Map<String, AbstractCol> colNameColMap;

    public AbstractTable(String tbName, List<AbstractCol> cols, List<AbstractIndex> idxs) {
        this.tbName = tbName;
        this.tbCols = cols;
        this.idxs = idxs;
        this.keyCols = new LinkedList<AbstractCol>();
        this.noKeyCols = new LinkedList<AbstractCol>();
        this.colNameColMap = new LinkedHashMap<String, AbstractCol>();
        for (AbstractCol col : cols) {
            colNameColMap.put(col.colName, col);
            if (col.isKeyCol) {
                keyCols.add(col);
            } else {
                noKeyCols.add(col);
            }
        }
    }

    public String genCreateTableSql() {
        StringBuilder createSql = new StringBuilder(TABLE_HEAD);
        createSql.append(tbName).append(" ");
        createSql.append("(");
        for (ColAction col : tbCols) {
            createSql.append(col.genCreatePart()).append(",");
        }

        for (AbstractIndex idx : idxs) {
            createSql.append(idx.genCreatePart()).append(",");
        }
        createSql.deleteCharAt(createSql.length() - 1).append(")");
        createSql.append(TABLE_TAIL);
        return createSql.toString();
    }

    @Override
    public SqlPair genInsertWithKeySql(int recordNum) throws TestCaseException {
        SqlPair pair = genInsertSql(true, recordNum, this.tbCols);
        pair.setCallMethod("InsertWithKey");
        return pair;
    }

    @Override
    public SqlPair genInsertWithoutKeySql(int recordNum) throws TestCaseException {
        List<AbstractCol> cols = new LinkedList<AbstractCol>();
        for (AbstractCol col : tbCols) {
            if (col.isKeyCol) {
                continue;
            }
            cols.add(col);
        }

        SqlPair pair = genInsertSql(false, 1, cols);
        pair.setCallMethod("InsertWithoutKey");
        return pair;
    }

    @Override
    public SqlPair genDeleteWithKeySql(int colValNum) throws TestCaseException {
        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        AbstractCol keyCol = getRandKeyColIfExist();
        whereCols.add(keyCol);
        SqlPair pair = genDeleteSql(true, colValNum, whereCols);
        pair.setCallMethod("DeleteWithKey");
        return pair;
    }

    @Override
    public SqlPair genDeleteWithoutKeySql(int colValNum) throws TestCaseException {
        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        List<AbstractCol> cols = new LinkedList<AbstractCol>();
        for (AbstractCol col : tbCols) {
            if (col.isKeyCol) {
                continue;
            }
            cols.add(col);
        }
        AbstractCol keyCol = cols.get((RAND.nextInt() & (cols.size() - 1)));
        whereCols.add(keyCol);
        SqlPair pair = genDeleteSql(false, colValNum, whereCols);
        pair.setCallMethod("DeleteWithoutKey");
        return pair;
    }

    @Override
    public SqlPair genUpdateKeyInSetSql(int colValNum) throws TestCaseException {
        List<AbstractCol> setCols = new LinkedList<AbstractCol>();
        AbstractCol keyCol = getRandKeyColIfExist();
        setCols.add(keyCol);

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        AbstractCol whereCol = this.tbCols.get((RAND.nextInt() & (tbCols.size() - 1)));
        whereCols.add(whereCol);

        SqlPair pair = genUpdateSql(true, colValNum, setCols, whereCols);
        pair.setCallMethod("UpdateKeyInSet");
        return pair;
    }

    @Override
    public SqlPair genUpdateKeyInWhereSql(int colValNum) throws TestCaseException {
        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        AbstractCol whereCol = getRandKeyColIfExist();
        whereCols.add(whereCol);

        List<AbstractCol> setCols = new LinkedList<AbstractCol>();
        AbstractCol setCol = getRandNoKeyCol();
        setCols.add(setCol);

        SqlPair pair = genUpdateSql(true, colValNum, setCols, whereCols);
        pair.setCallMethod("UpdateKeyInWhere");
        return pair;
    }

    @Override
    public SqlPair genUpdateWithoutKeySql(int colValNum) throws TestCaseException {
        List<AbstractCol> setCols = new LinkedList<AbstractCol>();
        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();

        setCols.add(getRandNoKeyCol());
        whereCols.add(getRandNoKeyCol());

        SqlPair pair = genUpdateSql(false, colValNum, setCols, whereCols);
        pair.setCallMethod("UpdateWithoutKey");
        return pair;
    }

    @Override
    public SqlPair genSelectWithKeySql(int colValNum) throws TestCaseException {
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        selectCols.add(getRandKeyColIfExist());
        whereCols.add(getRandKeyColIfExist());
        SqlPair pair = genSelectSql(true, colValNum,
                selectCols,
                whereCols,
                null,
                null);
        pair.setCallMethod("SelectWithKey");
        return pair;
    }

    @Override
    public SqlPair genSelectWithoutKeySql(int colValNum) throws TestCaseException {

        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        selectCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(getRandNoKeyCol());
        SqlPair pair = genSelectSql(false, colValNum, selectCols, whereCols, null, null);
        pair.setCallMethod("SelectWithoutKey");
        return pair;
    }

    @Override
    public SqlPair genSelectMinSql(int colValNum) throws TestCaseException {
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        AbstractCol randCol = tbCols.get((RAND.nextInt() & (tbCols.size() - 1)));
        selectCols.add(new MinCol(randCol.colName));

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        SqlPair pair = genSelectSql(whereCols.get(0).isKeyCol, colValNum, selectCols, whereCols, null, null);
        pair.setCallMethod("SelectMin");
        return pair;
    }

    @Override
    public SqlPair genSelectMaxSql(int colValNum) throws TestCaseException {
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        AbstractCol randCol = tbCols.get((RAND.nextInt() & (tbCols.size() - 1)));
        selectCols.add(new MaxCol(randCol.colName));

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        SqlPair pair = genSelectSql(whereCols.get(0).isKeyCol, colValNum, selectCols, whereCols, null, null);
        pair.setCallMethod("SelectMax");
        return pair;
    }

    @Override
    public SqlPair genSelectCountSql(int colValNum) throws TestCaseException {
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        selectCols.add(new CountCol());

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        SqlPair pair = genSelectSql(whereCols.get(0).isKeyCol, colValNum, selectCols, whereCols, null, null);

        pair.setCallMethod("SelectCount");
        return pair;
    }

    @Override
    public SqlPair genSelectGroupBySql(int colValNum) throws TestCaseException {
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        List<AbstractCol> intCols = new LinkedList<AbstractCol>();
        for (AbstractCol col : tbCols) {
            if (col instanceof IntCol) {
                intCols.add(col);
            }
        }
        AbstractCol selCol = intCols.get((RAND.nextInt() & (intCols.size() - 1)));
        MinCol minCol = new MinCol(selCol.colName);
        MaxCol maxCol = new MaxCol(selCol.colName);
        selectCols.add(minCol);
        selectCols.add(maxCol);

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        List<AbstractCol> groupByCols = new LinkedList<AbstractCol>();
        groupByCols.add(selCol);

        SqlPair pair = genSelectSql(whereCols.get(0).isKeyCol, colValNum, selectCols, whereCols, groupByCols, groupByCols);
        pair.setCallMethod("SelectGroupBy");
        return pair;
    }

    @Override
    public SqlPair genSelectOrderBySql(int colValNum) throws TestCaseException {
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        selectCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        SqlPair pair = genSelectSql(whereCols.get(0).isKeyCol, colValNum, selectCols, whereCols, null, tbCols);
        pair.setCallMethod("SelectOrderBy");
        return pair;
    }

    @Override
    public SqlPair genSelectLimitSql(int colValNum) throws TestCaseException {
        // limit 不能跟group by一起实现
        List<AbstractCol> selectCols = new LinkedList<AbstractCol>();
        selectCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        List<AbstractCol> whereCols = new LinkedList<AbstractCol>();
        whereCols.add(tbCols.get((RAND.nextInt() & (tbCols.size() - 1))));

        SqlPair pair = genSelectSql(whereCols.get(0).isKeyCol, colValNum, selectCols, whereCols, null, tbCols);
        int num1 = (RAND.nextInt() & INIT_NUM);
        int num2 = (RAND.nextInt() & INIT_NUM);
        if (num1 >= num2) {
            int tmp = num2;
            num2 = num1 + 1;
            num1 = tmp;
        }
        String limitSql = pair.getExeSql().remove(0) + " LIMIT "
                + num1 + ", " + num2;
        pair.getExeSql().add(limitSql);

        pair.setValidateSql(limitSql);
        pair.setCallMethod("SelectLimit");
        return pair;
    }

    @Override
    public SqlPair genCommitSql(int colValNum) throws TestCaseException {
        // update insert with key just test for ok
        //
        SqlPair pair = genInsertSql(true, 1, this.tbCols);
        pair = genUpdateByColVal(pair);

        pair.getExeSql().add("COMMIT");
        pair.setValidateSql(genValidateSql(pair));

        pair.setCallMethod("COMMIT");
        return pair;
    }

    protected SqlPair genUpdateByColVal(SqlPair insPair) throws TestCaseException {
        StringBuilder updateSql = new StringBuilder("UPDATE ").append(tbName);
        AbstractCol setCol = getRandNoKeyCol();
        String setColVal = getColValue(setCol, false);
        StringBuilder setPart = new StringBuilder(" SET ");
        setPart.append(setCol.colName).append(" = ").append("\'").append(setColVal).append("\'");
        StringBuilder wherePart = new StringBuilder();
        if (insPair.getColValsMap().size() > 0) {
            wherePart.append(" WHERE ");
            for (Map.Entry<AbstractCol, Set<String>> entry : insPair.getColValsMap().entrySet()) {
                AbstractCol col = entry.getKey();
                wherePart.append(col.colName).append(" IN (");
                for (String colVal : entry.getValue()) {
                    wherePart.append("\'").append(colVal).append("\',");
                }
                wherePart.deleteCharAt(wherePart.length() - 1);
                wherePart.append(") AND ");
            }
            wherePart.deleteCharAt(wherePart.length() - 1);
            wherePart.deleteCharAt(wherePart.length() - 1);
            wherePart.deleteCharAt(wherePart.length() - 1);
            wherePart.deleteCharAt(wherePart.length() - 1);
        }

        updateSql.append(setPart).append(wherePart);
        insPair.getExeSql().add(updateSql.toString());

        return insPair;
    }

    @Override
    public SqlPair genRollbacSql(int colValNum) throws TestCaseException {
        SqlPair pair = genInsertSql(true, 1, this.tbCols);
        pair = genUpdateByColVal(pair);

        pair.getExeSql().add("ROLLBACK");
        pair.setValidateSql(genValidateSql(pair));

        pair.setCallMethod("ROLLBACK");
        return pair;
    }

    public SqlPair genInsertSql(boolean isSpecial, int num, List<AbstractCol> cols) throws TestCaseException {
        if (num < 1) {
            return null;
        }
        Map<AbstractCol, Set<String>> colValsMap = new LinkedHashMap<AbstractCol, Set<String>>();

        List<String> exeSqls = new LinkedList<String>();


        StringBuilder insSql = new StringBuilder("INSERT INTO " + tbName);
        StringBuilder colStr = new StringBuilder("(");
        StringBuilder valStr = new StringBuilder("(");
        for (AbstractCol col : cols) {
            String colValue = getColValue(col, isSpecial);
            colStr.append(col.colName).append(",");
            valStr.append('\'' + colValue + "',");

            if (colValsMap.containsKey(col)) {
                colValsMap.get(col).add(colValue);
            } else {
                Set<String> valSets = new LinkedHashSet<String>();
                valSets.add(colValue);
                colValsMap.put(col, valSets);
            }
        }
        colStr.deleteCharAt(colStr.length() - 1).append(")VALUES");
        valStr.deleteCharAt(valStr.length() - 1).append("),(");

        // 如果需要多条记录
        if (num > 1) {
            for (int idx = 1; idx < num; idx++) {
                for (AbstractCol col : cols) {
                    String colValue = getColValue(col, isSpecial);
                    valStr.append('\'' + colValue + "',");

                    if (colValsMap.containsKey(col)) {
                        colValsMap.get(col).add(colValue);
                    } else {
                        Set<String> valSets = new LinkedHashSet<String>();
                        valSets.add(colValue);
                        colValsMap.put(col, valSets);
                    }
                }
                valStr.deleteCharAt(valStr.length() - 1).append("),(");
            }
        }
        valStr.deleteCharAt(valStr.length() - 1);
        valStr.deleteCharAt(valStr.length() - 1);
        exeSqls.add(insSql.append(colStr).append(valStr).toString());
        SqlPair pair = new SqlPair(exeSqls, colValsMap);
        pair.setValidateSql(genValidateSql(pair));
        return pair;
    }


    /**
     * @param colValsNum: where 列值的数量， 算这么多由于是随机的，可能不到，到那时肯定会计次
     * @param setCols     not null
     * @param whereCols   not null
     * @return
     */
    public SqlPair genUpdateSql(boolean isSpecial, int colValsNum,
                                List<AbstractCol> setCols, List<AbstractCol> whereCols) throws TestCaseException {
        Map<AbstractCol, Set<String>> colValsMap = new LinkedHashMap<AbstractCol, Set<String>>();

        List<String> exeSqls = new LinkedList<String>();

        StringBuilder updateSql = new StringBuilder("UPDATE " + tbName + " ");
        StringBuilder setPart = new StringBuilder(" SET ");
        for (AbstractCol col : setCols) {
            String colValue = getColValue(col, isSpecial);
            setPart.append(col.colName).append(" = ").append("\'").append(colValue).append("\',");

            if (colValsMap.containsKey(col)) {
                colValsMap.get(col).add(colValue);
            } else {
                Set<String> valSets = new LinkedHashSet<String>();
                valSets.add(colValue);
                colValsMap.put(col, valSets);
            }
        }
        setPart.deleteCharAt(setPart.length() - 1);

        StringBuilder wherePart = genWherePart(isSpecial, colValsNum, whereCols, colValsMap);

        updateSql.append(setPart).append(wherePart);
        exeSqls.add(updateSql.toString());

        SqlPair pair = new SqlPair(exeSqls, colValsMap);

        pair.setValidateSql(genValidateSql(pair));
        return pair;
    }

    public SqlPair genDeleteSql(boolean isSpecial, int colValNum, List<AbstractCol> whereCols) throws TestCaseException {
        Map<AbstractCol, Set<String>> colNameValsMap = new LinkedHashMap<AbstractCol, Set<String>>();
        List<String> exeSqls = new LinkedList<String>();

        StringBuilder deleteSqlStr = new StringBuilder("DELETE FROM " + tbName);

        StringBuilder wherePart = genWherePart(isSpecial, colValNum, whereCols, colNameValsMap);

        exeSqls.add(deleteSqlStr.append(wherePart).toString());

        SqlPair pair = new SqlPair(exeSqls, colNameValsMap);
        pair.setValidateSql(genValidateSql(pair));
        return pair;
    }

    public SqlPair genSelectSql(boolean isSpecial, int colValNum,
                                List<AbstractCol> selectCols,
                                List<AbstractCol> whereCols,
                                List<AbstractCol> groupByCols,
                                List<AbstractCol> orderByCols) throws TestCaseException {
        Map<AbstractCol, Set<String>> colValsMap = new LinkedHashMap<AbstractCol, Set<String>>();
        List<String> exeSqls = new LinkedList<String>();

        StringBuilder selectSql = new StringBuilder("SELECT ");
        if (selectCols == null || selectCols.size() == 0) {
            selectSql.append(" * ");
        } else {
            for (AbstractCol col : selectCols) {
                selectSql.append(" ").append(col.colName).append(", ");
            }
            selectSql.deleteCharAt(selectSql.length() - 1);
            selectSql.deleteCharAt(selectSql.length() - 1);
        }

        selectSql.append(" FROM ").append(tbName);

        if (whereCols != null && whereCols.size() > 0) {
            selectSql.append(genWherePart(isSpecial, colValNum, whereCols, colValsMap));
        }

        if (groupByCols != null && groupByCols.size() > 0) {
            StringBuilder groupByPart = new StringBuilder(" GROUP BY ");
            for (AbstractCol col : groupByCols) {
                groupByPart.append(col.colName).append(",");
            }
            groupByPart.deleteCharAt(groupByPart.length() - 1);
            selectSql.append(groupByPart);
        }

        if (orderByCols != null && orderByCols.size() > 0) {
            StringBuilder orderByPart = new StringBuilder(" ORDER BY ");
            for (AbstractCol col : orderByCols) {
                orderByPart.append(col.colName).append(",");
            }
            orderByPart.deleteCharAt(orderByPart.length() - 1);
            selectSql.append(orderByPart);
        }

        exeSqls.add(selectSql.toString()); // 查询语句的校验sql和执行sql是一样的
        SqlPair pair = new SqlPair(exeSqls, colValsMap);

        String[] fields = new String[selectCols.size()];
        int idx = 0;
        for (AbstractCol col : selectCols) { // get select expression list
            fields[idx++] = col.colName;
        }

        pair.setValidateSql(selectSql.toString());
        pair.setFields(fields);
        return pair;
    }

    protected StringBuilder genWherePart(boolean isSpecial,
                                         int colValNum,
                                         List<AbstractCol> whereCols,
                                         Map<AbstractCol, Set<String>> colValsMap) throws TestCaseException {
        if (whereCols == null || whereCols.size() == 0) {
            return new StringBuilder();
        }
        StringBuilder wherePart = new StringBuilder(" WHERE ");
        for (AbstractCol col : whereCols) {
            StringBuilder colValStr = new StringBuilder();
            for (int idx = 0; idx < colValNum; idx++) {
                String colValue = getColValue(col, isSpecial);
                colValStr.append("\'").append(colValue).append("\',");
                if (colValsMap.containsKey(col)) {
                    colValsMap.get(col).add(colValue);
                } else {
                    Set<String> valSets = new LinkedHashSet<String>();
                    valSets.add(colValue);
                    colValsMap.put(col, valSets);
                }
            }
            colValStr.deleteCharAt(colValStr.length() - 1);
            wherePart.append(col.colName).append(" IN (").append(colValStr).append(") AND ");
        }
        wherePart.deleteCharAt(wherePart.length() - 1);
        wherePart.deleteCharAt(wherePart.length() - 1);
        wherePart.deleteCharAt(wherePart.length() - 1);
        wherePart.deleteCharAt(wherePart.length() - 1);
        return wherePart;
    }

    protected String genValidateSql(SqlPair pair) {
        Map<AbstractCol, Set<String>> colValsMap = pair.getColValsMap();
        if (colValsMap == null || colValsMap.size() == 0) {
            return null;
        }
        StringBuilder selSql = new StringBuilder("SELECT ");
        String[] fields = new String[tbCols.size()];
        int fieldIdx = 0;
        for (AbstractCol col : tbCols) {
            selSql.append(col.colName).append(",");
            fields[fieldIdx++] = col.colName;
        }
        selSql.deleteCharAt(selSql.length() - 1).append(" FROM ").append(tbName);
        StringBuilder wherePart = new StringBuilder(" WHERE ");
        StringBuilder valPart = new StringBuilder();
        StringBuilder orderBy = new StringBuilder(" ORDER BY ");
        for (Map.Entry<AbstractCol, Set<String>> entry : colValsMap.entrySet()) {
            wherePart.append(entry.getKey().colName).append(" IN ").append("(");
            for (String val : entry.getValue()) {
                valPart.append("\'").append(val).append("\',");
            }
            valPart.deleteCharAt(valPart.length() - 1);
            wherePart.append(valPart).append(") AND ");
            valPart.setLength(0);
        }
        // remove "AND "
        wherePart.deleteCharAt(wherePart.length() - 1);
        wherePart.deleteCharAt(wherePart.length() - 1);
        wherePart.deleteCharAt(wherePart.length() - 1);
        wherePart.deleteCharAt(wherePart.length() - 1);

        for (AbstractCol col : tbCols) {
            orderBy.append(col.colName).append(",");
        }

        orderBy.deleteCharAt(orderBy.length() - 1);

        pair.setFields(fields); // 这里需要设置返回的列的名称
        return selSql.append(wherePart).append(orderBy).toString();
    }

    /**
     * 不同的表类型 以及列类型来决定 列值该如何选择
     *
     * @param col
     * @return
     */
    public abstract String getColValue(AbstractCol col, boolean needSpVal) throws TestCaseException;


    /**
     * then we let each
     */
    public List<SqlPair> getAllPairs() {
        List<SqlPair> pairs = new LinkedList<SqlPair>();
        try {
            for (Method method : TableAction.class.getDeclaredMethods()) {
                pairs.add((SqlPair) method.invoke(this, 1));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return pairs;
    }

    public Map<String, AbstractCol> getColNameColMap() {
        return colNameColMap;
    }

    protected AbstractCol getRandKeyColIfExist() {
        logger.info("get rand key column if exits");
        AbstractCol keyCol;
        if (keyCols == null || keyCols.size() == 0) {
            logger.warn("no key cols");
            keyCol = tbCols.get((RAND.nextInt() & (tbCols.size() - 1)));
        } else {
            keyCol = this.keyCols.get((RAND.nextInt() & (keyCols.size() - 1)));
        }
        return keyCol;
    }

    public AbstractCol getRandNoKeyCol() {
        return noKeyCols.get((RAND.nextInt() & (noKeyCols.size() - 1)));
    }

    protected void validate(Db jproxyDb, Db contrastDb,
                            SqlPair pair,
                            StringBuilder rst) throws TestCaseException {
        logger.info("validate sql is:" + pair.getValidateSql());
        rst.append("<td>\n<table>\n<tr>\n");
        if (pair.getValidateSql() == null || pair.getValidateSql().charAt(0) != 'S') {
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // JPROXY DB 没有执行
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // contrast db 没有执行
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // 没有比较结果
            rst.append("</tr>\n</table>\n</td>");
            return;
        }

        List<List<String>> jproxyRst;
        List<List<String>> contrastRst;
        try {
            jproxyRst = jproxyDb.executeQuery(pair.getValidateSql(), pair.getFields());
            rst.append("<td>").append(Constants.SUCCESS).append("</td>\n");
        } catch (SQLException exp) {
            System.err.println(pair.getValidateSql());
            rst.append("<td>").append(Constants.FAILURE).append("</td>\n");
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // contrast db 没有执行
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // 没有比较结果
            rst.append("</tr>\n</table>\n</td>");
            throw new TestCaseException(TestCaseException.ExceptCase.JPROXY_DB_EXECUTE_ERROR,
                    exp.getLocalizedMessage());
        }
        try {
            contrastRst = contrastDb.executeQuery(pair.getValidateSql(), pair.getFields());
            rst.append("<td>").append(Constants.SUCCESS).append("</td>\n");
        } catch (SQLException exp) {
            rst.append("<td>").append(Constants.FAILURE).append("</td>\n");
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // 没有比较结果
            rst.append("</tr>\n</table>\n</td>");
            throw new TestCaseException(TestCaseException.ExceptCase.CONTRAST_DB_EXECUTE_ERROR,
                    exp.getLocalizedMessage());
        }
        if (RstContrast.compare(jproxyRst, contrastRst)) {
            rst.append("<td>").append(Constants.SUCCESS).append("</td>\n"); // 结果比较成功
            rst.append("</tr>\n</table>\n</td>");
        } else {
            rst.append("<td>").append(Constants.FAILURE).append("</td>\n"); // 结果比较失败
            rst.append("</tr>\n</table>\n</td>");
            StringBuilder exceptMsg = catInConsistMsg(jproxyRst, contrastRst, pair.getFields());
            throw new TestCaseException(TestCaseException.ExceptCase.INCONSISTENT_QUERY_RESULT,
                    exceptMsg.toString());
        }
    }

    private StringBuilder catInConsistMsg(List<List<String>> jproxyRst, List<List<String>> contrastRst,
                                          String[] keyFields) {
        StringBuilder exceptMsg = new StringBuilder("<table border=1>\n");
        exceptMsg.append("<tr>");
        for (String field : keyFields) {
            exceptMsg.append("<th colspan=2>").append(field).append("</th>\n");
        }
        exceptMsg.append("</tr>"); // first row end

        int idx = 0;
        StringBuilder sqlRst = new StringBuilder(); // sql 查询结果集
        StringBuilder rowStr = new StringBuilder();
        while (idx < jproxyRst.size() && idx < contrastRst.size()) {
            rowStr.append("<tr>");
            List<String> jproxyRow = jproxyRst.get(idx);
            List<String> contrastRow = contrastRst.get(idx);
            for (int colIdx = 0; colIdx < jproxyRow.size(); colIdx++) {
                rowStr.append("<td>").append(jproxyRow.get(colIdx)).append("</td>\n");
                rowStr.append("<td>").append(contrastRow.get(colIdx)).append("</td>\n");
            }
            rowStr.append("</tr>\n");

            sqlRst.append(rowStr);
            rowStr.setLength(0);
            idx++;
        }

        if (idx != jproxyRst.size()) { // append the leave rows
            int jproxyRowIdx = idx;
            while (jproxyRowIdx < jproxyRst.size()) {
                rowStr.append("<tr>");
                List<String> jproxyRow = jproxyRst.get(jproxyRowIdx);
                for (int colIdx = 0; colIdx < jproxyRow.size(); colIdx++) {
                    rowStr.append("<td>").append(jproxyRow.get(colIdx)).append("</td>\n");
                    rowStr.append("<td>").append("</td>\n"); // 空列
                }
                rowStr.append("</tr>\n");
                sqlRst.append(rowStr);

                rowStr.setLength(0);
                jproxyRowIdx++;
            }
        }

        if (idx != contrastRst.size()) { // append the leave rows
            int contrastRowIdx = idx;
            while (contrastRowIdx < contrastRst.size()) {
                rowStr.append("<tr>");
                List<String> contrastRow = contrastRst.get(contrastRowIdx);
                for (int colIdx = 0; colIdx < contrastRow.size(); colIdx++) {
                    rowStr.append("<td>").append("</td>\n"); // 空列
                    rowStr.append("<td>").append(contrastRow.get(colIdx)).append("</td>\n");
                }
                rowStr.append("</tr>\n");
                sqlRst.append(rowStr);

                rowStr.setLength(0);
                contrastRowIdx++;
            }
        }

        exceptMsg.append(sqlRst);
        exceptMsg.append("</table>\n");
        return exceptMsg;
    }
}
