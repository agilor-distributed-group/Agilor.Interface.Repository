package agilor.distributed.relational.data.context;

import agilor.distributed.relational.data.config.CONFIG;
import agilor.distributed.relational.data.config.GLOBAL_CONFIG;
import agilor.distributed.relational.data.config.JDBC_CONFIG;
import agilor.distributed.relational.data.config.ZOOKEEPER_CONFIG;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by LQ on 2015/12/24.
 */
public class Config {

    private final static Logger logger = LoggerFactory.getLogger(Config.class);

    private final static String ZOOKEEPER_CONFIG_FILE = "zookeeper.properties";
    private final static String JDBC_CONFIG_FILE="jdbc.properties";
    private final static String GLOBAL_CONFIG_FILE="global.properties";


    private static boolean isRuning=true;


    public static ZOOKEEPER_CONFIG zookeeper = new ZOOKEEPER_CONFIG();
    public static JDBC_CONFIG jdbc = new JDBC_CONFIG();
    public static GLOBAL_CONFIG global = new GLOBAL_CONFIG();

    private static void parse_config(String file_path,CONFIG config) {
        Properties pps = new Properties();
        try {

            pps.load(Object.class.getResourceAsStream("/"+file_path));
        } catch (IOException e) {
            logger.info("load config file {} error", file_path);
            e.printStackTrace();
        }
        config.parse(pps);
        logger.info("load config file {}", file_path);
    }

    static {


        parse_config(ZOOKEEPER_CONFIG_FILE,zookeeper);
        //parse_config(JDBC_CONFIG_FILE, jdbc);
        //parse_config(GLOBAL_CONFIG_FILE,global);

    }

    public static boolean isRuning() {
        return isRuning;
    }

    public static void setIsRuning(boolean isRuning) {
        Config.isRuning = isRuning;
    }
}
