package italker.tencent.com.italkerproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import italker.tencent.com.common.Comon;
import italker.tencent.com.common.app.Activity;

public class MainActivity extends Activity {
    private TextView mLabelTest;
    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }
    protected void initWeiget(){
        mLabelTest = (TextView)super.findViewById(R.id.txt_test);
    }
    protected void initData(){
        mLabelTest.setText("那我不爱你了");
    }
}
