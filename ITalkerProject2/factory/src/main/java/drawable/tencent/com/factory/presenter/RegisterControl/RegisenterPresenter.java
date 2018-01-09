package drawable.tencent.com.factory.presenter.RegisterControl;

import android.media.MediaCodec;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.data.helper.AccountHelper;
import drawable.tencent.com.factory.model.api.RegiseModel;
import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

/**
 * 具体的一个RegistenerPresenter
 */
public class RegisenterPresenter extends BasePresenter<RegisterControl.View>
        implements RegisterControl.Presenter,DataSource.Callback<User>{

    public RegisenterPresenter(RegisterControl.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        statr();
        RegisterControl.View view = getView();
        if(check(phone)){
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if(name.length()<2){
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if(password.length()<6){
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else{
            //进行简单的网络请求
           final RegiseModel model = new RegiseModel(password,name,phone);
            AccountHelper.Regist(model,RegisenterPresenter.this);
        }
    }

    @Override
    public boolean check(String phone) {
        return TextUtils.isEmpty(phone);
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final RegisterControl.View view = getView();
        if(view == null){
            return ;
        }
        Run.onUiSync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }

    @Override
    public void onDataLoaded(User user) {
        final RegisterControl.View view = getView();
        if(view == null){
            return ;
        }
        Run.onUiSync(new Action() {
            @Override
            public void call() {
                view.registerSucess();
            }
        });
    }
}
