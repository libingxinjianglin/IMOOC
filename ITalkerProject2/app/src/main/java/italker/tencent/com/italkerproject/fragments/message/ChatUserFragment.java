package italker.tencent.com.italkerproject.fragments.message;


import android.os.Bundle;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.presenter.message.ChatControl;
import drawable.tencent.com.factory.presenter.message.ChatUserPresenter;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.italkerproject.R;


public class ChatUserFragment extends ChatFragment<User> implements ChatControl.UserView {

    public PortraitView mPortraitView;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mPortraitView = view.findViewById(R.id.im_portrait);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //表示你的垂直距离已经是0了不能再次下滑了
        if(verticalOffset == 0){
            mPortraitView.setVisibility(View.VISIBLE);
            mPortraitView.setScaleX(1);
            mPortraitView.setScaleY(1);
            mPortraitView.setAlpha(1);
        }else{
            verticalOffset = Math.abs(verticalOffset);
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if(totalScrollRange <= verticalOffset){    //我的偏移量连为1你总共也为1
                mPortraitView.setVisibility(View.INVISIBLE);
                mPortraitView.setScaleX(0);
                mPortraitView.setScaleY(0);
                mPortraitView.setAlpha(0);
            }else{
                // 中间状态
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                mPortraitView.setVisibility(View.VISIBLE);
                mPortraitView.setScaleX(progress);
                mPortraitView.setScaleY(progress);
                mPortraitView.setAlpha(progress);

            }
        }
    }

    @Override
    public void onInit(User user) {
        //对和你聊天的朋友进行初始化操作
        mPortraitView.setup(Glide.with(this),user.getPortrait());
        mCollapsingToolbarLayout.setTitle(user.getName());
        Glide.with(this).load(R.drawable.default_banner_personal).into(new ViewTarget<CollapsingToolbarLayout,GlideDrawable>(mCollapsingToolbarLayout) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setContentScrim(resource.getCurrent());
            }
        });
    }

    @Override
    protected ChatControl.Presenter initPresenter() {
        return new ChatUserPresenter(this,mReceiverId);
    }
}
