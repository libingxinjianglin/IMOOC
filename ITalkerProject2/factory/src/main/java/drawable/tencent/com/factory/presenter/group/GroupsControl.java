package drawable.tencent.com.factory.presenter.group;

import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.contact.ContactControl;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public interface GroupsControl {
    //什麼都不要做，開始進行一個start方法就行了
    interface Presenter extends BaseControl.Presenter{

    }
    interface View extends BaseControl.RecyclerView<Presenter,Group>{

    }
}
