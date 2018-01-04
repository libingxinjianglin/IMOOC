package italker.tencent.com.italkerproject.activits;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;

import italker.tencent.com.common.app.Activity;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.account.AccountTrigger;
import italker.tencent.com.italkerproject.fragments.account.LoginFragment;
import italker.tencent.com.italkerproject.fragments.account.RegisterFragment;

/**
 * @author  hugh
 * @function 主要用于进行界面的切换，进行一个登入和注册的切换
 */

public class AccountActivity extends Activity implements AccountTrigger{

    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;
    private Fragment mCurrentFragment;

    private ImageView mBgm;

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

        mBgm = (ImageView)findViewById(R.id.im_bg);

        mLoginFragment = mCurrentFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurrentFragment)
                .commit();

        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop() //居中剪切
                .into(new ViewTarget<ImageView, GlideDrawable>(mBgm) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        // 拿到glide的Drawable
                        Drawable drawable = resource.getCurrent();
                        // 使用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                                PorterDuff.Mode.SCREEN); // 设置着色的效果和颜色，蒙板模式
                        // 设置给ImageView
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    @Override
    public void triggerView() {
        Fragment mFragment;
        if(mCurrentFragment == mLoginFragment){
            if(mRegisterFragment == null){
                //说明这个是第一次
                mRegisterFragment = new RegisterFragment();
            }
            mFragment = mRegisterFragment;
        }else{
            mFragment = mLoginFragment;
        }
        mCurrentFragment = mFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_container,mCurrentFragment).commit();
    }
}
