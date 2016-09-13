import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by pengan on 16-9-9.
 */
public class InitStruct {
    private static final Logger logger = Logger.getLogger(InitStruct.class);
    Db metaDb;
    List<List<String>> instDbRels; // mysql instance db relation

    List<List<String>> shardTbNameDbRels; // shard table name db relations

    List<List<String>> noShardTbNameDbRels; // noshard table name db relations

    List<List<String>> spTbNameDbRels; // special tb name db relations

    List<List<String>> shardTbColRels; // shard table col relations

    List<List<String>> ruleSpTbColRels; // rule special table col relations

    List<List<String>> noRuleSpTbColRels; // no rule special table col relations

    List<List<String>> noshardTbColRels; // no shard table col relations

    List<List<String>> noRuleSpTbColValDbRels; // no rule special table column value db relation

    List<List<String>> ruleSpTbColValDbRels; // rule special table column value db relation

    Map<String, AbstractTable> tbNameTableMap; // <table name, AbstractTable> 所有的表记录都在这里
    Map<String, Db> dbNameDbMap; // <db name, Db> 确认拆分键 可以到指定库上查询
    Map<String, Map<String, Db>> noRuleSpTbColValDbMap; // <special table name with no rule, <column value, db>>
    Map<String, Map<String, Db>> ruleSpTbColValDbMap; // rule special table column value db map
    Map<String, Db> noshardTbDbMap;

    public InitStruct(Db metaDb) {
        this.metaDb = metaDb;
    }

    /**
     * 加载元数据信息 并处理
     * <p/>
     * 表与库的对应信息
     * <p/>
     * 表与列的对应信息
     * <p/>
     * 整理成： tb-> db; tb-> tbCols; colValues-> Db
     */
    private void loadMetaData() throws SQLException {
        instDbRels = metaDb.executeQuery(Constants.QUERY_SCHEMA_SQL,
                Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA);

        // db table name map
        shardTbNameDbRels = metaDb.executeQuery(Constants.QUERY_SHARD_TABLE_DB_RELATION,
                Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME);

        // db no shard table name map
        noShardTbNameDbRels = metaDb.executeQuery(Constants.QUERY_NO_SHARD_TABLE_DB_RELATION,
                Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME);

        // db special table name map
        spTbNameDbRels =
                metaDb.executeQuery(Constants.QUERY_SP_TABLE_DB_RELATION,
                        Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME);

        // shard table column map
        shardTbColRels = metaDb.executeQuery(Constants.QUERY_SHARD_TABLE_COLUMN,
                Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_TYPE);

        // special table with rule column map
        ruleSpTbColRels = metaDb.executeQuery(Constants.QUERY_RULE_SP_TABLE_COLUMNS,
                Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE);

        // special table with no rule column map
        noRuleSpTbColRels = metaDb.executeQuery(Constants.QUERY_NO_RULE_SP_TABLE_COLUMNS,
                Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE);

        // no shard table column map
        noshardTbColRels = metaDb.executeQuery(Constants.QUERY_NO_SHARD_TABLE_COLUMN,
                Constants.TABLE_NAME);

        noRuleSpTbColValDbRels = metaDb.executeQuery(Constants.QUERY_NO_RULE_SP_TABLE_COLVALUE_DB_RELATIONS,
                Constants.URL, Constants.USER, Constants.PASSWORD,
                Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE);

        ruleSpTbColValDbRels = metaDb.executeQuery(Constants.QUERY_RULE_SP_TABLE_COLVALUE_DB_RELATIONS,
                Constants.URL, Constants.USER, Constants.PASSWORD,
                Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE);
        assembleMetaData();

        setColValMap2Table();
    }

    /**
     * set column value to db using for @validate() method
     */
    private void setColValMap2Table() {
        // for no rule special table
        for (Map.Entry<String, Map<String, Db>> entry : noRuleSpTbColValDbMap.entrySet()) {
            ((NoRuleSpTb)tbNameTableMap.get(entry.getKey())).setColValDbMap(entry.getValue());
        }

        // for rule with special table
        for (Map.Entry<String, Map<String, Db>> entry : ruleSpTbColValDbMap.entrySet()) {
            ((RuleSpTb)tbNameTableMap.get(entry.getKey())).setColValDbMap(entry.getValue());
        }

        // for no shard table
        for (Map.Entry<String, Db> entry : noshardTbDbMap.entrySet()) {
            ((NoShardTb)tbNameTableMap.get(entry.getKey())).setDb(entry.getValue());
        }
    }

