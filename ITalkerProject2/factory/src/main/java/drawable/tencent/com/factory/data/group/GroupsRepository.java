package drawable.tencent.com.factory.data.group;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import drawable.tencent.com.factory.data.BaseDbRepository;
import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.Group_Table;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class GroupsRepository extends BaseDbRepository<Group> implements GroupsDataSource {


    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }
    @Override
    protected boolean isRequired(Group group) {
        return true;
    }
}
