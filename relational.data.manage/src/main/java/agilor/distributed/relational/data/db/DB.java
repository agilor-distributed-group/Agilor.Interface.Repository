package agilor.distributed.relational.data.db;

/**
 * Created by LQ on 2015/12/23.
 */
public class DB {
    public static class User extends ExtendModel<User> {
        private final static User _instance = new User();
        public static User instance() {
            return _instance;
        }
    }
    public static class DeviceType extends ExtendModel<DeviceType> {
        private final static DeviceType _instance = new DeviceType();

        public static DeviceType instance() {
            return _instance;
        }
    }
    public static class Device extends ExtendModel<Device> {
        private final static Device _instance = new Device();

        public static Device instance() {
            return _instance;
        }
    }
    public static class Sensor  extends ExtendModel<Sensor> {
        private final static Sensor _instance = new Sensor();

        public static Sensor instance() {
            return _instance;
        }
    }

    public static class SensorOfType extends ExtendModel<SensorOfType>
    {
        private final static SensorOfType _instance = new SensorOfType();

        public static SensorOfType instance() {
            return _instance;
        }
    }
}
