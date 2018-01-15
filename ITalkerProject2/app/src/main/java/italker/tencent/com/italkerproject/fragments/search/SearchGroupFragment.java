package italker.tencent.com.italkerproject.fragments.search;




import java.util.List;

import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.presenter.search.SearchContract;
import drawable.tencent.com.factory.presenter.search.SearchGroupPresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.SearchActivity;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;

/**
 * 群組搜索界面
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter> implements SearchContract.GroupView,SearchActivity.SearchFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void search(String content) {     //表示的是接口的一個search不是mvp裡面的
        mPresenter.search(content);
    }
}
