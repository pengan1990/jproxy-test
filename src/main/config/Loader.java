import java.io.*;
import java.util.Properties;

/**
 * Created by pengan on 16-8-31.
 */
public class Loader {

    String reportFilePath;
    ContrastDb contrastDb;
    JProxyDb jProxyDb;
    MetaDb metaDb;
    String config;

    public Loader(String config) {
        this.config = config;
    }

    public void load() throws TestCaseException, FileNotFoundException {

        InputStream input = new FileInputStream(this.config);
        Properties props = new Properties();
        try {
            System.err.println(input);
            props.load(input);
        } catch (IOException e) {
            throw new TestCaseException(TestCaseException.ExceptCase.PARSE_PROPERTIES_ERROR,
                    e.getLocalizedMessage());
        }

        this.metaDb = new MetaDb(props.getProperty(Constants.META_HOST) + ":" +
                props.getProperty(Constants.META_PORT) + "/" + props.getProperty(Constants.META_DB),
                props.getProperty(Constants.META_USER), props.getProperty(Constants.META_PASSWORD),
                props.getProperty(Constants.META_DB));

        this.jProxyDb = new JProxyDb(props.getProperty(Constants.JPROXY_HOST) + ":" +
                props.getProperty(Constants.JPROXY_PORT) + "/" + props.getProperty(Constants.JPROXY_DB),
                props.getProperty(Constants.JPROXY_USER), props.getProperty(Constants.JPROXY_PASSWORD),
                props.getProperty(Constants.JPROXY_DB));

        this.contrastDb = new ContrastDb(props.getProperty(Constants.CONTRAST_HOST) + ":" +
                props.getProperty(Constants.CONTRAST_PORT),
                props.getProperty(Constants.CONTRAST_USER), props.getProperty(Constants.CONTRAST_PASSWORD),
                props.getProperty(Constants.CONTRAST_DB));

        this.reportFilePath = props.getProperty(Constants.REPORT_FILE_PATH);

        check(this.metaDb, this.jProxyDb, this.contrastDb);
    }

    private void check(Db... dbs) throws TestCaseException {
        for (Db db : dbs) {
            if (!db.check()) {
                throw new TestCaseException(TestCaseException.ExceptCase.LOAD_CONFIG_ERROR, db.toString());
            }
        }
        if (this.reportFilePath == null || this.reportFilePath.equals("")) {
            throw new TestCaseException(TestCaseException.ExceptCase.LOAD_CONFIG_ERROR, reportFilePath + " is null");
        }
    }

    public ContrastDb getContrastDb() {
        return contrastDb;
    }

    public JProxyDb getjProxyDb() {
        return jProxyDb;
    }

    public MetaDb getMetaDb() {
        return metaDb;
    }

    public String getReportFilePath() {
        return this.reportFilePath;
    }
}
