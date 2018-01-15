package drawable.tencent.com.factory.data.helper;

import java.util.List;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.api.UserUpdateModel;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.net.Network;
import drawable.tencent.com.factory.net.RemoteService;
import drawable.tencent.com.factory.presenter.contact.FollowPresenter;
import italker.tencent.com.common.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

/**
 * 完善用戶信息的
 */
public class UserHelper {
    public static void update(UserUpdateModel model,final DataSource.Callback<UserCard> callback){
        Retrofit retrofit = Network.getRetrofit();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        Call<RspModel<UserCard>> call = remoteService.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> body = response.body();
                if(body.success()){
                    UserCard result = body.getResult();
                    // 数据库的存储操作，需要把UserCard转换为User
                    // 保存用户信息
                    User user = result.build();
                    user.save();
                    // 返回成功
                    callback.onDataLoaded(result);
                }else{
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 搜索的操作
     */
    public static Call search(String name,final DataSource.Callback<List<UserCard>> callback) {
        Retrofit retrofit = Network.getRetrofit();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        Call<RspModel<List<UserCard>>> rspModelCall = remoteService.userSearch(name);
        rspModelCall.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> body = response.body();
                if(body.success()){
                    List<UserCard> result = body.getResult();
                    if(result != null){
                        callback.onDataLoaded(result);
                    }
                }else {
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return rspModelCall;
    }

    /**
     * 添加聯繫人操作
     * @param id
     * @param callback
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        Retrofit retrofit = Network.getRetrofit();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        Call<RspModel<UserCard>> rspModelCall = remoteService.userFollow(id);
        rspModelCall.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> body = response.body();
                if(body.success()){
                    UserCard result = body.getResult();
                    User user = result.build();
                    user.save();              //添加一個聯繫人之後我們需要進行一個數據庫存儲
                    //TODO 通知聯繫人列表刷新
                    callback.onDataLoaded(result);
                }else{
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback){
        Retrofit retrofit = Network.getRetrofit();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        Call<RspModel<List<UserCard>>> rspModelCall = remoteService.userContacts();
        rspModelCall.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> body = response.body();
                if(body.success()){
                    List<UserCard> result = body.getResult();
                    callback.onDataLoaded(result);
                }else{
                    Factory.decodeRspCode(body,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
