package drawable.tencent.com.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

import drawable.tencent.com.factory.data.BaseDbRepository;
import drawable.tencent.com.factory.model.db.Session;
import drawable.tencent.com.factory.model.db.Session_Table;
import italker.tencent.com.common.factory.data.DataSource;

/**
 * Created by Administrator on 2018/1/28 0028.
 */

public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        // 数据库查询
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, false) // false 是倒序
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();

        //10
        //9
        //8

        // 复写insert之后导致的问题

        // 8
        // 9
        // 10

    }

    @Override
    protected boolean isRequired(Session session) {
        // 所有的会话我都需要，不需要过滤
        return true;
    }

    @Override
    protected void insert(Session session) {
        // 复写方法，让新的数据加到头部
        dataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        // 复写数据库回来的方法, 进行一次反转
        Collections.reverse(tResult);

        super.onListQueryResult(transaction, tResult);
    }
}
