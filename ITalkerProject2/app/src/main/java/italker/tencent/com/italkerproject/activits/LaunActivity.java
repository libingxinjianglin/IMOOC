package italker.tencent.com.italkerproject.activits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import italker.tencent.com.italkerproject.MainActivity;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.assist.PermissionFragment;

public class LaunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laun);
    }

    /**
     * 刚刚进去界面的时候我们出现OnResume方法的调用，然后接下来出现这个fragment
     * 当这个fragment被finish之后我们再次调用这个方法
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionFragment.haveAll(this,getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}
