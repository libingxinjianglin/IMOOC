package drawable.tencent.com.factory.presenter;

import java.util.List;

import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.data.DbDataSource;
import italker.tencent.com.common.factory.presenter.BaseControl;
import italker.tencent.com.common.factory.presenter.BaseRecyclerPresenter;

/**
 * 抽象观察者设计模式里面的一个presenter
 * Created by Administrator on 2018/1/22 0022.
 */

public abstract class BaseSourcePresenter<Data, ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseControl.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void statr() {
        super.statr();
        if (mSource != null)
            mSource.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
