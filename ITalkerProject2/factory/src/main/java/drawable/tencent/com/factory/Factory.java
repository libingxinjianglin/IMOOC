package drawable.tencent.com.factory;


import android.app.Application;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import drawable.tencent.com.factory.data.group.GroupCenter;
import drawable.tencent.com.factory.data.group.GroupDispatcher;
import drawable.tencent.com.factory.data.message.MessageCenter;
import drawable.tencent.com.factory.data.message.MessageDispatcher;
import drawable.tencent.com.factory.data.user.UserCenter;
import drawable.tencent.com.factory.data.user.UserDispatcher;
import drawable.tencent.com.factory.model.api.PushModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.card.GroupMemberCard;
import drawable.tencent.com.factory.model.card.MessageCard;
import drawable.tencent.com.factory.model.card.UserCard;
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
     * @param str 消息
     */
    public static void dispatchPush(String str) {
        // 首先检查登录状态
        if (!Account.isLogin())
            return;

        PushModel model = PushModel.decode(str);
        if (model == null)
            return;

        // 对推送集合进行遍历
        for (PushModel.Entity entity : model.getEntities()) {

            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    instance.logout();
                    // 退出情况下，直接返回，并且不可继续
                    return;

                case PushModel.ENTITY_TYPE_MESSAGE: {
                    // 普通消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    // 好友添加
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    // 添加群
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    // 群成员变更, 回来的是一个群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>() {
                    }.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    // 把数据集合丢到数据中心处理
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS: {
                    // TODO 成员退出的推送
                }

            }
        }
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

    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.getInstance();
    }

    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }


}
