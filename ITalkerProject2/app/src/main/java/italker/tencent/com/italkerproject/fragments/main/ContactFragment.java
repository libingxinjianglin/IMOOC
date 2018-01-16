package italker.tencent.com.italkerproject.fragments.main;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.contact.ContactControl;
import drawable.tencent.com.factory.presenter.contact.ContactPresenter;
import drawable.tencent.com.factory.presenter.contact.FollowControl;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.weiget.EmptyView;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.MessageActivity;
import italker.tencent.com.italkerproject.activits.PsersonActivity;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;
import italker.tencent.com.italkerproject.fragments.search.SearchUserFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends PresenterFragment<ContactControl.Presenter> implements ContactControl.View {

    private RecyclerView mRecyclerView;
    private EmptyView mEmptyView;
    private RecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.contact_recycler);
        mEmptyView = (EmptyView)view.findViewById(R.id.contact_empty);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<User>(){

            @Override
            protected int getItemViewType(int position, User userCard) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<User>(){

            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                MessageActivity.show(getContext(),user);
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, User user) {

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
    public RecyclerAdapter<User> getRecycler() {
        return mAdapter;
    }

    @Override
    public void onAdapterChanage() {
        // 进行界面操作
        mplaceHolder.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected ContactControl.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<User> implements View.OnClickListener{
        private PortraitView mPortraitView;
        private TextView mTextViewDes;
        private TextView mTxtName;

        public ViewHolder(View itemView) {
            super(itemView);
            mPortraitView = itemView.findViewById(R.id.im_portrait);
            mPortraitView.setOnClickListener(this);
            mTxtName = itemView.findViewById(R.id.txt_name);
            mTextViewDes = itemView.findViewById(R.id.txt_desc);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setup(Glide.with(getContext()),user);
            mTxtName.setText(user.getName());
            mTextViewDes.setText(user.getDesc());
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.im_portrait){
                PsersonActivity.show(getActivity(),mData.getId());  //点击头像的时候过来个人信息界面
            }
        }
    }

}
