package agilor.distributed.relational.data.db;

/**
 * Created by LQ on 2015/12/23.
 */
public class DB {
    public static class User extends ObjectModel<User> {
        private final static User _instance = new User();
        public static User instance() {
            return _instance;
        }
    }
    public static class DeviceType extends ObjectModel<DeviceType> {
        private final static DeviceType _instance = new DeviceType();

        public static DeviceType instance() {
            return _instance;
        }
    }
    public static class Device extends ObjectModel<Device> {
        private final static Device _instance = new Device();

        public static Device instance() {
            return _instance;
        }
    }
    public static class Sensor  extends ObjectModel<Sensor> {
        private final static Sensor _instance = new Sensor();

        public static Sensor instance() {
            return _instance;
        }
    }
}
