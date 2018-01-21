package drawable.tencent.com.factory.data.user;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import drawable.tencent.com.factory.data.helper.DbHelper;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.User;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class UserDispatcher implements UserCenter {
    public static UserCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    public final Executor executor = Executors.newSingleThreadExecutor();
    public static UserCenter getInstance(){
        if(instance == null){
            synchronized (UserDispatcher.class){
                if(instance == null){
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0)
            return;

        // 丢到单线程池中
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            // 单被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                // 进行过滤操作
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                // 添加操作
                users.add(card.build());
            }

            // 进行数据库存储，并分发通知, 异步的操作
            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
