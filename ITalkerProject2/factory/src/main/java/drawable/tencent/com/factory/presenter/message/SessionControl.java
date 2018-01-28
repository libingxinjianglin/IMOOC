package drawable.tencent.com.factory.presenter.message;

import drawable.tencent.com.factory.model.db.Session;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/28 0028.
 */

public interface SessionControl {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseControl.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseControl.RecyclerView<Presenter, Session> {

    }
}
