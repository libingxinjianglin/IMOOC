package drawable.tencent.com.factory.model.db;

import android.support.v7.util.DiffUtil;

import com.raizlabs.android.dbflow.structure.BaseModel;

import drawable.tencent.com.factory.utils.DiffUiDataCallback;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model> {
}
