package italker.tencent.com.italkerproject.fragments.main;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.contact.ContactControl;
import drawable.tencent.com.factory.presenter.group.GroupsControl;
import drawable.tencent.com.factory.presenter.group.GroupsPresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.weiget.EmptyView;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends PresenterFragment<GroupsControl.Presenter> implements GroupsControl.View {

    private RecyclerView mRecyclerView;
    private EmptyView mEmptyView;
    private RecyclerAdapter mAdapter;
    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void initFirstData() {
        super.initFirstData();
        mPresenter.statr();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mRecyclerView = view.findViewById(R.id.contact_recycler);
        mEmptyView = view.findViewById(R.id.contact_empty);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<Group>(){

            @Override
            protected int getItemViewType(int position, Group group) {
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public RecyclerAdapter<Group> getRecycler() {
        return mAdapter;
    }

    @Override
    public void onAdapterChanage() {
        // 进行界面操作
       mplaceHolder.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected GroupsControl.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<Group>{
        private PortraitView mPortraitView;
        private TextView mTextViewDes;
        private TextView mTxtName;
        private TextView mTxtMembers;

        public ViewHolder(View itemView) {
            super(itemView);
            mPortraitView = itemView.findViewById(R.id.im_portrait);
            mTxtName = itemView.findViewById(R.id.txt_name);
            mTextViewDes = itemView.findViewById(R.id.txt_desc);
            mTxtMembers = itemView.findViewById(R.id.txt_member);
        }

        @Override
        protected void onBind(Group user) {
            mPortraitView.setup(Glide.with(getContext()),user.getPicture());
            mTxtName.setText(user.getName());
            mTextViewDes.setText(user.getDesc());
            if (user.holder != null && user.holder instanceof String) {
                mTxtMembers.setText((String) user.holder);
            } else {
                mTxtMembers.setText("");
            }
        }

    }
}
