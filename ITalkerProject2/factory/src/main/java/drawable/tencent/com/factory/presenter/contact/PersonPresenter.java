package drawable.tencent.com.factory.presenter.contact;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.persistence.Account;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class PersonPresenter extends BasePresenter<PersonControl.View>
        implements PersonControl.Presenter {

    private User user;

    public PersonPresenter(PersonControl.View view) {
        super(view);
    }

    @Override
    public void statr() {
        super.statr();
        // 个人界面用户数据优先从网络拉取
        Factory.RunAsync(new Runnable() {
            @Override
            public void run() {
                final PersonControl.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(view, user);
                }
            }
        });

    }


    private void onLoaded(final PersonControl.View view, final User user) {
        this.user = user;
        // 是否就是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUser().getId());
        // 是否已经关注
        final boolean isFollow = isSelf || user.isFollow();
        // 已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;

        // 切换到Ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
