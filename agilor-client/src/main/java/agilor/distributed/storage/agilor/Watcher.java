package agilor.distributed.storage.agilor;

/**
 * Created by LQ on 2015/7/30.
 * 订阅处理函数接口
 */
public interface Watcher {
    void handler(Target target);
}
