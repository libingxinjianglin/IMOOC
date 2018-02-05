package italker.tencent.com.italkerproject.fragments.panel;

import android.view.View;

import java.util.List;

import italker.tencent.com.common.face.Face;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class FaceAdapter extends RecyclerAdapter<Face.Bean>{

    public FaceAdapter(List<Face.Bean> faces, AdapterListener<Face.Bean> adapterListener) {
        super(faces, adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