    /**
     * 组装元数据信息
     */
    private void assembleMetaData() {
        dbNameDbMap = new LinkedHashMap<String, Db>();
        tbNameTableMap = new LinkedHashMap<String, AbstractTable>();

        // assemble special route table with rule
        for (List<String> record : ruleSpTbColRels) {
            // Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE
            String tableName = record.get(0);
            String colName = record.get(1);
            String colVal = record.get(2);
            AbstractTable table = null;
            if (tbNameTableMap.containsKey(tableName)) {
                table = tbNameTableMap.get(tableName);
                table.getColNameColMap().get(colName).addColVal(colVal);
            } else {
                List<AbstractCol> tbCols = new LinkedList<AbstractCol>();
                IntCol intCol = new IntCol(true, colName, new LinkedList<String>());

                tbCols.addAll(genColums());
                tbCols.add(intCol);
                List<AbstractIndex> indexs = new LinkedList<AbstractIndex>();
                List<String> indexCols = new LinkedList<String>();
                indexCols.add(colName);
                indexs.add(genIndex(intCol));
                indexs.add(genIndex(tbCols.get(0)));

                table = new RuleSpTb(tableName, tbCols, indexs);
                intCol.addColVal(colVal);
                tbNameTableMap.put(tableName, table);
                logger.info("ruleSpTbColRels : " + table);
                logger.info(intCol);
            }
        }

        // assemble special route table with no rule
        for (List<String> record : noRuleSpTbColRels) {
            // Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE
            String tableName = record.get(0);
            String colName = record.get(1);
            String colVal = record.get(2);
            AbstractTable table = null;
            if (tbNameTableMap.containsKey(tableName)) {
                table = tbNameTableMap.get(tableName);
                table.getColNameColMap().get(colName).addColVal(colVal);
            } else {
                IntCol intCol = new IntCol(true, colName, new LinkedList<String>());
                List<AbstractCol> tbCols = new LinkedList<AbstractCol>();
                List<AbstractIndex> tbIdxs = new LinkedList<AbstractIndex>();
                tbCols.addAll(genColums());
                tbCols.add(intCol);
                tbIdxs.add(genIndex(tbCols.get(0)));
                tbIdxs.add(genIndex(intCol));
                table = new NoRuleSpTb(tableName, tbCols, tbIdxs);


                intCol.addColVal(colVal);
                tbNameTableMap.put(tableName, table);
                logger.info("noRuleSpTbColRels : " + table);
            }
        }

        // assemble no shard table
        for (List<String> record : noshardTbColRels) {
            //  Constants.TABLE_NAME
            String tableName = record.get(0);
            List<AbstractCol> cols = new LinkedList<AbstractCol>();
            cols.addAll(genColums());
            List<AbstractIndex> indexs = new LinkedList<AbstractIndex>();
            indexs.add(genIndex(cols.get(0)));
            indexs.add(genIndex(cols.get(1)));
            AbstractTable table = new NoShardTb(tableName, cols, indexs);
            tbNameTableMap.put(tableName, table);
            logger.info("noshardTbColRels : " + table);
        }

        // assemble shard table
        for (List<String> record : shardTbColRels) {
            // Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_TYPE
            String tableName = record.get(0);
            String colName = record.get(1);
            String colType = record.get(2);

            List<AbstractCol> cols = new LinkedList<AbstractCol>();
            cols.addAll(genColums());

            AbstractCol absCol;
            if (colType.endsWith("Long")) {
                absCol = new IntCol(true, colName);
            } else if (colType.endsWith("Date")) {
                absCol = new DateCol(true, colName);
            } else {
                absCol = new CharCol(true, colName);
            }
            cols.add(absCol);
            List<AbstractIndex> tbIdxs = new LinkedList<AbstractIndex>();
            tbIdxs.add(genIndex(cols.get(0)));
            tbIdxs.add(genIndex(absCol));
            AbstractTable table = new ShardTb(tableName, cols, tbIdxs);

            tbNameTableMap.put(tableName, table);
            logger.info("noshardTbColRels : " + table);
        }

        // assemble db relation to shard table
        for (List<String> record : shardTbNameDbRels) {
            // Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME
            String url = record.get(0);
            String user = record.get(1);
            String password = record.get(2);
            String schema = record.get(3);
            String tableName = record.get(4);
            Db db = null;
            if (dbNameDbMap.containsKey(schema)) {
                db = dbNameDbMap.get(schema);
                db.addTable(tbNameTableMap.get(tableName));
            } else {
                db = new Db(url, user, password, schema);
                db.addTable(tbNameTableMap.get(tableName));
                dbNameDbMap.put(schema, db);
            }
        }

        // assemble db relation to no shard table
        for (List<String> record : noShardTbNameDbRels) {
            // Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME
            String url = record.get(0);
            String user = record.get(1);
            String password = record.get(2);
            String schema = record.get(3);
            String tableName = record.get(4);
            Db db = null;
            if (dbNameDbMap.containsKey(schema)) {
                db = dbNameDbMap.get(schema);
                db.addTable(tbNameTableMap.get(tableName));
            } else {
                db = new Db(url, user, password, schema);
                db.addTable(tbNameTableMap.get(tableName));
                dbNameDbMap.put(schema, db);
            }
        }

        // assemble db relation to special route table
        for (List<String> record : spTbNameDbRels) {
            // Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME
            String url = record.get(0);
            String user = record.get(1);
            String password = record.get(2);
            String schema = record.get(3);
            String tableName = record.get(4);
            Db db = null;
            if (dbNameDbMap.containsKey(schema)) {
                db = dbNameDbMap.get(schema);
                db.addTable(tbNameTableMap.get(tableName));
            } else {
                db = new Db(url, user, password, schema);
                db.addTable(tbNameTableMap.get(tableName));
                dbNameDbMap.put(schema, db);
            }
        }

        noRuleSpTbColValDbMap = new LinkedHashMap<String, Map<String, Db>>();
        Map<String, Db> valDbMap;
        for (List<String> record : noRuleSpTbColValDbRels) {
            // Constants.URL, Constants.USER, Constants.PASSWORD
            // Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE,
            String url = record.get(0);
            String user = record.get(1);
            String password = record.get(2);
            String tableName = record.get(3);
            String colName = record.get(4);
            String colVal = record.get(5);
            if (noRuleSpTbColValDbMap.containsKey(tableName)) {
                valDbMap = noRuleSpTbColValDbMap.get(tableName);
                Db db = new Db(url, user, password, null);
                valDbMap.put(colVal, db);
            } else {
                valDbMap = new LinkedHashMap<String, Db>();
                Db db = new Db(url, user, password, null);
                valDbMap.put(colVal, db);
                noRuleSpTbColValDbMap.put(tableName, valDbMap);
            }
        }

        ruleSpTbColValDbMap = new LinkedHashMap<String, Map<String, Db>>();
        for (List<String> record : ruleSpTbColValDbRels) {
            // Constants.URL, Constants.USER, Constants.PASSWORD
            // Constants.TABLE_NAME, Constants.COLUMN_NAME, Constants.COLUMN_VALUE,
            String url = record.get(0);
            String user = record.get(1);
            String password = record.get(2);
            String tableName = record.get(3);
            String colName = record.get(4);
            String colVal = record.get(5);
            if (ruleSpTbColValDbMap.containsKey(tableName)) {
                valDbMap = ruleSpTbColValDbMap.get(tableName);
                Db db = new Db(url, user, password, null);
                valDbMap.put(colVal, db);
            } else {
                valDbMap = new LinkedHashMap<String, Db>();
                Db db = new Db(url, user, password, null);
                valDbMap.put(colVal, db);
                ruleSpTbColValDbMap.put(tableName, valDbMap);
            }
        }

        noshardTbDbMap = new LinkedHashMap<String, Db>();
        for (List<String> record : noShardTbNameDbRels) {
            // Constants.URL, Constants.USER, Constants.PASSWORD, Constants.SCHEMA, Constants.TABLE_NAME
            String url = record.get(0);
            String user = record.get(1);
            String password = record.get(2);
            String schema = record.get(3);
            String tableName = record.get(4);
            noshardTbDbMap.put(tableName, new Db(url, user, password, schema));
        }
    }

