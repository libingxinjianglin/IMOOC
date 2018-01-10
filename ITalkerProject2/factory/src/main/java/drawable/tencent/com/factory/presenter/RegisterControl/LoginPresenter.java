package drawable.tencent.com.factory.presenter.RegisterControl;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.data.helper.AccountHelper;
import drawable.tencent.com.factory.model.api.LoginModel;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.persistence.Account;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class LoginPresenter extends BasePresenter<LoginControl.View> implements LoginControl.Presenter,DataSource.Callback<User> {

    public LoginPresenter(LoginControl.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        statr();
        LoginControl.View view = getView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            // 尝试传递PushId
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.Login(model, this);
        }
    }


    @Override
    public boolean check(String phone) {
        return false;
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginControl.View view = getView();
        if (view == null)
            return;
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSucess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 网络请求告知注册失败
        final LoginControl.View view = getView();
        if (view == null)
            return;
        // 此时是从网络回送回来的，并不保证处于主现场状态
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 调用主界面注册失败显示错误
                view.showError(strRes);
            }
        });
    }
}
