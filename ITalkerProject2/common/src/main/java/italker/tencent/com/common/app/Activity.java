package italker.tencent.com.common.app;

import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import italker.tencent.com.common.R;

/**
 * Created by Administrator on 2017/11/12 0012.
 */

public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isArgs(getIntent().getExtras())){
            int layoutId = getLayoutId();
            setContentView(layoutId);
            initWeiget();
            initData();
        }else{
            finish();
        }
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
}
