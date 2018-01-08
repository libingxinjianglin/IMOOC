package drawable.tencent.com.factory.net;

import drawable.tencent.com.factory.model.api.AccountRepModl;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.api.RspModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

}
