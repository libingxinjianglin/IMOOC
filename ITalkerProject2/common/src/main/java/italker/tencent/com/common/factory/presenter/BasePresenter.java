package italker.tencent.com.common.factory.presenter;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

/**
 * 使用泛型的时候或者mvp的时候关键在于
 * 1、接口的关系设置后，m-v-p之间的关系
 * 2、让两个接口之间都存在一个具体的对方实现类去进行操作，通过泛型就能够很好的进行一个实现
 * @param <T>
 */
public class BasePresenter<T extends BaseControl.View> implements BaseControl.Presenter{

    private T mView = null;

    public BasePresenter(T view){
        setView(view);
    }
    public void setView(T view){
        this.mView = view;
        this.mView.setPresenter(this);
    }
    public final T getView(){
        return mView;
    }

    @Override
    public void statr() {
        T view = mView;
        if(view!=null){
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
       T view = mView;
        this.mView = null;
        if(view != null){
           this. mView.setPresenter(null);  // View对应的一个presenter为null
        }
    }
}
