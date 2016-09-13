import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by pengan on 16-9-8.
 */
public abstract class AbstractCol implements ColAction {
    protected static Random RAND;
    protected static SimpleDateFormat SDF;
    protected static char[] CHAR;
    protected static Long MAX_TIME;

    static {
        RAND = new Random();
        SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int len = 64;
        CHAR = new char[len];
        for (int idx = 66; idx < 91; idx++) {
            CHAR[idx - 66] = (char) idx;
        }
        for (int idx = 48; idx < 58; idx++) {
            CHAR[idx - 48 + 25] = (char) idx;
        }
        for (int idx = 35; idx < 39; idx++) {
            CHAR[idx - 35 + 35] = (char) idx;
        }
        for (int idx = 97; idx < 122; idx++) {
            CHAR[idx - 97 + 39] = (char) idx;
        }
        String maxDate = "2038-01-01 00:00:01";
        try {
            MAX_TIME = SDF.parse(maxDate).getTime();
        } catch (ParseException e) {
            MAX_TIME = System.currentTimeMillis();
        }
    }

    protected boolean isKeyCol; // 是否是关键列
    protected String colName;
    protected List<String> vals;

    public AbstractCol(boolean isKeyCol, String colName) {
        this.isKeyCol = isKeyCol;
        this.colName = colName;
    }

    public AbstractCol(boolean isKeyCol, String colName, List<String> vals) {
        this.isKeyCol = isKeyCol;
        this.colName = colName;
        this.vals = vals;
    }

    @Override
    public String genCreatePart() {
        return this.colName + getColSuffix();
    }

    public abstract String getColSuffix();

    @Override
    public String getRandValue(boolean isSpecial) throws TestCaseException {
        if (isSpecial && isKeyCol) {
            if (vals == null || vals.size() == 0) {
                // 列类型与调用方法出现异常
                throw new TestCaseException(TestCaseException.ExceptCase.TB_TYPE_INCONSISTENT_WITH_OP_TYPE,
                        "table type inconsistent with operation type :" + this);
            }
            int idx = (RAND.nextInt() & (vals.size() - 1));
            return vals.get(idx);
        }
        return getRandValue();
    }

    public abstract String getRandValue();

    /**
     * 添加特殊列值的
     *
     * @param val
     * @return
     */
    public boolean addColVal(String val) {
        return this.vals.add(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        AbstractCol another = (AbstractCol) obj;
        return this.colName.equals(another.colName) &&
                this.isKeyCol == another.isKeyCol &&
                this.vals == another.vals;
    }

    @Override
    public String toString() {
        return "AbstractCol{" +
                "colName='" + colName + '\'' +
                ", isKeyCol=" + isKeyCol +
                '}';
    }
}
