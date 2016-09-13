/**
 * Created by pengan on 16-9-3.
 * <p/>
 * 异常情况分为以下几种：
 * 1， 正常预判下， 返回结果异常， 分为三种
 * <p/>
 * 一， JProxy db 返回异常
 * 二， Contrast db 返回异常
 * 三， 两个对比库返回结果不一致
 * <p/>
 * 2, 异常预判下， 返回结果异常， 分为一种
 * 一， JProxy db 返回正常
 * <p/>
 * 3, 加载 配置文件异常
 * <p/>
 * 4, 初始化数据库异常 [分为三种， meta db， jproxy db， compare db]
 * <p/>
 * 5, 配置文件解析异常
 */
public class TestCaseException extends Exception {
    public enum ExceptCase {
        LOAD_CONFIG_ERROR,
        CREATE_PHYSICAL_DB_ERROR,
        CREATE_CONTRAST_DB_ERROR,
        CREATE_PHYSICAL_TABLE_ERROR,
        CREATE_CONTRAST_TABLE_ERROR,
        JPROXY_DB_INIT_DATA_ERROR,
        CONTRAST_DB_INIT_DATA_ERROR,
        PARSE_PROPERTIES_ERROR,
        LOAD_META_INFO_ERROR,
        OPEN_REPORT_FILE_ERROR,
        CLOSE_REPORT_FILE_ERROR,
        REPORT_FILE_WRITE_ERROR,
        DROP_DB_ERROR,
        INCONSISTENT_QUERY_RESULT, //
        TB_TYPE_INCONSISTENT_WITH_OP_TYPE,
        JPROXY_DB_EXECUTE_ERROR,    //
        CONTRAST_DB_EXECUTE_ERROR,  //
    }

    public TestCaseException(ExceptCase exceptCase, String msg) {
        super(exceptCase.toString() + ":" + msg);
    }
}
