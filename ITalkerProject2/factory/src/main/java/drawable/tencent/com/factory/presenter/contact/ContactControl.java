package drawable.tencent.com.factory.presenter.contact;

import java.util.List;

import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.factory.presenter.BaseControl;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public interface ContactControl {
    //什麼都不要做，開始進行一個start方法就行了
    interface Presenter extends BaseControl.Presenter{

    }
    interface View extends BaseControl.RecyclerView<Presenter,User>{

    }
}
