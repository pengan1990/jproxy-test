
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by pengan on 16-8-31.
 * <p/>
 * insert data into table
 */
public class InitData {
    private static final Logger logger = Logger.getLogger(InitStruct.class);

    public void initData(InitStruct initStruct, Db jproxyDb, Db contrastDb) throws TestCaseException {
        // 初始化数据 insert 1w record for each table
        for (Map.Entry<String, AbstractTable> entry : initStruct.getTbNameTableMap().entrySet()) {
            // key : table name   Table table
            SqlPair pair = entry.getValue().genInsertWithKeySql(1000);
            // 不同的 table 类型， 不同的操作类型， 对照的库是不一样的
            String sql = pair.getExeSql().get(0);
            try {
                jproxyDb.executeUpdate(sql);
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new TestCaseException(TestCaseException.ExceptCase.JPROXY_DB_INIT_DATA_ERROR,
                        e.getLocalizedMessage() + "\t" + sql);
            }
            try {
                contrastDb.executeUpdate(sql);
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new TestCaseException(TestCaseException.ExceptCase.CONTRAST_DB_INIT_DATA_ERROR,
                        e.getLocalizedMessage() + "\t" + sql);
            }
        }
    }
}
