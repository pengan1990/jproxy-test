/**
 * Created by pengan on 16-9-8.
 */
public interface ColAction {
    String genCreatePart();
    String getRandValue(boolean isSpecial) throws TestCaseException;
}
