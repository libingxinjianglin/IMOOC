package drawable.tencent.com.factory.presenter.RegisterControl;

import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public interface UpdateInfoContract {
     interface view extends BaseControl.View<presenter>{
         // 回调成功
         void updateSucceed();
     }
    interface presenter extends BaseControl.Presenter{
        // 更新
        void update(String photoFilePath, String desc, boolean isMan);
    }
}
