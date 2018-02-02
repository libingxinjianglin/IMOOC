package drawable.tencent.com.factory.presenter.search;

import android.support.annotation.StringRes;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import drawable.tencent.com.factory.data.helper.GroupHelper;
import drawable.tencent.com.factory.model.card.GroupCard;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BaseControl;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter,DataSource.Callback<List<GroupCard>> {

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        statr();
        GroupHelper.search(content,this);
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 搜索失败
        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }

    @Override
    public void onDataLoaded(final List<GroupCard> groupCards) {
        // 搜索成功
        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(groupCards);
                }
            });
        }
    }
}
