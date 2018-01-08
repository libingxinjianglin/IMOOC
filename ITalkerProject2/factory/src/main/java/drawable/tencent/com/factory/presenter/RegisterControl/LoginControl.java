package drawable.tencent.com.factory.presenter.RegisterControl;

import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public interface LoginControl {
    interface View extends BaseControl.View<Presenter>{
        //注册成功
        void loginSucess();
    }

    interface Presenter extends BaseControl.Presenter{
        //发起一个注册
        void login(String phone,String name,String password);
        //检查手机号是否正确
        boolean check(String phone);

    }
}
