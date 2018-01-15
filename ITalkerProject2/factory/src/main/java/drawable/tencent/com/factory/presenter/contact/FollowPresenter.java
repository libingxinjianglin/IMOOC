package drawable.tencent.com.factory.presenter.contact;

import android.support.annotation.StringRes;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.Ui;

import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.card.UserCard;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class FollowPresenter extends BasePresenter<FollowControl.View> implements FollowControl.Presenter,DataSource.Callback<UserCard> {

    public FollowPresenter(FollowControl.View view) {
        super(view);
    }

    /**
     * 具體進行關注人的實現
     * @param id
     */
    @Override
    public void follow(String id) {
        statr();
        UserHelper.follow(id,this);
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final FollowControl.View view = getView();
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
    public void onDataLoaded(UserCard userCard) {
        final FollowControl.View view = getView();
        if(view != null){
            view.onFollowSucceed(userCard);
        }
    }
}
