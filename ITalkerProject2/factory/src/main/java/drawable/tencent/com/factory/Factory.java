package drawable.tencent.com.factory;


import android.app.Application;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import drawable.tencent.com.factory.data.group.GroupCenter;
import drawable.tencent.com.factory.data.user.UserCenter;
import drawable.tencent.com.factory.data.user.UserDispatcher;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.persistence.Account;
import drawable.tencent.com.factory.utils.DBFlowExclusion;
import italker.tencent.com.common.app.MyApplication;
import italker.tencent.com.common.factory.data.DataSource;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

/**
 * 全局上面的一个引用
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
                .setExclusionStrategies(new DBFlowExclusion())
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

    /**
     * 进行错误Code的解析，
     * 把网络返回的Code值进行统一的规划并返回为一个String资源
     *
     * @param model    RspModel
     * @param callback DataSource.FailedCallback 用于返回一个错误的资源Id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Toast.makeText(getApp(),getApp().getString(R.string.data_rsp_error_account_token),Toast.LENGTH_LONG).show();
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode( final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }


    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {

    }

    /**
     * 处理推送来的消息
     *
     * @param message 消息
     */
    public static void dispatchPush(String message) {
        // TODO

    }

    public static void setup(){
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)        //数据库初始化的时候就打开数据库
                .build());
        Account.load(app());
    }

    /**
     * 返回一个全局的Application
     * @return
     */
    public static MyApplication app(){
        return MyApplication.getInstance();
    }

    public static UserCenter getUserCenter() {
        return UserDispatcher.getInstance();
    }

}
