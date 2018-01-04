package italker.tencent.com.italkerproject.activits;

import android.content.Intent;

import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.user.UpdateFragments;

public class UserActivity extends italker.tencent.com.common.app.Activity {
    private UpdateFragments updata ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

//    @Override
//    protected void initWeiget() {
//        super.initWeiget();
//        updata = new UpdateFragments();
//        getSupportFragmentManager().beginTransaction().add(R.id.galley_containt,updata).commit();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updata.sv(requestCode,resultCode,data);
    }
}
