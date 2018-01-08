package italker.tencent.com.italkerproject.fragments.account;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private AccountTrigger account;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         account = (AccountTrigger) context;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData() {
        super.initData();
        account.triggerView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

}
