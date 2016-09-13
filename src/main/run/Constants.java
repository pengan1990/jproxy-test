/**
 * Created by pengan on 16-8-31.
 */
public interface Constants {
    String URL = "url";
    String USER = "user";
    String PASSWORD = "password";
    String SCHEMA = "schema";
    String TABLE_NAME = "table_name";
    String COLUMN_NAME = "column_name";
    String COLUMN_TYPE = "type";
    String COLUMN_VALUE = "column_value";

    String TABLE_HEAD = "CREATE TABLE IF NOT EXISTS ";
    String TABLE_TAIL = "ENGINE=InnoDB DEFAULT CHARSET=utf8"; // table name keyFields index

    String QUERY_NO_SHARD_TABLE_DB_RELATION = "SELECT lower(CONCAT(mi.ip, ':', mi.port, '/', di.datanode_name)) url,\n" +
            "       lower(mi.username) AS user,\n" +
            "       lower(mi.passwd) AS password,\n" +
            "       lower(di.datanode_name) AS `schema`,\n" +
            "       lower(ti.table_name) AS table_name\n" +
            "FROM mysql_instance mi,\n" +
            "     datanode_info di,\n" +
            "     table_datanode_relation tdr,\n" +
            "     table_info ti\n" +
            "WHERE mi.id = di.instance_id\n" +
            "  AND di.id = tdr.datanode_id\n" +
            "  AND ti.id = tdr.table_id\n" +
            "  AND ti.id IN\n" +
            "    (SELECT ti.id AS id\n" +
            "     FROM table_info ti\n" +
            "     LEFT JOIN table_rule_relation trr ON trr.table_id = ti.id\n" +
            "     LEFT JOIN special_route sr ON sr.table_id = ti.id\n" +
            "     WHERE trr.table_id IS NULL\n" +
            "       AND sr.table_id IS NULL)\n" +
            "ORDER BY tdr.datanode_idx";
    String QUERY_SHARD_TABLE_DB_RELATION = "SELECT lower(Concat(mi.ip, ':', mi.port, '/', di.datanode_name)) url,\n" +
            "       lower(mi.username)                                        user,\n" +
            "       lower(mi.passwd)                                          password,\n" +
            "       lower(di.datanode_name) AS `schema`,\n" +
            "       lower(ti.table_name)                                      table_name\n" +
            "FROM   mysql_instance mi,\n" +
            "       datanode_info di,\n" +
            "       table_datanode_relation tdr,\n" +
            "       table_info ti,\n" +
            "       table_rule_relation trr\n" +
            "WHERE  mi.id = di.instance_id\n" +
            "       AND di.id = tdr.datanode_id\n" +
            "       AND ti.id = tdr.table_id\n" +
            "       AND ti.id = trr.table_id\n" +
            "ORDER  BY tdr.datanode_idx";

    // query special table db relation
    String QUERY_SP_TABLE_DB_RELATION = "SELECT lower(Concat(mi.ip, ':', mi.port, '/', di.datanode_name)) url,\n" +
            "       lower(mi.username) user, lower(mi.passwd) password,\n" +
            "                                lower(di.datanode_name) AS `schema`,\n" +
            "                                lower(ti.table_name) table_name\n" +
            "FROM mysql_instance mi,\n" +
            "     datanode_info di,\n" +
            "     table_datanode_relation tdr,\n" +
            "     table_info ti\n" +
            "WHERE mi.id = di.instance_id\n" +
            "  AND di.id = tdr.datanode_id\n" +
            "  AND ti.id = tdr.table_id\n" +
            "  AND ti.id IN\n" +
            "    (SELECT table_id\n" +
            "     FROM special_route\n" +
            "     GROUP BY table_id)\n" +
            "UNION ALL\n" +
            "SELECT lower(Concat(mi.ip, ':', mi.port, '/', di.datanode_name)) url,\n" +
            "       lower(mi.username) user, lower(mi.passwd) password,\n" +
            "                                lower(di.datanode_name) AS `schema`,\n" +
            "                                lower(ti.TABLE_NAME) table_name\n" +
            "FROM mysql_instance mi,\n" +
            "     datanode_info di,\n" +
            "     table_info ti,\n" +
            "     special_route sr\n" +
            "WHERE mi.id = di.instance_id\n" +
            "  AND di.id = sr.datanode_id\n" +
            "  AND ti.id = sr.table_id";

    String QUERY_SCHEMA_SQL = "SELECT lower(Concat(mi.ip, ':', mi.port)) url,\n" +
            "       lower(mi.username)                 user,\n" +
            "       lower(mi.passwd)                   password,\n" +
            "       lower(di.datanode_name)            `schema`\n" +
            "FROM   mysql_instance mi,\n" +
            "       datanode_info di\n" +
            "WHERE  mi.id = di.instance_id";


    // only shard table
    String QUERY_SHARD_TABLE_COLUMN = "SELECT lower(ti.table_name) AS table_name, " +
            "lower(ri.columns) AS column_name, fci.class AS type\n" +
            "FROM table_info ti, table_rule_relation trr, rule_info ri, rule_func_info rfi, func_class_info fci\n" +
            "WHERE ti.id = trr.table_id\n" +
            "\tAND trr.rule_id = ri.id\n" +
            "\tAND rfi.id = ri.rule_func_id\n" +
            "\tAND rfi.func_class_id = fci.id\n" +
            "\tAND ti.id NOT IN (select table_id from special_route group by table_id)";

