package drawable.tencent.com.factory;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import italker.tencent.com.common.app.MyApplication;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class Factory {

    private static final Factory instance = new Factory();
    private final Executor executor ;

    public static MyApplication getApp(){
        return MyApplication.getInstance();
    }

    public Factory(){
        executor = Executors.newFixedThreadPool(4);
    }
    /**
     * 异步运行的方法
     */
    public static void RunAsync(Runnable run){
        instance.executor.execute(run);
    }
}
