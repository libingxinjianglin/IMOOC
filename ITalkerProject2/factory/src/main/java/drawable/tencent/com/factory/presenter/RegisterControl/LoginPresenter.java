package drawable.tencent.com.factory.presenter.RegisterControl;

import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class LoginPresenter extends BasePresenter<LoginControl.View> implements LoginControl.Presenter {

    public LoginPresenter(LoginControl.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }

    @Override
    public boolean check(String phone) {
        return false;
    }
}