    // QUERY RULED SPECIAL TABLE COLUMNS
    String QUERY_RULE_SP_TABLE_COLUMNS = "SELECT lower(ti.table_name) table_name, " +
            "lower(sr.column_name) column_name, sr.column_value column_value\n" +
            "FROM table_info ti,\n" +
            "     special_route sr\n" +
            "WHERE ti.id = sr.table_id\n" +
            "  AND ti.id IN\n" +
            "    (SELECT table_id\n" +
            "     FROM table_rule_relation)";

    // special route table with no rule columns
    String QUERY_NO_RULE_SP_TABLE_COLUMNS = "SELECT lower(ti.table_name) table_name, " +
            "lower(sr.column_name) column_name, sr.column_value column_value\n" +
            "FROM table_info ti,\n" +
            "     special_route sr\n" +
            "WHERE ti.id = sr.table_id\n" +
            "  AND ti.id NOT IN\n" +
            "    (SELECT table_id\n" +
            "     FROM table_rule_relation)";

    String QUERY_NO_SHARD_TABLE_COLUMN = "SELECT lower(ti.table_name) AS table_name\n" +
            "FROM mysql_instance mi,\n" +
            "     datanode_info di,\n" +
            "     table_datanode_relation tdr,\n" +
            "     table_info ti\n" +
            "WHERE ti.id = tdr.table_id\n" +
            "  AND ti.id IN\n" +
            "    (SELECT ti.id AS id\n" +
            "     FROM table_info ti\n" +
            "     LEFT JOIN table_rule_relation trr ON trr.table_id = ti.id\n" +
            "     LEFT JOIN special_route sr ON sr.table_id = ti.id\n" +
            "     WHERE trr.table_id IS NULL\n" +
            "       AND sr.table_id IS NULL)\n" +
            "      GROUP BY table_name";

    String QUERY_NO_RULE_SP_TABLE_COLVALUE_DB_RELATIONS = "SELECT lower(concat(mi.ip, ':', mi.port, '/', di.datanode_name)) url,\n" +
            "       lower(mi.username) user, lower(mi.passwd) password,\n" +
            "                         lower(ti.table_name) table_name, " +
            "lower(sr.column_name) column_name, sr.column_value column_value\n" +
            "FROM mysql_instance mi,\n" +
            "     datanode_info di,\n" +
            "     table_info ti,\n" +
            "     special_route sr\n" +
            "WHERE di.instance_id = mi.id\n" +
            "  AND di.id = sr.datanode_id\n" +
            "  AND sr.table_id = ti.id\n" +
            "  AND ti.id IN\n" +
            "    (SELECT ti.id\n" +
            "     FROM table_info ti\n" +
            "     LEFT JOIN table_rule_relation trr ON ti.id = trr.table_id\n" +
            "     WHERE trr.table_id IS NULL)";

    String QUERY_RULE_SP_TABLE_COLVALUE_DB_RELATIONS = "SELECT lower(concat(mi.ip, ':', mi.port, '/', di.datanode_name)) url,\n" +
            "       lower(mi.username) user, lower(mi.passwd) password,\n" +
            "                         lower(ti.table_name) table_name, " +
            "lower(sr.column_name) column_name, sr.column_value column_value\n" +
            "FROM mysql_instance mi,\n" +
            "     datanode_info di,\n" +
            "     table_info ti,\n" +
            "     special_route sr\n" +
            "WHERE di.instance_id = mi.id\n" +
            "  AND di.id = sr.datanode_id\n" +
            "  AND sr.table_id = ti.id\n" +
            "  AND ti.id IN\n" +
            "    (SELECT ti.id\n" +
            "     FROM table_info ti\n" +
            "     LEFT JOIN table_rule_relation trr ON ti.id = trr.table_id\n" +
            "     WHERE trr.table_id IS NOT NULL)";


    String META_HOST = "meta.host";
    String META_PORT = "meta.port";
    String META_USER = "meta.user";
    String META_PASSWORD = "meta.password";
    String META_DB = "meta.db";


    String JPROXY_HOST = "jproxy.host";
    String JPROXY_PORT = "jproxy.port";
    String JPROXY_USER = "jproxy.user";
    String JPROXY_PASSWORD = "jproxy.password";
    String JPROXY_DB = "jproxy.db";


    String CONTRAST_HOST = "contrast.host";
    String CONTRAST_PORT = "contrast.port";
    String CONTRAST_USER = "contrast.user";
    String CONTRAST_PASSWORD = "contrast.password";
    String CONTRAST_DB = "contrast.db";

    String REPORT_FILE_PATH = "report.file.path";

    String SUCCESS = "SUCCESS"; // 执行成功
    String FAILURE = "FAILURE"; // 执行失败
    String NULL = "NULL";       // 没有执行

    String TABLE_TYPE = "TABLE_TYPE";
    String OPERATION_TYPE = "OPERATION_TYPE";
    String JPROXY_DB_RESULT = "JPROXY_RESULT";
    String CONTRAST_DB_RESULT = "CONTRAST_DB_RESULT";
    String COMPARE_RESULT = "COMPARE_RESULT";
    String SQL = "SQL";
    String EXCEPTION = "EXCEPTION";

}