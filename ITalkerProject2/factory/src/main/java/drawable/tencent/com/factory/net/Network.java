package drawable.tencent.com.factory.net;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

import java.io.IOException;

import drawable.tencent.com.factory.Factory;
import italker.tencent.com.common.commont;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求框架的封装"
 */
public class Network {

    // 构建一个Retrofit
    public static Retrofit getRetrofit(){
        // 得到一个OK Client

        OkHttpClient client = new OkHttpClient.Builder()
                .build();


        Retrofit.Builder builder = new Retrofit.Builder();

        // 设置电脑链接
        return builder.baseUrl(commont.Constance.API_URL)
                // 设置client
                .client(client)
                // 设置Json解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

    }

}
