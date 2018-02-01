package drawable.tencent.com.factory.presenter.group;

import italker.tencent.com.common.factory.model.Autor;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public interface GroupCreateControl {
    interface Presenter extends BaseControl.Presenter {
        // 创建
        void create(String name, String desc, String picture);

        // 更改一个Model的选中状态
        void changeSelect(ViewModel model, boolean isSelected);
    }

    interface View extends BaseControl.RecyclerView<Presenter, ViewModel> {
        // 创建成功
        void onCreateSucceed();
    }

    class ViewModel {
        // 用户信息
        public Autor author;
        // 是否选中
        public boolean isSelected;
    }
}
