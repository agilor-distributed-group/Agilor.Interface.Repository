package agilor.distributed.relational.data.context;

import agilor.distributed.relational.data.config.CONFIG;
import agilor.distributed.relational.data.config.GLOBAL_CONFIG;
import agilor.distributed.relational.data.config.JDBC_CONFIG;
import agilor.distributed.relational.data.config.ZOOKEEPER_CONFIG;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by LQ on 2015/12/24.
 */
public class Config {
    private final static String ZOOKEEPER_CONFIG_FILE = "zookeeper.properties";
    private final static String JDBC_CONFIG_FILE="jdbc.properties";
    private final static String GLOBAL_CONFIG_FILE="global.properties";


    private static boolean isRuning=true;


    public static ZOOKEEPER_CONFIG zookeeper = new ZOOKEEPER_CONFIG();
    public static JDBC_CONFIG jdbc = new JDBC_CONFIG();
    public static GLOBAL_CONFIG global = new GLOBAL_CONFIG();

    private static void parse_config(String file_path,CONFIG config) throws IOException {
        Properties pps = new Properties();
        pps.load(new FileInputStream(ZOOKEEPER_CONFIG_FILE));
        config.parse(pps);

    }

    static {

        try {
            parse_config(ZOOKEEPER_CONFIG_FILE,zookeeper);
            parse_config(JDBC_CONFIG_FILE,jdbc);
            parse_config(GLOBAL_CONFIG_FILE,global);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRuning() {
        return isRuning;
    }

    public static void setIsRuning(boolean isRuning) {
        Config.isRuning = isRuning;
    }
}
