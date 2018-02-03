package drawable.tencent.com.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import drawable.tencent.com.factory.model.db.AppDatabases;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

@QueryModel(database = AppDatabases.class)
public class MemberUserModel {
    @Column
    public String userId; // User-id/Member-userId
    @Column
    public String name; // User-name
    @Column
    public String alias; // Member-alias
    @Column
    public String portrait; // User-portrait
}
