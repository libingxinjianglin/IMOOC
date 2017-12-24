package italker.tencent.com.italkerproject.fragments.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.app.MyApplication;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.GalleyView;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.media.GalleyFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragments extends Fragment implements View.OnClickListener{

    private PortraitView mPortrain;
    public UpdateFragments() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_update_fragments;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mPortrain = view.findViewById(R.id.ima_protrait);
        mPortrain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ima_protrait){
            new GalleyFragment().setSelectedListener(new GalleyFragment.OnSelectedlistener() {
                @Override
                public void onSelectImage(String path) {
                    UCrop.Options option = new UCrop.Options();
                    option.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                    option.setCompressionQuality(96);
                    File mFile = MyApplication.getProparain();
                    UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(mFile)).withAspectRatio(1,1)
                    .withMaxResultSize(520,520)
                            .withOptions(option).start(getActivity());
                }
            }).show(getChildFragmentManager(),GalleyFragment.class.getName());//建议使用ChildFragment
        }
    }
    public void sv(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri mData= UCrop.getOutput(data);
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(getContext()).load(mData).asBitmap().centerCrop().into(mPortrain);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}
