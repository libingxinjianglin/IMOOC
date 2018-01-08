package italker.tencent.com.italkerproject.fragments.account;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import drawable.tencent.com.factory.presenter.RegisterControl.RegisenterPresenter;
import drawable.tencent.com.factory.presenter.RegisterControl.RegisterControl;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * 注册界面在mvp里面是一个View，所以需要继承一下BaseControl.View
 */
public class RegisterFragment extends PresenterFragment<RegisterControl.Presenter> implements RegisterControl.View,View.OnClickListener {

    private EditText mPhone;
    private EditText mName;
    private EditText mPassword;

    private Button mSubmit;
    private TextView mGo_login;
    private Loading mLoading;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AccountTrigger account = (AccountTrigger) context;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mName = (EditText)view.findViewById(R.id.edit_name);
        mPhone = (EditText)view.findViewById(R.id.edit_phone);
        mPassword = (EditText)view.findViewById(R.id.edit_password);
        mSubmit = (Button)view.findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(this);
        mGo_login = (TextView) view.findViewById(R.id.txt_go_login);
        mGo_login.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void registerSucess() {

    }

    @Override
    public void showError(int str) {
        super.showError(str);
        mName.setEnabled(true);
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
        mGo_login.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mName.setEnabled(false);
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
        mGo_login.setEnabled(false);
    }

    /**
     * 具体的实现类，实现一个presenter
     * @return
     */
    @Override
    protected RegisterControl.Presenter initPresenter() {
        //赋初值presenter
        return new RegisenterPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_submit){
            String name = mName.getText().toString();
            String password = mPassword.getText().toString();
            String phone = mPhone.getText().toString();

            /**
             * 初始化的时候我们就要实例化这个对象
             */
            mPresenter.register(phone,name,password);
        }
    }
}
