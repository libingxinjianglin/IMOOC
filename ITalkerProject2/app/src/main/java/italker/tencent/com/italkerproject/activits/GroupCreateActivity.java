package italker.tencent.com.italkerproject.activits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.EditText;

import java.io.File;

import drawable.tencent.com.factory.presenter.group.GroupCreateControl;
import drawable.tencent.com.factory.presenter.group.GroupCreatePresenter;
import italker.tencent.com.common.app.MyApplication;
import italker.tencent.com.common.app.ToorbarActivity;
import italker.tencent.com.common.factory.presenter.BaseControl;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.media.GalleyFragment;

public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateControl.Presenter> implements GroupCreateControl.View{

    private RecyclerView mRecycler;
    private EditText mName;
    private EditText mDesc;
    private PortraitView mPortrait;
    private String mPortraitPath;
    private Adapter mAdapter;

    public static void show(Context context){
        context.startActivity(new Intent(context,GroupCreateActivity.class));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initWeiget() {
        super.initWeiget();
        setTitle("");
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());
        mName = (EditText)findViewById(R.id.edit_name);
        mDesc = (EditText)findViewById(R.id.edit_desc);
        mPortrait = (PortraitView)findViewById(R.id.im_portrait);
        mPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftBord();
                if(view.getId() == R.id.im_portrait){
                    new GalleyFragment().setSelectedListener(new GalleyFragment.OnSelectedlistener() {
                        @Override
                        public void onSelectImage(String path) {
                            UCrop.Options option = new UCrop.Options();
                            option.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                            option.setCompressionQuality(96);
                            File mFile = MyApplication.getProparain();
                            UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(mFile)).withAspectRatio(1,1)
                                    .withMaxResultSize(520,520)
                                    .withOptions(option).start(GroupCreateActivity.this);
                        }
                    }).show(getSupportFragmentManager(),GalleyFragment.class.getName());//建议使用ChildFragment
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_create,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_create){
            //进行创建
            onClick();
            return true;
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.statr();
    }

    private void onClick() {
        hideSoftBord();
        String name = mName.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();
        mPresenter.create(name,desc,mPortraitPath);
    }

    @Override
    public void onCreateSucceed() {
        // 不管你怎么样，我先隐藏我
        hideDialogLoading();
        Toast.makeText(this,"创建成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public RecyclerAdapter<GroupCreateControl.ViewModel> getRecycler() {
        return mAdapter;
    }

    @Override
    public void onAdapterChanage() {
        // 不管你怎么样，我先隐藏我
        hideDialogLoading();
    }

    @Override
    protected GroupCreateControl.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    public class Adapter extends RecyclerAdapter<GroupCreateControl.ViewModel>{

        @Override
        protected int getItemViewType(int position, GroupCreateControl.ViewModel viewModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateControl.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupCreateActivity.ViewHolder(root);
        }
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateControl.ViewModel>{
        private PortraitView mPortrait;
        private TextView mName;
        private CheckBox mSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            mPortrait = itemView.findViewById(R.id.im_portrait);
            mName = itemView.findViewById(R.id.txt_name);
            mSelect = itemView.findViewById(R.id.cb_select);
            mSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mPresenter.changeSelect(mData,b);
                }
            });
        }

        @Override
        protected void onBind(GroupCreateControl.ViewModel viewModel) {
           mPortrait.setup(Glide.with(GroupCreateActivity.this),viewModel.author);
            mName.setText(viewModel.author.getName());
            mSelect.setChecked(viewModel.isSelected);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri mData= UCrop.getOutput(data);
            mPortraitPath = mData.getPath();
            Log.e("TAG", "url:" + mData);
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(this).load(mData).asBitmap().centerCrop().into(mPortrait);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void hideSoftBord(){
        //得到当前的焦点
        View view = getCurrentFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}
