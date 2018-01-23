package italker.tencent.com.italkerproject.fragments.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.MessageActivity;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public abstract class ChatFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    protected String mReceiverId;

    public Toolbar mToolbar;

    public RecyclerView mRecyclerView;

    public AppBarLayout mAppBarLayout;

    public EditText mContent;

    public View mSubmit;


    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mToolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.recycler);
        mAppBarLayout = view.findViewById(R.id.appbar);
        mContent = view.findViewById(R.id.edit_content);
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mContent.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    mSubmit.setActivated(true);
                }else{
                    mSubmit.setActivated(false);
                }

            }
        });
        mSubmit = view.findViewById(R.id.btn_submit);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initToolbar();
        initAppbar();
    }

    private void initAppbar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    protected void initToolbar(){
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
