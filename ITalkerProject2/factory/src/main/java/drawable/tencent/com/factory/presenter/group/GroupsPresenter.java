package drawable.tencent.com.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import java.util.List;

import drawable.tencent.com.factory.data.group.GroupsDataSource;
import drawable.tencent.com.factory.data.group.GroupsRepository;
import drawable.tencent.com.factory.data.helper.GroupHelper;
import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.BaseSourcePresenter;
import drawable.tencent.com.factory.utils.DiffUiDataCallback;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class GroupsPresenter extends BaseSourcePresenter<Group,Group,GroupsDataSource,GroupsControl.View>
        implements GroupsControl.Presenter, DataSource.SucceedCallback<List<Group>>{

    public GroupsPresenter(GroupsControl.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void statr() {
        super.statr();
        GroupHelper.refreshGroup();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        GroupsControl.View view = getView();
        if (view != null) {
            RecyclerAdapter<Group> adapter = view.getRecycler();
            List<Group> oldData = adapter.getItems();
            DiffUtil.Callback data = new DiffUiDataCallback<>(oldData, groups);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(data);
            refreshData(result, groups);
        }
    }
}
