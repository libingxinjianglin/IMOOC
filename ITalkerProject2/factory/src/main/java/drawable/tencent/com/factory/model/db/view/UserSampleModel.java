package drawable.tencent.com.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import drawable.tencent.com.factory.model.db.AppDatabases;
import italker.tencent.com.common.factory.model.Autor;

/**
 * Created by Administrator on 2018/2/2 0002.
 */
@QueryModel(database = AppDatabases.class)
public class UserSampleModel implements Autor {
    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String portrait;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPortrait() {
        return portrait;
    }

    @Override
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
