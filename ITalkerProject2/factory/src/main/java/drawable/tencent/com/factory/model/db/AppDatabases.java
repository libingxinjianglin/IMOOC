package drawable.tencent.com.factory.model.db;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 创建一个数据库
 */
@Database(name =AppDatabases.name,version = AppDatabases.Version)
public class AppDatabases {
    public static final String name = "AppDataBases";
    public static final int Version = 1;
}
