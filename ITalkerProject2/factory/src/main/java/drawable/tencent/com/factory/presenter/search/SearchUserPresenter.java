package drawable.tencent.com.factory.presenter.search;

import android.support.annotation.StringRes;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.card.UserCard;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BasePresenter;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter,DataSource.Callback<List<UserCard>> {

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }
    private Call call;
    @Override
    public void search(String content) {
        statr();
        Call mCall = call;
        if(mCall != null && mCall.isCanceled()){
            mCall.cancel();
        }
        call = UserHelper.search(content,this);
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final SearchContract.UserView view = getView();
        if(view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView view = getView();
        if(view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }
}
