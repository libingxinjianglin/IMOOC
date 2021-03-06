package drawable.tencent.com.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.api.UserUpdateModel;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;
import drawable.tencent.com.factory.model.db.view.UserSampleModel;
import drawable.tencent.com.factory.net.Network;
import drawable.tencent.com.factory.net.RemoteService;
import drawable.tencent.com.factory.persistence.Account;
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
                    // 唤起进行保存的操作
                    Factory.getUserCenter().dispatch(result);
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
                    Factory.getUserCenter().dispatch(result);//添加一個聯繫人之後我們需要進行一個數據庫存儲
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

    public static void refreshContacts(){
        Retrofit retrofit = Network.getRetrofit();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        Call<RspModel<List<UserCard>>> rspModelCall = remoteService.userContacts();
        rspModelCall.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> body = response.body();
                if(body.success()){
                    List<UserCard> result = body.getResult();
                    if (result == null || result.size() == 0)
                        return;

                    UserCard[] cards1 = result.toArray(new UserCard[0]);
                    Factory.getUserCenter().dispatch(cards1);
                }else{
                    Factory.decodeRspCode(body,null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
            }
        });
    }

    // 从本地查询一个用户的信息
    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    public static User findFromNet(String id) {

        Retrofit retrofit = Network.getRetrofit();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card != null) {

                // TODO 数据库的存储但是没有通知
                User user = card.build();
                DbHelper.save(User.class,user);

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 搜索一个用户，优先本地缓存，
     * 没有用然后再从网络拉取
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络查询
     * 没有用然后再从本地缓存拉取
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }

    /**
     * 获取联系人
     */
    public static List<User> getContact() {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUser().getId()))
                .limit(100)
                .queryList();
    }


    // 获取一个联系人列表，
    // 但是是一个简单的数据的
    public static List<UserSampleModel> getSampleContact() {
        //"select id = ??";
        //"select User_id = ??";
        return SQLite.select(User_Table.id.withTable().as("id"),
                User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait"))
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUser().getId()))
                .orderBy(User_Table.name, true)
                .queryCustomList(UserSampleModel.class);
    }

}
