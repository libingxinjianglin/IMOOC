package italker.tencent.com.common.factory.presenter;

/**
 * Created by Administrator on 2018/1/8 0008.
 */


import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;

/**
 * 该类是一个基类进行对mvp公用部分进行一个封装
 */
public interface BaseControl {
    interface View<T extends Presenter>{
        void showError(int str);

        void showLoading();  //显示公共的进度条

        //支持设置一个任意类型的presenter
        void setPresenter(T presenter);
    }

    interface Presenter{
        void statr();
        void destroy();

    }

    interface RecyclerView<T extends Presenter,ViewModl> extends View<T>{
        //每次進來特別麻煩都要重新刷新一下
        //void onDone(List<User> mList);

        RecyclerAdapter<ViewModl> getRecycler();
        //當數據更改的時候刷新
        void onAdapterChanage();
    }
}
