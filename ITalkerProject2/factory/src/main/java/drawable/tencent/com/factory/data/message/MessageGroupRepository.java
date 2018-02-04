package drawable.tencent.com.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

import drawable.tencent.com.factory.data.BaseDbRepository;
import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.model.db.Message_Table;

/**
 * 具体的一个观察者,和某人聊天的时候聊天记录列表
 * 关注的内容一定是我发给这群的
 * Created by Administrator on 2018/1/24 0024.
 */

public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {

    // 聊天的群Id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);
        SQLite.select().from(Message.class).where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
       //TODO
    }

    @Override
    protected boolean isRequired(Message message) {
        return message.getGroup() != null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {

        // 反转返回的集合
        Collections.reverse(tResult);
        // 然后再调度
        super.onListQueryResult(transaction, tResult);
    }
}
