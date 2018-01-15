package italker.tencent.com.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import italker.tencent.com.common.R;


/**
 * Created by Administrator on 2018/1/13 0013.
 */

public abstract class ToorbarActivity extends Activity {
    protected Toolbar mToolbar;

    @Override
    protected void initWeiget() {
        super.initWeiget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    /**
     * 初始化toolbar
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    protected void initTitleNeedBack() {
        // 设置左上角的返回按钮为实际的返回效果
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
