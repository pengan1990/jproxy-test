import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by pengan on 16-9-11.
 *
 * table handler
 */
public class TableHandler {
    private static final Logger logger = Logger.getLogger(TableHandler.class);

    public static void handle(Db jproxyDb,
                              Db contrastDb,
                              AbstractTable table,
                              SqlPair pair,
                              StringBuilder rst, StringBuilder sqlRecords) throws TestCaseException {
        jproxyDb.shareConn(); // 开启事务
        contrastDb.shareConn();

        // start to execute transaction
        StringBuilder sqlExeRst = new StringBuilder();
        for (String sql : pair.getExeSql()) {
            sqlRecords.append("<tr><td>").append(sql).append("</td></tr>");
            sqlExeRst.setLength(0); // 重置sql execute result
            try {
                execute(jproxyDb, contrastDb,
                        sql, sqlExeRst, pair.isRstPredict()); // 和之前的兼容
            } catch (TestCaseException exp) {
                rst.append(sqlExeRst); // 如果一次不成功，则返回当前失败的结果
                rst.append("<td>\n").append(Constants.NULL).append("</td>\n"); // 填充空行
                throw exp;
            }
        }
        // 这里事务已经执行完成
        rst.append(sqlExeRst); // 事务已经成功执行

        jproxyDb.closeShareConn();
        contrastDb.closeShareConn();
        // sql already execute over

        // select 验证sql执行情况
        table.validate(jproxyDb, contrastDb, pair, rst);
    }

    protected static void execute(Db jproxyDb, Db contrastDb, String sql,
                                  StringBuilder rst, boolean resultPredict) throws TestCaseException {
        if (sql == null || sql.charAt(0) == 'S') { //
            logger.warn("sql include select just in validate method");
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // JPROXY db 没有执行
            rst.append("<td>").append(Constants.NULL).append("</td>\n"); // contrast db 没有执行
            return;
        }
        logger.info(sql);
        // for most case size == two : the first sql is the original the next is for validation
        if (!resultPredict) { // 预测sql执行失败，需要catch异常
            try {
                jproxyDb.executeUpdate(sql);
                rst.append("<td>").append(Constants.SUCCESS).append("</td>\n");
                rst.append("<td>").append(Constants.NULL).append("</td>\n");
                throw new TestCaseException(TestCaseException.ExceptCase.JPROXY_DB_EXECUTE_ERROR,
                        " should't execute OK");
            } catch (SQLException exp) {
                rst.append("<td>").append(Constants.FAILURE).append("</td>\n");
                rst.append("<td>").append(Constants.NULL).append("</td>\n"); // 这里contrast db 不执行
            }
        } else {
            try {
                jproxyDb.executeUpdate(sql);
                rst.append("<td>").append(Constants.SUCCESS).append("</td>\n");
            } catch (SQLException exp) { // 如果数据库连接正常 则必须执行正常
                rst.append("<td>").append(Constants.FAILURE).append("</td>\n");
                rst.append("<td>").append(Constants.NULL).append("</td>\n");
                throw new TestCaseException(TestCaseException.ExceptCase.JPROXY_DB_EXECUTE_ERROR,
                        exp.getLocalizedMessage());
            }
            try {
                contrastDb.executeUpdate(sql);
                rst.append("<td>").append(Constants.SUCCESS).append("</td>\n");
            } catch (SQLException exp) {
                rst.append("<td>").append(Constants.FAILURE).append("</td>\n");
                throw new TestCaseException(TestCaseException.ExceptCase.CONTRAST_DB_EXECUTE_ERROR,
                        exp.getLocalizedMessage());
            }
        }
    }

}
