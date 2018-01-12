package italker.tencent.com.italkerproject.fragments.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;

import java.io.File;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.net.UpLoadHelper;
import drawable.tencent.com.factory.presenter.RegisterControl.UpdateInfoContract;
import drawable.tencent.com.factory.presenter.RegisterControl.UpdatePresenter;
import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.app.MyApplication;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.GalleyView;
import italker.tencent.com.italkerproject.MainActivity;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.account.PresenterFragment;
import italker.tencent.com.italkerproject.fragments.media.GalleyFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * 更新头像
 */
public class UpdateFragments extends PresenterFragment<UpdateInfoContract.presenter> implements UpdateInfoContract.view, View.OnClickListener{

    private PortraitView mPortrain;
    private Button mButtonSubmit;
    private EditText mEeditDes;
    private ImageView mImageSex;
    private String mPortrainPath;
    private boolean isMan = true;

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

        mButtonSubmit = view.findViewById(R.id.btn_submit);
        mButtonSubmit.setOnClickListener(this);

        mEeditDes = view.findViewById(R.id.edit_des_view);
        mImageSex = view.findViewById(R.id.ima_sex_view);
        mImageSex.setOnClickListener(this);
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
        }else if(view.getId() == R.id.btn_submit){
            String mDesc = mEeditDes.getText().toString();
            mPresenter.update(mPortrainPath,mDesc,isMan);
        }else if(view.getId() == R.id.ima_sex_view){
            // 性别图片点击的时候触发
            isMan = !isMan; // 反向性别

            Drawable drawable = getResources().getDrawable(isMan ?
                    R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
            mImageSex.setImageDrawable(drawable);
            // 设置背景的层级，切换颜色
            mImageSex.getBackground().setLevel(isMan ? 0 : 1);
        }
    }
    public void sv(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri mData= UCrop.getOutput(data);
            mPortrainPath = mData.getPath();
            Log.e("TAG", "url:" + mData);
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(getContext()).load(mData).asBitmap().centerCrop().into(mPortrain);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    protected UpdateInfoContract.presenter initPresenter() {
        return new UpdatePresenter(this);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mImageSex.setEnabled(false);
        mButtonSubmit.setEnabled(false);
        mPortrain.setEnabled(false);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        mImageSex.setEnabled(true);
        mButtonSubmit.setEnabled(true);
        mPortrain.setEnabled(true);
    }
}
