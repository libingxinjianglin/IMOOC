package italker.tencent.com.italkerproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import drawable.tencent.com.factory.persistence.Account;
import italker.tencent.com.common.app.Activity;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.italkerproject.activits.AccountActivity;
import italker.tencent.com.italkerproject.activits.SearchActivity;
import italker.tencent.com.italkerproject.activits.UserActivity;
import italker.tencent.com.italkerproject.fragments.main.ActionFragment;
import italker.tencent.com.italkerproject.fragments.main.ContactFragment;
import italker.tencent.com.italkerproject.fragments.main.GroupFragment;
import italker.tencent.com.italkerproject.navhelper.NavHelper;

public class MainActivity extends Activity
        implements View.OnClickListener,BottomNavigationView.OnNavigationItemSelectedListener,NavHelper.OnTabChangedListener<Integer>{

    private View mAppBar;

    private FrameLayout mContent;

    private FloatActionButton mFloatButton;

    private TextView mLabelTitle;

    private ImageView mImageSearch;

    private PortraitView mImagePorait;

    private BottomNavigationView mNavigation;

    private NavHelper<Integer> mHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected boolean isArgs(Bundle bundle) {
        if(Account.isComplete()){
            return true;
        }else{
            UserActivity.show(this);
            return false;
        }
    }


    @Override
    protected void initWeiget() {
        super.initWeiget();

        mHelper = new NavHelper<Integer>(this,R.id.activity_content_view,getSupportFragmentManager(),this);
        mHelper.add(R.id.action_home,new NavHelper.Tab<Integer>(ActionFragment.class,R.string.action_home)).
                add(R.id.action_group,new NavHelper.Tab<Integer>(GroupFragment.class,R.string.action_group)).
                add(R.id.action_contact,new NavHelper.Tab<Integer>(ContactFragment.class,R.string.action_contact));

        mAppBar = (AppBarLayout) findViewById(R.id.activity_appbar);
        mContent = (FrameLayout)findViewById(R.id.activity_content_view);

        mFloatButton = (FloatActionButton)findViewById(R.id.activity_float_button);
        mFloatButton.setOnClickListener(this);

        mNavigation = (BottomNavigationView)findViewById(R.id.activity_navigation_view);
        mNavigation.setOnNavigationItemSelectedListener(this);

        mLabelTitle = (TextView)findViewById(R.id.txt_title);
        mImageSearch = (ImageView)findViewById(R.id.ima_search);
        mImageSearch.setOnClickListener(this);
        mImagePorait = (PortraitView) findViewById(R.id.ima_protrait);
        mImagePorait.setup(Glide.with(this),Account.getUser());
        Glide.with(this).load(R.drawable.bg_src_morning).into(new ViewTarget<View,GlideDrawable>(mAppBar) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home,0);   //这个方法就是会调用onNavigationItemSelected
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.activity_float_button ){
            if(mHelper.getCurrentTab().extra.equals(R.string.action_group)){
                //TODO 進入到群裡面去
            }else{
                //進入到搜索聯繫人界面
              SearchActivity.show(this,SearchActivity.TYPE_USER);
            }
        }else if(view.getId() == R.id.ima_search){
            /**
             *
             */
           int type = mHelper.getCurrentTab().extra.equals(R.string.action_group)?SearchActivity.TYPE_GROUP:SearchActivity.TYPE_USER;
          SearchActivity.show(this,type);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mHelper.performClickMenu(item.getItemId());
    }

    /**
     * 处理后回调的方法
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab newTab, NavHelper.Tab oldTab) {
        mLabelTitle.setText((Integer)newTab.extra);

        int TranslationY = 0;
        int Rotation = 0;

        if(newTab.extra.equals(R.string.action_home)){
            TranslationY = (int) Ui.dipToPx(getResources(),76);
        }else{
            if(newTab.extra.equals(R.string.action_group)){
                Rotation = -360;
                mFloatButton.setImageResource(R.drawable.ic_group_add);
            }else{
                mFloatButton.setImageResource(R.drawable.ic_contact_add);
                Rotation = 360;
            }
        }
        mFloatButton.animate().translationY(TranslationY).rotation(Rotation).
                setInterpolator(new AnticipateOvershootInterpolator()).
                setDuration(480)
                .start();
    }

    public static void show(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
    }
}