    /**
     * 生成 列
     *
     * @return
     */
    private List<AbstractCol> genColums() {
        List<AbstractCol> absCols = new LinkedList<AbstractCol>();
        int num = 5;
        IntCol id = new IntCol(false, "table_id");
        absCols.add(id);

        AbstractCol col;
        for (int idx = 0; idx < num; idx++) {

            if (idx % 3 == 0) {
                col = new IntCol(false, "int_" + idx);
            } else if (idx % 4 == 0) {
                col = new DateCol(false, "date_" + idx);
            } else {
                col = new CharCol(false, "varchar_" + idx);
            }
            absCols.add(col);
        }
        return absCols;
    }

    /**
     * 生成索引
     *
     * @param idxCols
     * @return
     */
    private AbstractIndex genIndex(AbstractCol... idxCols) {
        List<String> columns = new LinkedList<String>();
        for (AbstractCol col : idxCols) {
            columns.add(col.colName);
        }
        return new KeyIndex(columns);
    }

    /**
     * 获取元数据信息
     * <p/>
     * 初始化库表结构
     */
    public void initStruct(Db contrastDb) throws TestCaseException {
        try {
            loadMetaData();
        } catch (SQLException exp) {
            logger.error(exp.getLocalizedMessage());
            throw new TestCaseException(TestCaseException.ExceptCase.LOAD_META_INFO_ERROR,
                    exp.getLocalizedMessage());
        }
        initMysqlDbStruct();
        initMysqlTbStruct();

        initContrastDbStruct(contrastDb);
        initContrastTbStruct(contrastDb);
    }

