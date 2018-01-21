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
import drawable.tencent.com.factory.data.user.ContactDataSource;
import drawable.tencent.com.factory.data.user.ContactRepository;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.AppDatabases;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;
import drawable.tencent.com.factory.persistence.Account;
import drawable.tencent.com.factory.utils.DiffUiDataCallback;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BasePresenter;
import italker.tencent.com.common.factory.presenter.BaseRecyclerPresenter;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class ContactPresenter extends BaseRecyclerPresenter<User,ContactControl.View> implements ContactControl.Presenter,
        DataSource.SucceedCallback<List<User>>{

    private ContactDataSource dataSource;
    public ContactPresenter(ContactControl.View view) {
        super(view);
        dataSource = new ContactRepository();
    }

    @Override
    public void statr() {
        super.statr();
        //TODO 具體加載數據
        //观察者模式这里面已经给我们进行了一次数据库的刷新
        dataSource.load(this);
        //网络更新
        UserHelper.refreshContacts();
            // TODO 问题：
            // 1.关注后虽然存储数据库，但是没有刷新联系人
            // 2.如果刷新数据库，或者从网络刷新，最终刷新的时候是全局刷新
            // 3.本地刷新和网络刷新，在添加到界面的时候会有可能冲突；导致数据显示异常
            // 4.如何识别已经在数据库中有这样的数据了
    }

    @Override
    public void onDataLoaded(final List<User> users) {
        ContactControl.View view = getView();
        if(view != null){
            RecyclerAdapter<User> adapter = view.getRecycler();
            List<User> oldData = adapter.getItems();
            DiffUtil.Callback data = new DiffUiDataCallback<>(oldData,users);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(data);
            refreshData(result,users);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        // 当界面销毁的时候，我们应该把数据监听进行销毁
        dataSource.dispose();
    }

}

