package italker.tencent.com.common.weiget.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class QuareLayout extends FrameLayout {
    public QuareLayout(Context context) {
        super(context);
    }

    public QuareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
