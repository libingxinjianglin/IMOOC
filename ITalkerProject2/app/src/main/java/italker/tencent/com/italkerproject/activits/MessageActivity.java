package italker.tencent.com.italkerproject.activits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.app.Activity;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.app.ToorbarActivity;
import italker.tencent.com.common.factory.model.Autor;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.message.ChatGroupFragment;
import italker.tencent.com.italkerproject.fragments.message.ChatUserFragment;

public class MessageActivity extends Activity {
    public static final String KEY_RECEIVER_ID= "KEY_RECEIVER_ID";
    private static final String KEY_RECEIVER_GROUP_ID= "KEY_RECEIVER_GROUP_ID";
    private String mReceiverId;
    private Boolean isGroup ;
    /**
     * 发起人聊天
     * @param context
     * @param user
     */
    public static void show(Context context, Autor user){
        if(user == null || context == null){
            return ;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,user.getId());
        intent.putExtra(KEY_RECEIVER_GROUP_ID,false);
        context.startActivity(intent);
    }

    /**
     * 发起群聊天
     * @param context
     * @param user
     */
    public static void show(Context context, Group user){
        if(user == null || context == null){
            return ;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,user.getId());
        intent.putExtra(KEY_RECEIVER_GROUP_ID,true);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean isArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        isGroup = bundle.getBoolean(KEY_RECEIVER_GROUP_ID);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWeiget() {
        super.initWeiget();
        setTitle("");
        Fragment fragment;
        if(isGroup){
            fragment = new ChatGroupFragment();
        }else{
            fragment = new ChatUserFragment();
        }
        Bundle bundle = new Bundle();
        //把联系人的id传递进来
        bundle.putString(KEY_RECEIVER_ID,mReceiverId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }
}
