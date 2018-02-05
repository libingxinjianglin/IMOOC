package italker.tencent.com.italkerproject.fragments.panel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import italker.tencent.com.common.face.Face;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {
    private ImageView mFace;
    public FaceHolder(View itemView) {
        super(itemView);
        mFace = (ImageView)itemView.findViewById(R.id.im_face);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null
                // drawable 资源 id
                && ((bean.preview instanceof Integer)
                // face zip 包资源路径
                || bean.preview instanceof String))
            Glide.with(itemView.getContext())
                    .load(bean.preview)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888) //设置解码格式8888，保证清晰度
                    .into(mFace);
    }
}
