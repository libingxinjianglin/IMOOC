package drawable.tencent.com.factory.utils;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * Gson过滤器，数据库的字段不要过滤
 */
public class DBFlowExclusion implements ExclusionStrategy{
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
