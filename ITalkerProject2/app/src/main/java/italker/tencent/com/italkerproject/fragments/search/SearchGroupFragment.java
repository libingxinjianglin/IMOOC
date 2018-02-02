package italker.tencent.com.italkerproject.fragments.search;




import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.presenter.contact.FollowControl;
import drawable.tencent.com.factory.presenter.contact.FollowPresenter;
import drawable.tencent.com.factory.presenter.search.SearchContract;
import drawable.tencent.com.factory.presenter.search.SearchGroupPresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.weiget.EmptyView;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.SearchActivity;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;

/**
 * 群組搜索界面
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter> implements SearchContract.GroupView,SearchActivity.SearchFragment {
    private RecyclerView mRecyclerView;
    private EmptyView mEmptyView;
    private RecyclerAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        mEmptyView = (EmptyView)view.findViewById(R.id.empty);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>(){

            @Override
            protected int getItemViewType(int position, GroupCard userCard) {
                return R.layout.cell_search_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });
        // 初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        mAdapter.replace(groupCards);
        // 如果有数据，则是OK，没有数据就显示空布局
        mplaceHolder.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void search(String content) {     //表示的是接口的一個search不是mvp裡面的
        mPresenter.search(content);
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> implements View.OnClickListener{

        private PortraitView mPortraitView;
        private TextView mTextViewDes;
        private ImageView mImgeAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            mPortraitView = itemView.findViewById(R.id.im_portrait);
            mTextViewDes = itemView.findViewById(R.id.txt_name);
            mImgeAdd = itemView.findViewById(R.id.im_join);
            mImgeAdd.setOnClickListener(this);
        }

        @Override
        protected void onBind(GroupCard groupCard) {
            mPortraitView.setup(Glide.with(getContext()),groupCard.getPicture());
            mTextViewDes.setText(groupCard.getName());
            mImgeAdd.setEnabled(groupCard.getModifyAt() == null);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.im_join){

            }
        }
    }
}
