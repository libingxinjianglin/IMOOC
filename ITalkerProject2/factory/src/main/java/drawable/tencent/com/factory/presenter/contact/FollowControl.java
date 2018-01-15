package drawable.tencent.com.factory.presenter.contact;

import drawable.tencent.com.factory.model.card.UserCard;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

/**
 * 關注人的一個Control
 */
public interface FollowControl {
    // 任务调度者
    interface Presenter extends BaseControl.Presenter {
        // 关注一个人
        void follow(String id);
    }

    interface View extends BaseControl.View<Presenter> {
        // 成功的情况下返回一个用户的信息
        void onFollowSucceed(UserCard userCard);
    }
}
