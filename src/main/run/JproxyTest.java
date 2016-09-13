import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pengan on 16-9-1.
 */
public class JproxyTest {
    private final static Logger logger = Logger.getLogger(JproxyTest.class);
    /**
     * one, load config to init
     * <p/>
     * jproxy db, meta db, compare db
     * <p/>
     * <p/>
     * two, init db table struct on compare db and back mysql instance
     * <p/>
     * <p/>
     * three, using status machine to run over all status in Operation.OPERATION
     * <p/>
     * <p/>
     * four, for normal shard table
     * <p/>
     * for each status in machine then compare the results, one from jproxy db
     * one from compare db
     * <p/>
     * five, for no shard table and special route table
     * <p/>
     * we have to be cautious about the result is more than comparing the result from
     * jproxy db and compare db
     * <p/>
     * Be sure that the data is load on the special data node
     */

    Loader loader;
    InitStruct initStruct;
    InitData initData;
    String config;

    FileWriter writer; // write file into html open in brower

    public JproxyTest() throws TestCaseException {
    }

    public JproxyTest(String config) throws TestCaseException {
        this.config = config;
    }

    private void cleanup() throws TestCaseException {
        try {
            writer.write("</table>\n</body>\n"); // end html body table
            writer.close();
        } catch (IOException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.CLOSE_REPORT_FILE_ERROR,
                    e.getLocalizedMessage());
        }

        // 清空每个后台实例的库表
        initStruct.clean();

        // 清空对照库的库表结构
        try {
            loader.getContrastDb().dropDb();
        } catch (SQLException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.DROP_DB_ERROR,
                    e.getLocalizedMessage());
        }
    }

    private void run() throws TestCaseException {
        // 所有的表
        for (Map.Entry<String, AbstractTable> entry : initStruct.getTbNameTableMap().entrySet()) {
            String tbName = entry.getKey();
            AbstractTable table = entry.getValue();
            // key : table name   Table table
            for (SqlPair pair : table.getAllPairs()) {
                StringBuilder rst = new StringBuilder("<tr>");

                StringBuilder sqlRecords = new StringBuilder("<table>"); // record execute sqls in tx for many

                rst.append("<td>").append(pair.getCallMethod()).append("</td>\n");
                rst.append("<td>").append(table.getClass().getName()).append("</td>\n");
                StringBuilder except = new StringBuilder();

                try {
                    TableHandler.handle(loader.getjProxyDb(), loader.getContrastDb(),
                            table,
                            pair, rst, sqlRecords);
                } catch (TestCaseException exp) {
                    except.append(exp.getLocalizedMessage());
//                    throw exp;
                } finally {
                    sqlRecords.append("</table>");
                    try {
                        rst.append("<td>").append(sqlRecords).append("</td>\n");
                        rst.append("<td>").append(except).append("</td>\n");
                        rst.append("</tr>\n");
                        writer.write(rst.toString());
                        // 每次都将结果写入到报告当中
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void prepare() throws TestCaseException, FileNotFoundException {
        loader = new Loader(this.config);
        loader.load();

        initStruct = new InitStruct(loader.getMetaDb());
        initData = new InitData();

        initStruct.initStruct(loader.getContrastDb());
        initData.initData(initStruct, loader.getjProxyDb(), loader.getContrastDb());

        try {
            writer = new FileWriter(new File(loader.getReportFilePath()), false);
            writer.write("<html>\n" +
                    "<body>\n" +
                    "<h3 style=\"text-align:center\">JProxy Test Report </h3>\n");
            writer.write("<table border=\"1\">");
            writer.write("<th>" + Constants.OPERATION_TYPE + "</th>\n");
            writer.write("<th>" + Constants.TABLE_TYPE + "</th>\n");
            writer.write("<th>" + Constants.JPROXY_DB_RESULT + "</th>\n");
            writer.write("<th>" + Constants.CONTRAST_DB_RESULT + "</th>\n");
            writer.write("<th>" + Constants.COMPARE_RESULT + "</th>\n");
            writer.write("<th>" + Constants.SQL + "</th>\n");
            writer.write("<th>" + Constants.EXCEPTION + "</th>\n");
        } catch (IOException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.OPEN_REPORT_FILE_ERROR,
                    e.getLocalizedMessage());
        }
    }

    public void startTest() throws TestCaseException, FileNotFoundException {
        logger.info("test prepare");
        prepare();
        logger.info("test run");
        run();
        logger.info("test cleanup");
        cleanup();
    }

}
