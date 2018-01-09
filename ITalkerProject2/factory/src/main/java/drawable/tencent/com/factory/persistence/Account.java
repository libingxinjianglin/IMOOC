package drawable.tencent.com.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import drawable.tencent.com.factory.Factory;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class Account {
    // 设备的推送Id
    private static String pushId;
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    // 设备Id是否已经绑定到了服务器
    private static boolean isBind;

    public static String getPushId(){
        return Account.pushId;
    }

    public static void setPushId(String id){
        Account.pushId = id;
        sava(Factory.app());
    }

    public static Boolean isLogin(){
        return true;
    }

    public  static Boolean isBind(){
        return false;
    }

    /**
     * 进行数据存储
     * @param context
     */
    public static void sava(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(KEY_PUSH_ID,pushId).apply();
    }

    /**
     * 进行数据加载
     * @param context
     */
    public static void load(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId = sharedPreferences.getString(KEY_PUSH_ID,"");
    }
}
