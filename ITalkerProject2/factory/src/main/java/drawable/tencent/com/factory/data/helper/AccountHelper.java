package drawable.tencent.com.factory.data.helper;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

import android.net.Network;
import android.text.TextUtils;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.model.api.AccountRepModl;
import drawable.tencent.com.factory.model.api.LoginModel;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.net.RemoteService;
import drawable.tencent.com.factory.persistence.Account;
import drawable.tencent.com.factory.presenter.RegisterControl.RegisenterPresenter;
import italker.tencent.com.common.factory.data.DataSource;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 实现注册逻辑
 */

public class AccountHelper {
    public static void Regist(RegiseModel model, final DataSource.Callback<User> callback){

        /**
         * retrofit
         1、创建Retrofit对象
         2、创建访问API的请求
         3、发送请求
         4、处理结果
         */

        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = drawable.tencent.com.factory.net.Network.getRetrofit().create(RemoteService.class);
        // 得到一个Call
        Call<RspModel<AccountRepModl>> call = service.accountRegister(model);
        // 异步的请求
        call.enqueue(new Callback<RspModel<AccountRepModl>>() {
            public void onResponse(Call<RspModel<AccountRepModl>> call,
                                   Response<RspModel<AccountRepModl>> response) {
                // 请求成功返回
                // 从返回中得到我们的全局Model，内部是使用的Gson进行解析
                RspModel<AccountRepModl> rspModel = response.body();
                if (rspModel.success()) {
                    // 拿到实体
                    AccountRepModl accountRspModel = rspModel.getResult();
                    // 判断绑定状态，是否绑定设备
                    User user = accountRspModel.getUser();
                    DbHelper.save(User.class,user);    //将得到的用户信息进行一个存储到SQLLite数据库中
                    Account.login(accountRspModel);
                    if(accountRspModel.isBind()) {
                        Account.setbindId(true);
                        // 进行的是数据库写入和缓存绑定
                        // 然后返回
                        callback.onDataLoaded(user);
                    }else{
                        // 进行绑定的唤起
                        bindPush(callback);
                    }
                }else {
                    // TODO 对返回的RspModel中的失败的Code进行解析，解析到对应的String资源上面
                    // callback.onDataNotAvailable();
                    // 错误解析
                    Factory.decodeRspCode(rspModel, callback);
                }
            }
            @Override
            public void onFailure(Call<RspModel<AccountRepModl>> call, Throwable t) {
                // 网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    public static void Login(LoginModel model, final DataSource.Callback<User> callback) {

        /**
         * retrofit
         1、创建Retrofit对象
         2、创建访问API的请求
         3、发送请求
         4、处理结果
         */
        // 调用Retrofit对我们的网络请求接口做代理
        Retrofit retrofit = drawable.tencent.com.factory.net.Network.getRetrofit();
        RemoteService service = retrofit.create(RemoteService.class);
        Call<RspModel<AccountRepModl>> rspModelCall = service.accountLogin(model);

        rspModelCall.enqueue(new Callback<RspModel<AccountRepModl>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRepModl>> call, Response<RspModel<AccountRepModl>> response) {
                RspModel<AccountRepModl> body = response.body();

                if(body.success()){
                    AccountRepModl result = body.getResult();
                    User user = result.getUser();
                    DbHelper.save(User.class,user);    //将得到的用户信息进行一个存储到SQLLite数据库中
                    Account.login(result);
                    if(result.isBind()) {
                        Account.setbindId(true);
                        // 进行的是数据库写入和缓存绑定
                        // 然后返回
                        callback.onDataLoaded(user);
                    }else{
                        // 进行绑定的唤起
                        bindPush(callback);
                    }
                }else {
                    // TODO 对返回的RspModel中的失败的Code进行解析，解析到对应的String资源上面
                    // callback.onDataNotAvailable();
                    // 错误解析
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRepModl>> call, Throwable t) {
                // 网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备Id进行绑定的操作
     * @param callback Callback
     */
    public static void bindPush( final DataSource.Callback<User> callback){

        /**
         * retrofit
         1、创建Retrofit对象
         2、创建访问API的请求
         3、发送请求
         4、处理结果
         */
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = drawable.tencent.com.factory.net.Network.getRetrofit().create(RemoteService.class);
        // 得到一个Call
        Call<RspModel<AccountRepModl>> rspModelCall = service.accountBind(Account.getPushId());
        rspModelCall.enqueue(new Callback<RspModel<AccountRepModl>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRepModl>> call, Response<RspModel<AccountRepModl>> response) {
                RspModel<AccountRepModl> body = response.body();
                if(body.success()){
                    AccountRepModl result = body.getResult();
                    User user = result.getUser();
                    DbHelper.save(User.class,user);    //将得到的用户信息进行一个存储到SQLLite数据库中
                    Account.login(result);
                    if(result.isBind()) {       //服务器后台绑定的时候返回true
                        Account.setbindId(true);
                        // 进行的是数据库写入和缓存绑定
                        // 然后返回
                        if(callback != null){
                            callback.onDataLoaded(user);   //广播绑定的时候这个值为空的
                        }

                    }else{
                        // 进行绑定的唤起
                        bindPush(callback);
                    }
                }else {
                    // TODO 对返回的RspModel中的失败的Code进行解析，解析到对应的String资源上面
                    // callback.onDataNotAvailable();
                    // 错误解析
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRepModl>> call, Throwable t) {
                // 网络请求失败
                if(callback != null) {
                    callback.onDataNotAvailable(R.string.data_network_error);
                }
            }
        });
    }

//    /**
//     * 注册的接口，异步的调用
//     *
//     * @param model    传递一个注册的Model进来
//     * @param callback 成功与失败的接口回送
//     */
//    public static void Regist(final RegiseModel model, final DataSource.Callback<User> callback) {
//        // 调用Retrofit对我们的网络请求接口做代理
//        RemoteService service = drawable.tencent.com.factory.net.Network.getRetrofit().create(RemoteService.class);
//        // 得到一个Call
//        Call<RspModel<AccountRepModl>> call = service.accountRegister(model);
//        // 异步的请求
//        call.enqueue(new AccountRspCallback(callback));
//    }
//
//    /**
//     * 登录的调用
//     *
//     * @param model    登录的Model
//     * @param callback 成功与失败的接口回送
//     */
//    public static void Login(final LoginModel model, final DataSource.Callback<User> callback) {
//        // 调用Retrofit对我们的网络请求接口做代理
//        RemoteService service = drawable.tencent.com.factory.net.Network.getRetrofit().create(RemoteService.class);
//        // 得到一个Call
//        Call<RspModel<AccountRepModl>> call = service.accountLogin(model);
//        // 异步的请求
//        call.enqueue(new AccountRspCallback(callback));
//    }
//
//    /**
//     * 对设备Id进行绑定的操作
//     *
//     * @param callback Callback
//     */
//    public static void bindPush(final DataSource.Callback<User> callback) {
//        // 检查是否为空
//        String pushId = Account.getPushId();
//        if (TextUtils.isEmpty(pushId))
//            return;
//
//        // 调用Retrofit对我们的网络请求接口做代理
//        RemoteService service = drawable.tencent.com.factory.net.Network.getRetrofit().create(RemoteService.class);
//        Call<RspModel<AccountRepModl>> call = service.accountBind(pushId);
//        call.enqueue(new AccountRspCallback(callback));
//    }
//
//
//    /**
//     * 请求的回调部分封装
//     */
//    private static class AccountRspCallback implements Callback<RspModel<AccountRepModl>> {
//
//        final DataSource.Callback<User> callback;
//
//        AccountRspCallback(DataSource.Callback<User> callback) {
//            this.callback = callback;
//        }
//
//        @Override
//        public void onResponse(Call<RspModel<AccountRepModl>> call,
//                               Response<RspModel<AccountRepModl>> response) {
//            // 请求成功返回
//            // 从返回中得到我们的全局Model，内部是使用的Gson进行解析
//            RspModel<AccountRepModl> rspModel = response.body();
//            if (rspModel.success()) {
//                // 拿到实体
//                AccountRepModl accountRspModel = rspModel.getResult();
//                // 获取我的信息
//                User user = accountRspModel.getUser();
//                // 第一种，之间保存
//                user.save();
//                    /*
//                    // 第二种通过ModelAdapter
//                    FlowManager.getModelAdapter(User.class)
//                            .save(user);
//
//                    // 第三种，事务中
//                    DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
//                    definition.beginTransactionAsync(new ITransaction() {
//                        @Override
//                        public void execute(DatabaseWrapper databaseWrapper) {
//                            FlowManager.getModelAdapter(User.class)
//                                    .save(user);
//                        }
//                    }).build().execute();
//                    */
//                // 同步到XML持久化中
//                Account.login(accountRspModel);
//
//                // 判断绑定状态，是否绑定设备
//                if (accountRspModel.isBind()) {
//                    // 设置绑定状态为True
//                    Account.setbindId(true);
//                    // 然后返回
//                    if (callback != null)
//                        callback.onDataLoaded(user);
//                } else {
//                    // 进行绑定的唤起
//                    bindPush(callback);
//                }
//            } else {
//                // 错误解析
//                Factory.decodeRspCode(rspModel, callback);
//            }
//        }
//
//        @Override
//        public void onFailure(Call<RspModel<AccountRepModl>> call, Throwable t) {
//            // 网络请求失败
//            if (callback != null)
//                callback.onDataNotAvailable(R.string.data_network_error);
//        }
//    }

}

