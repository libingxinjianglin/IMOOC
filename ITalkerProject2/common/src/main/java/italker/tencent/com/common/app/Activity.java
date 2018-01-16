package italker.tencent.com.common.app;

import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import italker.tencent.com.common.R;
import italker.tencent.com.common.weiget.convention.PlaceHolderView;

/**
 * Created by Administrator on 2017/11/12 0012.
 */

public abstract class Activity extends AppCompatActivity {

    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isArgs(getIntent().getExtras())){
            int layoutId = getLayoutId();
            initBefore();
            setContentView(layoutId);
            initWeiget();
            initData();
        }else{
            finish();
        }
    }

    protected void initBefore() {

    }

    protected void initWeiget(){

    }
    protected void initData(){

    }
    protected abstract int getLayoutId();

    /**
     * 判断是否有数据
     * @param bundle
     * @return
     */
    protected boolean isArgs(Bundle bundle){
        return true;
    }

    @Override
    public void onBackPressed() {
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if(fragments.size()>0 && fragments != null){
            for (Fragment mFragment:
                 fragments) {
                if(mFragment instanceof italker.tencent.com.common.app.Fragment){
                    if(((italker.tencent.com.common.app.Fragment) mFragment).onBack()){
                        return ;
                    }
                }
            }
        }
        super.onBackPressed();
    }
    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }
}
