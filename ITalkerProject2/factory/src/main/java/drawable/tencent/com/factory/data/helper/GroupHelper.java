package drawable.tencent.com.factory.data.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.model.api.GroupCreateModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.Group_Table;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.net.Network;
import drawable.tencent.com.factory.net.RemoteService;
import drawable.tencent.com.factory.presenter.search.SearchGroupPresenter;
import italker.tencent.com.common.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class GroupHelper {
    public static Group findFromLocal(String groupId) {
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null)
            group = findFormNet(groupId);
        return group;
    }

    private static Group findFormNet(String groupId) {
        Retrofit retrofit = Network.getRetrofit();
        RemoteService service = retrofit.create(RemoteService.class);
        try {
            Response<RspModel<GroupCard>> response = service.groupFind(groupId).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                // 数据库的存储并通知
                Factory.getGroupCenter().dispatch(card);
                //得到管理员
                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        //进行网络请求
        Retrofit retrofit = Network.getRetrofit();
        RemoteService service = retrofit.create(RemoteService.class);
        Call<RspModel<GroupCard>> call = service.groupCreate(model);
        call.enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                RspModel<GroupCard> body = response.body();
                if (body.success()) {
                    GroupCard result = body.getResult();
                    Factory.getGroupCenter().dispatch(result);
                    callback.onDataLoaded(result);

                } else {
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }


    public static void search(String content, final DataSource.Callback<List<GroupCard>> callback) {
        //进行网络请求
        Retrofit retrofit = Network.getRetrofit();
        RemoteService service = retrofit.create(RemoteService.class);
        Call<RspModel<List<GroupCard>>> rspModelCall = service.groupSearch(content);
        rspModelCall.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> body = response.body();
                if(body.success()){
                    callback.onDataLoaded(body.getResult());
                }else{
                    Factory.decodeRspCode(body,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
    //刷新群组的一个列表
    public static void refreshGroup() {
        //进行网络请求
        Retrofit retrofit = Network.getRetrofit();
        RemoteService service = retrofit.create(RemoteService.class);
        service.groups("").enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel.success()) {
                    List<GroupCard> groupCards = rspModel.getResult();
                    if (groupCards != null && groupCards.size() > 0) {
                        // 进行调度显示
                        Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupCard[0]));
                    }
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                // 不做任何事情
            }
        });
    }
}
