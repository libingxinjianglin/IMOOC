package drawable.tencent.com.factory.data.helper;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.RegisterControl.RegisenterPresenter;
import italker.tencent.com.common.factory.data.DataSource;

/**
 * 实现注册逻辑
 */

public class AccountHelper {
    public static void Regist(RegiseModel model, final DataSource.Callback<User> callback){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onDataNotAvailable(R.string.data_rsp_error_parameters);
            }
        }.start();
    }
}
