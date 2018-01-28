package drawable.tencent.com.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import java.util.List;

import drawable.tencent.com.factory.data.message.SessionDataSource;
import drawable.tencent.com.factory.data.message.SessionRepository;
import drawable.tencent.com.factory.model.db.Session;
import drawable.tencent.com.factory.presenter.BaseSourcePresenter;
import drawable.tencent.com.factory.utils.DiffUiDataCallback;

/**
 * Created by Administrator on 2018/1/28 0028.
 */

public class SessionPresenter extends BaseSourcePresenter<Session,Session,SessionDataSource,SessionControl.View> implements SessionControl.Presenter {

    public SessionPresenter(SessionControl.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionControl.View view = getView();
        if (view == null)
            return;

        // 差异对比
        List<Session> old = view.getRecycler().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 刷新界面
        refreshData(result, sessions);
    }
}
