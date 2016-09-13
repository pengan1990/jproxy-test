import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pengan on 16-9-9.
 */
public class Db {
    boolean shareConn = false; // 是否打开事务, 共享连接
    String user;
    String password;
    String url;
    String name;
    Connection txConn;
    List<AbstractTable> tables;

    public Db(String url, String user, String password, String name) {
        this.user = user;
        this.password = password;
        this.url = "jdbc:mysql://" + url;
        this.name = name;
        this.tables = new LinkedList<AbstractTable>();
    }

    public boolean addTable(AbstractTable table) {
        return this.tables.add(table);
    }

    private void close(ResultSet rst, Statement stmt, Connection conn) {
        if (rst != null) try {
            rst.close();
        } catch (SQLException e) {
        }
        if (stmt != null) try {
            stmt.close();
        } catch (SQLException e) {
        }
        if (conn != null) try {
            conn.close();
        } catch (SQLException e) {
        }
    }

    // rule: keyFields[0] is key, others is value List<String>
    public List<List<String>> executeQuery(String sql, String... keyFields) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rst = null;
        try {
            if (!shareConn) {
                // if not open transaction or txConn is null then just get a new one is ok
                conn = DriverManager.getConnection(this.url, this.user, this.password);
            } else if (txConn == null) {
                txConn = DriverManager.getConnection(this.url, this.user, this.password);
                conn = txConn;
            } else {
                conn = txConn;
            }
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            List<List<String>> insts = new LinkedList<List<String>>();
            while (rst.next()) {
                List<String> values = new LinkedList<String>();

                for (String field : keyFields) {
                    values.add(rst.getString(field));
                }
                insts.add(values);
            }
            return insts;
        } finally {
            if (!shareConn) {
                close(rst, stmt, conn);
            } else {
                close(rst, stmt, null);
            }
        }
    }

    public boolean executeUpdate(String sql) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            if (!shareConn) {
                // if not open transaction or txConn is null then just get a new one is ok
                conn = DriverManager.getConnection(this.url, this.user, this.password);
            } else if (txConn == null) {
                txConn = DriverManager.getConnection(this.url, this.user, this.password);
                conn = txConn;
            } else {
                conn = txConn;
            }
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } finally {
            if (!shareConn) {
                close(null, stmt, conn);
            } else {
                close(null, stmt, null);
            }
        }
    }

    /**
     * create db
     *
     * @return
     */
    public boolean createDb() throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + this.name;
        try {
            return executeUpdate(sql);
        } finally {
            this.url = this.url + "/" + this.name;
        }

    }

    public boolean dropDb() throws SQLException {
        assert this.name != null;
        String sql = "DROP DATABASE IF EXISTS " + this.name;
        return executeUpdate(sql);
    }

    public void createTable() throws SQLException {
        for (AbstractTable tb : tables) {
            String sql = tb.genCreateTableSql();
            executeUpdate(sql);
        }
    }

    public boolean check() {
        if (this.url == null || this.user == null
                || this.password == null || this.name == null) {
            return false;
        }
        return true;
    }

    public void closeShareConn() {
        // 关闭事务， 停止连接共享
        shareConn = false;
        close(null, null, txConn);
        txConn = null;
    }

    public void shareConn() {
        // 开启事务，共享连接，保证事务一致行
        shareConn = true;
    }

    @Override
    public String toString() {
        return "Db{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
