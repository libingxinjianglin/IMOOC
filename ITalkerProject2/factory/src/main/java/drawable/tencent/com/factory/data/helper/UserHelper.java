package drawable.tencent.com.factory.data.helper;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.api.UserUpdateModel;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.net.Network;
import drawable.tencent.com.factory.net.RemoteService;
import italker.tencent.com.common.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/1/12 0012.
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
}
