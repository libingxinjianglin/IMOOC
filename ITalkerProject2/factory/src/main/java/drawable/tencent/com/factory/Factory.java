package drawable.tencent.com.factory;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import italker.tencent.com.common.app.MyApplication;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class Factory {

    private static final Factory instance = new Factory();
    private final Executor executor ;
    private final Gson gson;


    public static MyApplication getApp(){
        return MyApplication.getInstance();
    }

    public Factory(){
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // TODO 设置一个过滤器，数据库级别的Model不进行Json转换
                //.setExclusionStrategies()
                .create();
    }
    /**
     * 异步运行的方法
     */
    public static void RunAsync(Runnable run){
        instance.executor.execute(run);
    }

    public static Gson getGson(){
        return instance.gson;
    }
}
