package agilor.distributed.storage.inter.jlient;

import org.apache.thrift.TException;

/**
 * Created by LQ on 2015/8/12.
 * 迭代器基类
 *
 */
public interface Iterator<T> {

    /**
     * 获取集合中的下一个数据
     * @return
     * @throws TException
     */
    T next() throws TException, NoSuchFieldException, IllegalAccessException;

    /**
     * 判断是否有数据
     * @return
     * @throws TException
     */
    boolean hasNext() throws TException;

    /**
     * 关闭迭代器（必须关闭）
     */
    void close();
}
