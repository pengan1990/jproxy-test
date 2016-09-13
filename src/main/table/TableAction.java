
/**
 * Created by pengan on 16-9-8.
 *
 * table action for each table
 */
public interface TableAction {
//    String genCreateTableSql();

    SqlPair genInsertWithKeySql(int recordNum) throws TestCaseException; //
    SqlPair genInsertWithoutKeySql(int recordNum) throws TestCaseException;

    SqlPair genDeleteWithKeySql(int colValNum) throws TestCaseException;
    SqlPair genDeleteWithoutKeySql(int colValNum) throws TestCaseException;

    SqlPair genUpdateKeyInSetSql(int colValNum) throws TestCaseException;
    SqlPair genUpdateKeyInWhereSql(int colValNum) throws TestCaseException;
    SqlPair genUpdateWithoutKeySql(int colValNum) throws TestCaseException;

    SqlPair genSelectWithKeySql(int colValNum) throws TestCaseException;
    SqlPair genSelectWithoutKeySql(int colValNum) throws TestCaseException;

    SqlPair genSelectMinSql(int colValNum) throws TestCaseException;
    SqlPair genSelectMaxSql(int colValNum) throws TestCaseException;
    SqlPair genSelectCountSql(int colValNum) throws TestCaseException;
    SqlPair genSelectGroupBySql(int colValNum) throws TestCaseException;
    SqlPair genSelectOrderBySql(int colValNum) throws TestCaseException;
    SqlPair genSelectLimitSql(int colValNum) throws TestCaseException;

    /**
     * 事务的规则是保证事务在一个节点
     * @param colValNum: 列值的个数，随机生成
     * @return
     * @throws TestCaseException
     */
    SqlPair genCommitSql(int colValNum) throws TestCaseException;
    SqlPair genRollbacSql(int colValNum) throws TestCaseException;
}
