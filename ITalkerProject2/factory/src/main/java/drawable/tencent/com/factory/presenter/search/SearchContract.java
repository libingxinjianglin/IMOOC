package drawable.tencent.com.factory.presenter.search;

import java.util.List;

import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.card.UserCard;
import italker.tencent.com.common.factory.presenter.BaseControl;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
public interface SearchContract {
    interface Presenter extends BaseControl.Presenter {
        // 搜索内容
        void search(String content);
    }

    // 搜索人的界面
    interface UserView extends BaseControl.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    // 搜索群的界面
    interface GroupView extends BaseControl.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }
}
