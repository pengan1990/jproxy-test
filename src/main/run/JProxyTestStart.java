
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by pengan on 16-9-8.
 */
public class JProxyTestStart {
    private static final Logger logger = Logger.getLogger(JProxyTestStart.class);

    public static void main(String[] args) {
        try {
            String config = System.getProperty("config") + File.separator + "config.properties";
            String log4j = System.getProperty("config") + File.separator + "log4j.xml";

            log4j = "/home/admin/idea-projects/test/jproxy-test/conf/log4j.xml";
            config = "/home/admin/idea-projects/test/jproxy-test/conf/config.properties";
            Log4jInitializer.configureAndWatch(log4j, 1000);
            JproxyTest test = new JproxyTest(config);
            test.startTest();
        } catch (TestCaseException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
    }
}
