package italker.tencent.com.italkerproject.activits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import italker.tencent.com.common.app.Activity;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.account.UpdateFragments;

public class AccountActivity extends Activity {
    private UpdateFragments updata ;
    public static void show(Context context){
        Intent mIntent = new Intent(context,AccountActivity.class);
        context.startActivity(mIntent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWeiget() {
        super.initWeiget();
        updata = new UpdateFragments();
        getSupportFragmentManager().beginTransaction().add(R.id.galley_containt,updata).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      updata.sv(requestCode,resultCode,data);
    }
}
