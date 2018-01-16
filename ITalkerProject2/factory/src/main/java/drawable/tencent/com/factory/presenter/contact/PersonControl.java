package drawable.tencent.com.factory.presenter.contact;

import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public interface PersonControl {
    interface Presenter extends BaseControl.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseControl.View<Presenter> {
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天
        void allowSayHello(boolean isAllow);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
