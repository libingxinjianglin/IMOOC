package italker.tencent.com.italkerproject.fragments.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.presenter.contact.FollowControl;
import drawable.tencent.com.factory.presenter.contact.FollowPresenter;
import drawable.tencent.com.factory.presenter.search.SearchContract;
import drawable.tencent.com.factory.presenter.search.SearchGroupPresenter;
import drawable.tencent.com.factory.presenter.search.SearchUserPresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.weiget.EmptyView;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.PsersonActivity;
import italker.tencent.com.italkerproject.activits.SearchActivity;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;

/**
 * 聯繫人搜索界面
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements SearchContract.UserView,SearchActivity.SearchFragment {

    private RecyclerView mRecyclerView;
    private EmptyView mEmptyView;
    private RecyclerAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        // 如果有数据，则是OK，没有数据就显示空布局
        mplaceHolder.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        mEmptyView = (EmptyView)view.findViewById(R.id.empty);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<UserCard>(){

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        // 初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    /**
     * 具體進行一個查找,調用mvp
     * @param content
     */
    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowControl.View,View.OnClickListener{
        private PortraitView mPortraitView;
        private TextView mTextViewDes;
        private ImageView mImgeAdd;
        private FollowControl.Presenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            new FollowPresenter(this);
            mPortraitView = itemView.findViewById(R.id.im_portrait);
            mPortraitView.setOnClickListener(this);
            mTextViewDes = itemView.findViewById(R.id.txt_name);
            mImgeAdd = itemView.findViewById(R.id.im_follow);
            mImgeAdd.setOnClickListener(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            Glide.with(getContext())
                    .load(userCard.getPortrait())
                    .centerCrop()
                    .into(mPortraitView);
            mTextViewDes.setText(userCard.getName());
            mImgeAdd.setEnabled(!userCard.isFollow());
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            // 更改当前界面状态
            if (mImgeAdd.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mImgeAdd.getDrawable()).stop();
                // 设置为默认的
                mImgeAdd.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updateData(userCard);
        }

        @Override
        public void showError(int str) {
            // 更改当前界面状态
            if (mImgeAdd.getDrawable() instanceof LoadingDrawable) {
                // 失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) mImgeAdd.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);

            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            // 设置进去
            mImgeAdd.setImageDrawable(drawable);
            // 启动动画
            drawable.start();
        }

        @Override
        public void setPresenter(FollowControl.Presenter presenter) {
            this.mPresenter = presenter;
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.im_follow){
                mPresenter.follow(mData.getId());
            }else if(view.getId() == R.id.im_portrait){
                PsersonActivity.show(getActivity(),mData.getId());  //点击头像的时候过来个人信息界面
            }
        }
    }
}
