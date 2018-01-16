package drawable.tencent.com.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.AppDatabases;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;
import drawable.tencent.com.factory.persistence.Account;
import drawable.tencent.com.factory.utils.DiffUiDataCallback;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class ContactPresenter extends BasePresenter<ContactControl.View> implements ContactControl.Presenter {

    public ContactPresenter(ContactControl.View view) {
        super(view);
    }

    @Override
    public void statr() {
        super.statr();
        //TODO 具體加載數據
        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUser().getId()))
                .orderBy(User_Table.name,true)
                .limit(100)
                .async()  //異步的加載
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecycler().replace(tResult);
                        getView().onAdapterChanage();
                    }
                }).execute();

            UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(@StringRes int strRes) {
                //失敗的話我們就管了
            }

            @Override
            public void onDataLoaded(List<UserCard> userCards) {
                final List<User> users = new ArrayList<User>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }
                // 丢到事物中保存数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabases.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .saveAll(users);
                    }
                }).build().execute();
                List<User> items = getView().getRecycler().getItems();
                diff(items, users);  //比較一下現在的數據沒有有更新

            }
            // TODO 问题：
            // 1.关注后虽然存储数据库，但是没有刷新联系人
            // 2.如果刷新数据库，或者从网络刷新，最终刷新的时候是全局刷新
            // 3.本地刷新和网络刷新，在添加到界面的时候会有可能冲突；导致数据显示异常
            // 4.如何识别已经在数据库中有这样的数据了
        });
    }
    private void diff(List<User> oldList, List<User> newList) {
        DiffUtil.Callback call = new DiffUiDataCallback<User>(oldList,newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(call);
        // 在对比完成后进行数据的赋值
        getView().getRecycler().replace(newList);
        result.dispatchUpdatesTo(getView().getRecycler());
        
        //// TODO: 2018/1/16 0016 存在问题网络上添加数据会报错 

        getView().onAdapterChanage();
    }
}

