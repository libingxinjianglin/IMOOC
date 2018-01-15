package italker.tencent.com.italkerproject.activits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.app.Activity;
import italker.tencent.com.italkerproject.R;

public class MessageActivity extends Activity {

    public static void show(Context context, User user){
        context.startActivity(new Intent(context,MessageActivity.class));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }
}
