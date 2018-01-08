package drawable.tencent.com.factory.presenter.RegisterControl;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * mvp的思想方法让View和presenter有联系
 */
public interface RegisterControl {
    interface View extends BaseControl.View<Presenter>{
        //注册成功
        void registerSucess();
    }

    interface Presenter extends BaseControl.Presenter{

        //发起一个注册
        void register(String phone,String name,String password);
        //检查手机号是否正确
        boolean check(String phone);

    }

}
