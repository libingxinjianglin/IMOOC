package italker.tencent.com.italkerproject.fragments.panel;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import net.qiujuer.genius.ui.Ui;

import java.util.List;

import italker.tencent.com.common.face.Face;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaceFragment extends italker.tencent.com.common.app.Fragment {
    private PanelCallback mCallback;


    public FaceFragment() {
        // Required empty public constructor
    }

    // 开始初始化方法
    public void setup(PanelCallback callback) {
        mCallback = callback;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_face;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        initFace(view);
        initGallery(view);
        initRecord(view);
    }

    public void initFace(View view){
        final View facePanel = view.findViewById(R.id.lay_pancel);

        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCallback mDeleteCallBack = mCallback;
                //模拟一个键盘点击
                KeyEvent mEvent = new KeyEvent(0,0,0,KeyEvent.KEYCODE_DEL,0,0,0,0,KeyEvent.KEYCODE_ENDCALL);
                mDeleteCallBack.getInputEditText().dispatchKeyEvent(mEvent);
            }
        });

        TabLayout tabLayout = (TabLayout) facePanel.findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        float minFaceSize = Ui.dipToPx(getResources(), 48);
        WindowManager windowManager = getActivity().getWindowManager();
        float mTotal = windowManager.getDefaultDisplay().getWidth();
        final int countClus = (int) (mTotal / minFaceSize);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater Inflater = LayoutInflater.from(getContext());
                RecyclerView mRecyclerView = (RecyclerView) Inflater.inflate(R.layout.lay_face_contact,container,false);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),countClus));
                //得到一个表情盘里面所有的表情
                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter faceAdapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListener<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        //发生点击事件的时候进行一个触发
                        if (mCallback == null)
                            return;
                        // 表情添加到输入框
                        EditText editText = mCallback.getInputEditText();
                        Face.inputFace(getContext(), editText.getText(), bean, (int)
                                (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));

                    }

                    @Override
                    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {

                    }
                });

                //具体的表情界面加载
                mRecyclerView.setAdapter(faceAdapter);
                container.addView(mRecyclerView);
                return mRecyclerView;
            }

            /**
             * 想要和TabLayout关联的话必须要进行一个复写
             * @param position
             * @return
             */
            @Override
            public CharSequence getPageTitle(int position) {
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    public void initRecord(View view){

    }

    public void initGallery(View view){

    }


    public void showFace(){

    }

    public void showRecord(){

    }

    public void showGallery(){

    }

    // 回调聊天界面的Callback
    public interface PanelCallback {
        EditText getInputEditText();
    }

}
