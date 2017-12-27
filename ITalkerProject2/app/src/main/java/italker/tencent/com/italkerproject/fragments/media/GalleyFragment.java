package italker.tencent.com.italkerproject.fragments.media;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import italker.tencent.com.common.weiget.recycler.GalleyView;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleyFragment extends BottomSheetDialogFragment implements GalleyView.SelectedChangeListener{

    private GalleyView mGalley;
    private OnSelectedlistener mListener;

    public GalleyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_galley, container, false);
        mGalley = (GalleyView)mView.findViewById(R.id.protrait_select_galleyview);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalley.setup(getLoaderManager(),this,getContext());   //初始化画廊的数据
    }

    @Override
    public void onSelectedCountChanged(int size) {
        if(size > 0){
            //隐藏自己
            dismiss();
            //告诉外面我已经选好了一张图片了
            if(mListener != null){
                String paths[] = mGalley.getPaths();
                mListener.onSelectImage(paths[0]);
            }
            mListener = null;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //使用默认的dialog
        return new BottomSheetDialog(getContext());
    }

    public GalleyFragment setSelectedListener(OnSelectedlistener listener){
        mListener = listener;
        return this;
    }

    public interface OnSelectedlistener{
        void onSelectImage(String path);
    }
}
