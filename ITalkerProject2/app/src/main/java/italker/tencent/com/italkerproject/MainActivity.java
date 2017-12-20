package italker.tencent.com.italkerproject;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import italker.tencent.com.common.Comon;
import italker.tencent.com.common.app.Activity;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.weiget.PortraitView;

public class MainActivity extends Activity implements View.OnClickListener{

    private View mAppBar;

    private FrameLayout mContent;

    private FloatActionButton mFloatButton;

    private TextView mLabelTitle;

    private ImageView mImageSearch;

    private PortraitView mImagePorait;

    private BottomNavigationView mNavigation;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initWeiget() {
        super.initWeiget();

        mAppBar = (AppBarLayout) findViewById(R.id.activity_appbar);
        mContent = (FrameLayout)findViewById(R.id.activity_content_view);

        mFloatButton = (FloatActionButton)findViewById(R.id.activity_float_button);
        mFloatButton.setOnClickListener(this);

        mNavigation = (BottomNavigationView)findViewById(R.id.activity_navigation_view);
        mLabelTitle = (TextView)findViewById(R.id.txt_title);
        mImageSearch = (ImageView)findViewById(R.id.ima_search);
        mImagePorait = (PortraitView) findViewById(R.id.ima_protrait);

        Glide.with(this).load(R.drawable.bg_src_morning).into(new ViewTarget<View,GlideDrawable>(mAppBar) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.activity_float_button ){

        }
    }
}
