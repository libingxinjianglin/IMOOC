package drawable.tencent.com.factory.presenter.group;

import java.util.ArrayList;
import java.util.List;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.view.UserSampleModel;
import italker.tencent.com.common.factory.presenter.BaseRecyclerPresenter;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateControl.ViewModel,GroupCreateControl.View> implements GroupCreateControl.Presenter{

    public GroupCreatePresenter(GroupCreateControl.View view) {
        super(view);
    }

    @Override
    public void statr() {
        super.statr();
        // 加载
       Factory.RunAsync(loader);
    }

    @Override
    public void create(String name, String desc, String picture) {

    }

    @Override
    public void changeSelect(GroupCreateControl.ViewModel model, boolean isSelected) {

    }

    private Runnable loader = new Runnable() {
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateControl.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateControl.ViewModel viewModel = new GroupCreateControl.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };
}
