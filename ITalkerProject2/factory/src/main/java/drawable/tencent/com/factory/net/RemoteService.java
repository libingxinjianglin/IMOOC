package drawable.tencent.com.factory.net;

import java.util.List;

import drawable.tencent.com.factory.model.api.AccountRepModl;
import drawable.tencent.com.factory.model.api.GroupCreateModel;
import drawable.tencent.com.factory.model.api.GroupMemberAddModel;
import drawable.tencent.com.factory.model.api.LoginModel;
import drawable.tencent.com.factory.model.api.MsgCreateModel;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.api.UserUpdateModel;
import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.card.GroupMemberCard;
import drawable.tencent.com.factory.model.card.MessageCard;
import drawable.tencent.com.factory.model.card.UserCard;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public interface RemoteService {

    /**
     * 网络请求一个注册接口
     *
     * @param model 传入的是RegisterModel
     * @return 返回的是RspModel<AccountRspModel>
     */
    @POST("account/regist")
    Call<RspModel<AccountRepModl>> accountRegister(@Body RegiseModel model);

    @POST("account/login")
    Call<RspModel<AccountRepModl>> accountLogin(@Body LoginModel model);

    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRepModl>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    // 用户更新的接口
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    @PUT("user/follow/{followId}")
    Call<RspModel<UserCard>> userFollow(@Path("followId") String followId);

    // 获取联系人列表
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    //获得指定用户id的用户的信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    //发送消息
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    //获取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    // 群搜索的接口
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    // 我的群的成员列表
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 给群添加成员
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);
}