    private void initContrastTbStruct(Db contrastDb) throws TestCaseException {
        for (AbstractTable table : tbNameTableMap.values()) {
            contrastDb.addTable(table);
        }
        try {
            contrastDb.createTable();
        } catch (SQLException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.CREATE_CONTRAST_TABLE_ERROR,
                    e.getLocalizedMessage());
        }
    }

    private void initMysqlTbStruct() throws TestCaseException {
        for (Db db : dbNameDbMap.values()) {
            try {
                db.createTable();
            } catch (SQLException e) {
                throw new TestCaseException(TestCaseException.ExceptCase.CREATE_PHYSICAL_TABLE_ERROR,
                        e.getLocalizedMessage());
            }
        }
    }

    private void initMysqlDbStruct() throws TestCaseException {
        // 创建mysql的物理库
        for (List<String> record : instDbRels) {
            Db db = new Db(record.get(0),
                    record.get(1),
                    record.get(2), record.get(3));
            try {
                db.dropDb(); // 清空老的数据库
            } catch (SQLException e) {
                throw new TestCaseException(TestCaseException.ExceptCase.DROP_DB_ERROR,
                        e.getLocalizedMessage() + db);
            }
            try {
                db.createDb();
            } catch (SQLException e) {
                throw new TestCaseException(TestCaseException.ExceptCase.CREATE_PHYSICAL_DB_ERROR,
                        e.getLocalizedMessage() + db);
            }
        }
    }

    private void initContrastDbStruct(Db contrastDb) throws TestCaseException {
        try {
            contrastDb.dropDb();
        } catch (SQLException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.DROP_DB_ERROR,
                    e.getLocalizedMessage() + contrastDb);
        }
        try {
            contrastDb.createDb();
        } catch (SQLException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.CREATE_CONTRAST_DB_ERROR,
                    e.getLocalizedMessage() + contrastDb);
        }
    }


    public Map<String, AbstractTable> getTbNameTableMap() {
        return tbNameTableMap;
    }

    public Map<String, Map<String, Db>> getNoRuleSpTbColValDbMap() {
        return noRuleSpTbColValDbMap;
    }

    public Map<String, Map<String, Db>> getRuleSpTbColValDbMap() {
        return ruleSpTbColValDbMap;
    }

    public Map<String, Db> getNoshardTbDbMap() {
        return noshardTbDbMap;
    }

    public void clean() throws TestCaseException {
        for (Map.Entry<String, Db> entry : dbNameDbMap.entrySet()) {
            try {
                entry.getValue().dropDb();
            } catch (SQLException e) {
                throw new TestCaseException(TestCaseException.ExceptCase.DROP_DB_ERROR,
                        e.getLocalizedMessage());
            }
        }
        // 清空map
        this.instDbRels.clear();

        this.dbNameDbMap.clear();

        this.noShardTbNameDbRels.clear();

        this.shardTbNameDbRels.clear();

        this.noShardTbNameDbRels.clear();

        this.spTbNameDbRels.clear();

        this.shardTbColRels.clear();

        this.noshardTbColRels.clear();

        this.ruleSpTbColRels.clear();
    }
}
