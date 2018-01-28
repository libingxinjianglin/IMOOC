package italker.tencent.com.italkerproject.fragments.main;


import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import drawable.tencent.com.factory.model.db.Session;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.message.SessionControl;
import drawable.tencent.com.factory.presenter.message.SessionPresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.utils.DataTime;
import italker.tencent.com.common.weiget.EmptyView;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.GalleyView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.MessageActivity;
import italker.tencent.com.italkerproject.activits.PsersonActivity;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionFragment extends PresenterFragment<SessionControl.Presenter> implements SessionControl.View {

    private RecyclerView mRecyclerView;
    private EmptyView mEmptyView;
    private RecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_action;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.contact_recycler);
        mEmptyView = (EmptyView)view.findViewById(R.id.contact_empty);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<Session>(){

            @Override
            protected int getItemViewType(int position, Session userCard) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActionFragment.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListener<Session>(){

            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session user) {
                MessageActivity.show(getContext(),user);
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Session user) {

            }
        });
        // 初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public void initFirstData() {
        super.initFirstData();
        mPresenter.statr();
    }

    @Override
    public RecyclerAdapter<Session> getRecycler() {
        return mAdapter;
    }

    @Override
    public void onAdapterChanage() {
        // 进行界面操作
        mplaceHolder.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SessionControl.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<Session> implements View.OnClickListener{
        private PortraitView mPortraitView;
        private TextView mTextViewDes;
        private TextView mTxtName;
        private TextView mData;

        public ViewHolder(View itemView) {
            super(itemView);
            mPortraitView = itemView.findViewById(R.id.im_portrait);
            mTxtName = itemView.findViewById(R.id.txt_name);
            mTextViewDes = itemView.findViewById(R.id.txt_desc);
            mData = itemView.findViewById(R.id.txt_time);
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActionFragment.this), session.getPicture());
            mTxtName.setText(session.getTitle());
            mTextViewDes.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mData.setText(DataTime.getSampleData(session.getModifyAt()));
        }

        @Override
        public void onClick(View view) {

        }
    }
}
