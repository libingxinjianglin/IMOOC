package drawable.tencent.com.factory.data.user;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import drawable.tencent.com.factory.data.BaseDbRepository;
import drawable.tencent.com.factory.data.helper.DbHelper;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;
import drawable.tencent.com.factory.persistence.Account;
import italker.tencent.com.common.factory.data.DataSource;

/**
 *
 * 提高一个联系人数据源
 * Created by Administrator on 2018/1/20 0020.
 */

public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource{

    private DataSource.SucceedCallback<List<User>> callback;

    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);

        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUser().getId()))
                .orderBy(User_Table.name,true)
                .limit(100)
                .async()  //異步的加載
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUser().getId());
    }

}
