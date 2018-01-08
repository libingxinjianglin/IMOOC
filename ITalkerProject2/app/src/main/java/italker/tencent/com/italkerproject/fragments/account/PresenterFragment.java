package italker.tencent.com.italkerproject.fragments.account;

import android.content.Context;
import android.widget.Toast;

import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

/**\
 * 一个RegistenerControl的一个抽象类，实现公共部分的一个方法。在公共部分我们有一个setPresenter所以我们需要一个presenter的泛型
 * @param <T>
 */
public abstract class PresenterFragment<T extends BaseControl.Presenter > extends Fragment implements BaseControl.View<T> {
    protected T mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = initPresenter();
    }

    /**
     * 初始化一个presenter，返还一个泛型的presenter
     * @return
     */
    protected abstract T initPresenter();

    @Override
    public void showError(int str) {
        Toast.makeText(getContext(), getString(str), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }
}
