package italker.tencent.com.common.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 * Created by Administrator on 2018/1/22 0022.
 */

public interface DbDataSource<Data> extends DataSource{
    /**
     * 有一个基本的数据源加载方法，传递一个callback回调
     * @param callback
     */
    void load(SucceedCallback<List<Data>> callback);
}
