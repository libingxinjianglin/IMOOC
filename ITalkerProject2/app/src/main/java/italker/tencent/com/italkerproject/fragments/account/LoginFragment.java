package italker.tencent.com.italkerproject.fragments.account;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import drawable.tencent.com.factory.presenter.RegisterControl.LoginControl;
import drawable.tencent.com.factory.presenter.RegisterControl.LoginPresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.italkerproject.MainActivity;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends PresenterFragment<LoginControl.Presenter> implements LoginControl.View,View.OnClickListener {

    private AccountTrigger account;
    private EditText mPhone;
    private EditText mPassword;

    private Button mSubmit;
    private TextView mGo_login;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         account = (AccountTrigger) context;
    }

    @Override
    protected LoginControl.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mPhone = (EditText)view.findViewById(R.id.edit_phone);
        mPassword = (EditText)view.findViewById(R.id.edit_password);
        mSubmit = (Button)view.findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(this);
        mGo_login = (TextView) view.findViewById(R.id.txt_go_register);
        mGo_login.setOnClickListener(this);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
        mGo_login.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
        mGo_login.setEnabled(false);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void loginSucess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_submit){
            String password = mPassword.getText().toString();
            String phone = mPhone.getText().toString();

            /**
             * 初始化的时候我们就要实例化这个对象
             */
            mPresenter.login(phone,password);
        }else if(view.getId() == R.id.txt_go_register){
            account.triggerView();
        }
    }
}
