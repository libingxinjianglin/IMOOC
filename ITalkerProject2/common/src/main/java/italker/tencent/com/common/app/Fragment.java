package italker.tencent.com.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

import italker.tencent.com.common.weiget.convention.PlaceHolderView;

/**
 * Created by Administrator on 2017/11/12 0012.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    private View mRoot;
    protected PlaceHolderView mplaceHolder;
    private Boolean isFirst = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot == null) {
            int id = getLayoutId();
            View mView = inflater.inflate(id, container, false);
            initWidget(mView);
            mRoot = mView;
        }else{
            if(mRoot.getParent()!= null){
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isFirst) {
            initFirstData();
            isFirst = false;
        }
        initData();
    }

    public void initFirstData() {

    }

    /**
     * 让子类实现id
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    protected void initWidget(View view){

    }
    protected boolean onBack(){
        return false;
    }

    public  void setPlaceHolderView(PlaceHolderView view){
        this.mplaceHolder = view;
    }
}
