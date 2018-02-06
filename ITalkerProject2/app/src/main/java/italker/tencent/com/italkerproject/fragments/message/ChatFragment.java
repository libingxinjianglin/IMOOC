package italker.tencent.com.italkerproject.fragments.message;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.structure.Model;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.util.Objects;

import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.persistence.Account;
import drawable.tencent.com.factory.presenter.message.ChatControl;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.face.Face;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.MessageActivity;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;
import italker.tencent.com.italkerproject.fragments.panel.FaceFragment;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public abstract class ChatFragment<Model> extends PresenterFragment<ChatControl.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,ChatControl.View<Model>,FaceFragment.PanelCallback {
    protected String mReceiverId;

    public Toolbar mToolbar;

    public RecyclerView mRecyclerView;

    public AppBarLayout mAppBarLayout;

    public EditText mContent;

    public View mSubmit;

    public ImageView mFaceView;

    private Adapter adapter;

    public ImageView mHead;

    public  CollapsingToolbarLayout mCollapsingToolbarLayout;

    private AirPanel.Boss boss;

    private FaceFragment fragmentById;


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
        mHead = view.findViewById(R.id.im_header);
        mAppBarLayout = view.findViewById(R.id.appbar);
        mContent = view.findViewById(R.id.edit_content);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        fragmentById = (FaceFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        fragmentById.setup(this);
        boss =  view.findViewById(R.id.lay_contenct);
        mFaceView = view.findViewById(R.id.btn_face);
        mFaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //表情界面显示
                boss.openPanel();
                fragmentById.showFace();
            }
        });
        boss.setup(new AirPanel.PanelListener() {
            @Override
            public void requestHideSoftKeyboard() {
                //请求隐藏软键盘
                //参数是焦点的空控件
                Util.hideKeyboard(mContent);
            }
        });
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
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSubmit.isActivated()){
                    mPresenter.pushText(mContent.getText().toString());
                    mContent.setText("");

                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter();
        mRecyclerView.setAdapter(adapter);
        initToolbar();
        initAppbar();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.statr();
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

    private class Adapter extends RecyclerAdapter<Message>{

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected int getItemViewType(int position, Message message) {
            //返回4种布局
            // 我发送的在右边，收到的在左边
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUser().getId());
            switch (message.getType()) {   //根据消息的类型来进行一个判断到底是什么类型的一个布局
                // 文字内容
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;

                // 语音内容
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;

                // 图片内容
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

            }
            return 0;
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // 左右都是同一个
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);


                // 默认情况下，返回的就是Text类型的Holder进行处理
                default:
                    return new TextHolder(root);
            }
        }
    }

    private class BaseViewHolder extends RecyclerAdapter.ViewHolder<Message> implements View.OnClickListener{

        private PortraitView mPortrait;

        // 允许为空，左边没有，右边有
        private Loading mLoading;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mPortrait = itemView.findViewById(R.id.im_portrait);
            mPortrait.setOnClickListener(this);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 进行数据加载
            sender.load();
            // 头像加载
            mPortrait.setup(Glide.with(ChatFragment.this), sender);

            if (mLoading != null) {
                // 当前布局应该是在右边
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    // 正常状态, 隐藏Loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // 正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // 发送失败状态, 允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }

                // 当状态是错误状态时才允许点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.im_portrait){
                // TODO 进行重新发送操作
                mPresenter.rePush(mData);
                updateData(mData);
            }
        }
    }
    class TextHolder extends BaseViewHolder {

        private TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.txt_content);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            Spannable mSpan = new SpannableString(message.getContent());
            Face.decode(mContent,mSpan, (int) Ui.dipToPx(getResources(),20));
            // 把内容设置到布局上
            mContent.setText(mSpan);

        }
    }
    class AudioHolder extends BaseViewHolder {


        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

        }
    }
    class PicHolder extends BaseViewHolder {

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

        }
    }

    @Override
    public void onAdapterChanage() {
        //我们没有占位布局所以我们不做任何的处理
        if(adapter.getItemCount() > 0){
            mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        }
    }

    @Override
    public RecyclerAdapter<Message> getRecycler() {
        return adapter;
    }

    @Override
    public EditText getInputEditText() {
        return mContent;
    }
}
