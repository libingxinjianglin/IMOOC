package drawable.tencent.com.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import drawable.tencent.com.factory.model.db.Session;
import drawable.tencent.com.factory.model.db.Session_Table;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.User_Table;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class SessionHelper {

    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
