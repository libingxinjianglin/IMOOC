package italker.tencent.com.common.weiget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;

import de.hdodenhof.circleimageview.CircleImageView;
import italker.tencent.com.common.R;
import italker.tencent.com.common.factory.model.Autor;

/**
 *頭像控件
 */

public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager request,Autor autor){
        this.setup(request,autor.getPortrait());
    }

    public void setup(RequestManager request,String url){
        this.setup(request, R.drawable.default_portrait,url);
    }

    public void setup(RequestManager request,int resourceId,String url){
        if(url == null){
            url = "";
        }
        request.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()
                .into(this);
    }
}
