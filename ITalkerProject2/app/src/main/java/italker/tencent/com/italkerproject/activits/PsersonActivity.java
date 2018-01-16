package italker.tencent.com.italkerproject.activits;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.widget.Button;

import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.contact.PersonControl;
import drawable.tencent.com.factory.presenter.contact.PersonPresenter;
import italker.tencent.com.common.app.Activity;
import italker.tencent.com.common.app.ToorbarActivity;
import italker.tencent.com.common.factory.presenter.BaseControl;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

/**
 * 个人信息界面
 */

public class PsersonActivity extends PresenterToolbarActivity<PersonControl.Presenter> implements PersonControl.View,View.OnClickListener{
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    private MenuItem mFollow;
    private PortraitView mPortrainView;
    private TextView mFollows;
    private TextView mFollowing;

    private TextView mUserDes;
    private TextView mUserNmae;
    private Button mSubmit;

    private Boolean isFollow = false;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PsersonActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void initWeiget() {
        super.initWeiget();
        mPortrainView = (PortraitView) findViewById(R.id.portrait_view);
        mPortrainView.setOnClickListener(this);
        mUserNmae = (TextView)findViewById(R.id.textView);
        mFollows = (TextView)findViewById(R.id.txt_follows);
        mFollowing = (TextView)findViewById(R.id.txt_following);
        mUserDes = (TextView)findViewById(R.id.txt_dex_view);
        mSubmit = (Button)findViewById(R.id.btn_talk);
        mSubmit.setOnClickListener(this);
        setTitle("");    //设置actionBar的title为空
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.statr();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal,menu);
        mFollow = menu.findItem(R.id.action_follow);
        chanageStatueColor();
        return true;
    }

    /**
     * TODO 渲染技术
     */
    private void chanageStatueColor() {
        if(mFollow != null){
            Drawable drawable = isFollow?getResources().getDrawable(R.drawable.ic_favorite):
                    getResources().getDrawable(R.drawable.ic_favorite_border);
            Drawable wrap = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrap,getResources().getColor(R.color.white));
            mFollow.setIcon(wrap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            Toast.makeText(this,"已关注该联系人~~",Toast.LENGTH_SHORT).show();
            return true;
        }else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    public void onClick(View view) {
        if(view .getId() == R.id.btn_talk){

        }

    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void onLoadDone(User user) {
        if (user == null)
            return;
        mPortrainView.setup(Glide.with(this), user);
        mUserNmae.setText(user.getName());
        mUserDes.setText(user.getDesc());
        int follows = user.getFollows();
       // mFollows.setText(String.format(getString(R.string.label_follows),follows));
       // mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        hideLoading();
    }

    @Override
    public void allowSayHello(boolean isAllow) {
        mSubmit.setVisibility(isAllow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        this.isFollow = isFollow;
        chanageStatueColor();
    }


    @Override
    protected PersonControl.Presenter initPresenter() {
        return new PersonPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
