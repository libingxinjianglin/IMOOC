package drawable.tencent.com.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.model.api.AccountRepModl;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

/**
 * 程序入口，进行判断程序是否已经登入了，有没有绑定设备ID
 */
public class Account {
    // 设备的推送Id
    private static String pushId;
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    // 设备Id是否已经绑定到了服务器
    private static boolean bindId;

    private static String account;
    private static String token;
    private static String userID;

    public static String getPushId(){
        return Account.pushId;
    }

    public static void setPushId(String id){
        Account.pushId = id;
        sava(Factory.app());
    }

    public static Boolean isLogin(){
        return !TextUtils.isEmpty(Account.token) && !TextUtils.isEmpty(Account.userID);
    }

    public static void setbindId(boolean isBind) {
        Account.bindId = isBind;
        sava(Factory.app());
    }

    public  static Boolean isBind(){
        return Account.bindId;
    }
    /**
     * 获取当前登录的Token
     *
     * @return Token
     */
    public static String getToken() {
        return token;
    }

    /**
     * 进行数据存储
     * @param context
     */
    public static void sava(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(KEY_PUSH_ID,pushId)
                .putBoolean(KEY_IS_BIND,bindId)
                .putString(KEY_TOKEN,token)
                .putString(KEY_ACCOUNT,account)
                .putString(KEY_USER_ID,userID)
                .apply();
    }

    /**
     * 进行数据加载
     * @param context
     */
    public static void load(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId = sharedPreferences.getString(KEY_PUSH_ID,"");
        bindId = sharedPreferences.getBoolean(KEY_IS_BIND,false);
        token = sharedPreferences.getString(KEY_TOKEN, "");
        userID = sharedPreferences.getString(KEY_USER_ID, "");
        account = sharedPreferences.getString(KEY_ACCOUNT, "");
    }

    public static void login(AccountRepModl model){
       Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userID = model.getUser().getId();
        sava(Factory.app());
    }

    public static User getUser(){
        return TextUtils.isEmpty(userID) ? new User():
                SQLite.select().from(User.class).where(User_Table.id.eq(userID)).querySingle();
    }

    public static Boolean isComplete(){
        //首先保证登入成功
        if(isLogin()){
            User user = Account.getUser();
            return !TextUtils.isEmpty(user.getDesc()) &&
                    !TextUtils.isEmpty(user.getPortrait()) &&
                    user.getSex() != 0;
        }
        return false;
    }
}
