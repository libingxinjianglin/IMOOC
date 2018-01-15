package italker.tencent.com.common.weiget.recycler;

import android.content.Context;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import italker.tencent.com.common.R;

/**
 * TODO: document your custom view class.
 */
public class GalleyView extends RecyclerView {

    public Context context;
    private Adapter mAdapter;
    private List<Image> mSelectImage = new LinkedList<>();
    private static final int MAX_SISE = 3;
    private static final int MIN_IMAGE_SIZE = 10 * 1024;
    private SelectedChangeListener mListenter;
    private static final int LOADER_ID = 0x0100;
    private MyLoaderCallBack mLoaderCallback = new MyLoaderCallBack();


    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(),4));
        mAdapter = new Adapter();
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                if(OnItemSelectClick(image)){
                    holder.updateData(image);     //会调用刷新对应的recyclerAdapter里面的对应的数据发生改变
                }
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Image image) {

            }
        });
        setAdapter(mAdapter);
    }

    private boolean OnItemSelectClick(Image image){
        Boolean notifyRefresh ;
        if(mSelectImage.contains(image)){
            mSelectImage.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        }else{
            if(mSelectImage.size() >= MAX_SISE){  //选中的图片数量大于三
                notifyRefresh = false; //不刷新
                Toast.makeText(getContext(),"选中图片数量大于上限",Toast.LENGTH_LONG).show();
            }else{
                image.isSelect = true;
                mSelectImage.add(image);
                notifyRefresh = true;
            }
        }
        if(notifyRefresh){
            //接口回调让外面的方法刷新这个数据就是告诉外面我的数量进行了改变
            notifySelectChanged();
        }
        return true;
    }

    public String[] getPaths(){
        int i = 0;
        String paths[] = new String[mSelectImage.size()];
        for (Image image:mSelectImage) {
            String path = image.path;
            paths[i] = path;
            i++;
        }
        return paths;
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        // 得到监听者，并判断是否有监听者，然后进行回调数量变化
        SelectedChangeListener listener = mListenter;
        if (listener != null) {
            listener.onSelectedCountChanged(mSelectImage.size());
        }
    }

    public interface SelectedChangeListener {
        void onSelectedCountChanged(int size);
    }

    /**
     * 初始化方法
     *
     * @param loaderManager Loader管理器
     * @return 任何一个LOADER_ID，可用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener,Context context) {
        this.context = context;
        mListenter = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }
    /**
     * loader方法正是这个方法把值传递给了Adapterl里面的mList集合
     */
    public class MyLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {
        private String MAGE_PROJECTION[] = new String[]{
                MediaStore.Images.Media._ID, // Id
                MediaStore.Images.Media.DATA, // 图片路径
                MediaStore.Images.Media.DATE_ADDED // 图片的创建时间ø
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MAGE_PROJECTION,
                        null,
                    null,null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 当Loader加载完成时
            List<Image> images = new ArrayList<>();
            if (data != null) {
                if (data.getCount() > 0) {
                    data.moveToFirst();

                    // 得到对应的列的Index坐标
                    int indexId = data.getColumnIndexOrThrow(MAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(MAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(MAGE_PROJECTION[2]);


                    do {
                        // 循环读取，直到没有下一条数据
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_SIZE) {
                            // 如果没有图片，或者图片大小太小，则跳过
                            continue;
                        }


                        // 添加一条新的数据
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.data = dateTime;
                        images.add(image);


                    } while (data.moveToNext());
                }
            }
            updataListData(images);
        }

        private void updataListData(List<Image> images) {
            mAdapter.replace(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            updataListData(null);
        }
    }

    /**
     * 内部的数据结构
     */
    public static class Image{
        public String path;  //图片的路径
        public int id;       //图片的id
        public long data;   // 图片的日期
        public boolean isSelect;  //是否被选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    public class Adapter extends RecyclerAdapter<Image>{

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.sample_galley_view;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }

    }

    public  class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
        private ImageView mImageView;
        private CheckBox mCheckBox;
        private View mShade;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.select_image_view);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.select_check_view);
            mShade = (View)itemView.findViewById(R.id.select_view);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(context)
                    .load(image.path) // 加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用缓存，直接从原图加载
                    .centerCrop() // 居中剪切
                    .placeholder(R.color.grey_200) // 默认颜色
                    .into(mImageView);
            mShade.setVisibility(image.isSelect ? VISIBLE:INVISIBLE);
            mCheckBox.setVisibility(VISIBLE);
            mCheckBox.setChecked(image.isSelect);
        }
    }
}
