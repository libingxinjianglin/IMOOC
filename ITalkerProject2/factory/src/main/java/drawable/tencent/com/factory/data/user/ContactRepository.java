package drawable.tencent.com.factory.data.user;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

public class ContactRepository implements ContactDataSource,QueryTransaction.QueryResultListCallback<User>,
        DbHelper.ChangedListener<User>{

    private DataSource.SucceedCallback<List<User>> callback;

    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        this.callback = callback;
        DbHelper.addChangedListener(User.class,this);
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
    public void dispose() {
        //当数据库数据删除的操作
        this.callback = null;
        DbHelper.removeChangedListener(User.class,this);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {

        if (tResult.size() == 0) {
            mList.clear();
            notifyDataChanage();
            return;
        }
        // 转变为数组
        User[] users = tResult.toArray(new User[0]);
        // 回到数据集更新的操作中
        onDataSave(users);
    }



    @Override
    public void onDataSave(User... list) {
        Boolean isChanaged = false;
        for (User user : list) {
            //是关注的人但是不是我自己
            if(required(user)){
                insertOrUpdate(user);
                isChanaged = true;
            }
        }
        if(isChanaged){
            notifyDataChanage();
        }
    }

    @Override
    public void onDataDelete(User... list) {
        Boolean isChanaged = false;
        for (User user : list) {
            mList.remove(user);
            isChanaged = true;
        }
        //有数据变更则进行界面刷新
        if(isChanaged){
            notifyDataChanage();
        }
    }

    List<User> mList = new LinkedList<>();
    public void insertOrUpdate(User user){
        int index = indexOf(user);
        if(index>=0){
            replace(index,user);
        }else{
            insert(user);
        }
    }

    private void replace(int incex,User user){
        mList.remove(incex);
        mList.add(incex,user);
    }
    private void insert(User user){
        mList.add(user);
    }

    private int indexOf(User user){
        int index = -1;
        for (User user1 : mList) {
            index ++;
            if(user1.isSame(user)){
                return index;
            }
        }
        return -1;
    }

    private void notifyDataChanage(){
        if(callback != null){
            callback.onDataLoaded(mList);
        }
    }

    public Boolean required(User user){
        return user.isFollow() && !user.getId().equals(Account.getUser().getId());
    }
}
