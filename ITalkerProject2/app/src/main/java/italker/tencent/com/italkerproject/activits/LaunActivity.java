package italker.tencent.com.italkerproject.activits;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import drawable.tencent.com.factory.persistence.Account;
import italker.tencent.com.common.app.Activity;
import italker.tencent.com.italkerproject.MainActivity;

import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.assist.PermissionFragment;

public class LaunActivity extends Activity {

    private ColorDrawable colorDrawable;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initWeiget() {
        super.initWeiget();
        // 拿到跟布局
        View root = findViewById(R.id.activity_account);
        // 获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        // 创建一个Drawable
        ColorDrawable drawable = new ColorDrawable(color);

        root.setBackground(drawable);
        colorDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }

    private void waitPushReceiverId() {
        if(Account.isLogin()){
            //已经登入判断是否绑定
            //如果没有的话就等待广播绑定
            if(Account.isBind()){
                sckin();
                return ;
            }
        }else{
            //没有登入的话不能绑定pushID
            if(!TextUtils.isEmpty(Account.getPushId())) {
                sckin();
                return ;
            }
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        },500);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_laun;
    }

    private void sckin(){
        startAnim(1, new Runnable() {
            @Override
            public void run() {
                realsckin();
            }
        });
    }

    private void realsckin() {
        if(PermissionFragment.haveAll(this,getSupportFragmentManager())) {
            if(Account.isLogin()){
                MainActivity.show(this);
                finish();
            }else{
                AccountActivity.show(this);
                finish();
            }

        }
    }

    /**
     * 刚刚进去界面的时候我们出现OnResume方法的调用，然后接下来出现这个fragment
     * 当这个fragment被finish之后我们再次调用这个方法
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void startAnim(float endProgress, final Runnable endCallback) {
        // 获取一个最终的颜色
        int finalColor = Resource.Color.WHITE; // UiCompat.getColor(getResources(), R.color.white);
        // 运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress, colorDrawable.getColor(), finalColor);
        // 构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        valueAnimator.setDuration(1500); // 时间
        valueAnimator.setIntValues(colorDrawable.getColor(), endColor); // 开始结束值
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }


    private final Property<LaunActivity, Object> property = new Property<LaunActivity, Object>(Object.class, "color") {
        @Override
        public void set(LaunActivity object, Object value) {
            object.colorDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunActivity object) {
            return object.colorDrawable.getColor();
        }
    };

}
