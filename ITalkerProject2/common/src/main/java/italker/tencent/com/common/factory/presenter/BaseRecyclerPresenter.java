package italker.tencent.com.common.factory.presenter;

import android.support.v7.util.DiffUtil;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;

/**
 * 对Recycler进行一个简单的封装
 * Created by Administrator on 2018/1/20 0020.
 */

public class BaseRecyclerPresenter<ViewModel,View extends BaseControl.RecyclerView> extends BasePresenter<View> {

    public BaseRecyclerPresenter(View view) {
        super(view);
    }
    protected void refreshData(final List<ViewModel> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if(view == null){
                    return ;
                }
                //更新数据并且刷新界面
                RecyclerAdapter adapter = view.getRecycler();
                adapter.replace(dataList);
                view.onAdapterChanage();
            }
        });
    }

    /**
     * 刷新一堆新的数据到界面里面去
     * @param result
     * @param dataList
     */
    protected void refreshData(final DiffUtil.DiffResult result, final List<ViewModel> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //这个是在一个主线程里面实现的，
                refreshDataOnUiThread(result,dataList);
            }
        });
    }
    public void refreshDataOnUiThread(DiffUtil.DiffResult result, final List<ViewModel> dataList){
        View view = getView();
        if(view == null){
            return ;
        }
        RecyclerAdapter adapter = view.getRecycler();
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
       //通知界面刷新占位布局
        //进行更量更新
        result.dispatchUpdatesTo(adapter);
        view.onAdapterChanage();
    }
}
