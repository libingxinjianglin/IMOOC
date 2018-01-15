package drawable.tencent.com.factory.presenter.contact;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;
import drawable.tencent.com.factory.persistence.Account;
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
    }
}
