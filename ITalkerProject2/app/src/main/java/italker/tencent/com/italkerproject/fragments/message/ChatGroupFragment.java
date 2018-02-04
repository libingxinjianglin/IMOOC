package italker.tencent.com.italkerproject.fragments.message;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.List;

import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.view.MemberUserModel;
import drawable.tencent.com.factory.presenter.message.ChatControl;
import drawable.tencent.com.factory.presenter.message.ChatGroupPresenter;
import italker.tencent.com.common.weiget.PortraitView;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.activits.PsersonActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatControl.GroupView {

    public PortraitView mPortraitView;

    LinearLayout mLayMembers;

    TextView mMemberMore;

    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mPortraitView = view.findViewById(R.id.im_portrait);
        mLayMembers = view.findViewById(R.id.lay_members);
        mMemberMore = view.findViewById(R.id.txt_member_more);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //表示你的垂直距离已经是0了不能再次下滑了
        View mPortraitView = mLayMembers;
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
    public void onInit(Group group) {
        mCollapsingToolbarLayout.setTitle(group.getName());
        Glide.with(this)
                .load(R.drawable.default_banner_group)
                .centerCrop()
                .placeholder(R.drawable.default_banner_group)
                .into(mHead);
    }

    @Override
    protected ChatControl.Presenter initPresenter() {
        return new ChatGroupPresenter(this,mReceiverId);
    }

    @Override
    public void showAdminOption(boolean isAdmin) {

        if (isAdmin) {
            mToolbar.inflateMenu(R.menu.chat_group);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_add) {
                        // TODO 进行群成员添加操作
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, long moreCount) {
        if(members != null && members.size()>0){
            for (final MemberUserModel member : members) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                ImageView view = (ImageView)layoutInflater.inflate(R.layout.lay_group_portiorty, mLayMembers, false);
                mLayMembers.addView(view,0);
                Glide.with(this)
                        .load(member.portrait)
                        .placeholder(R.drawable.default_portrait)
                        .centerCrop()
                        .dontAnimate()
                        .into(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PsersonActivity.show(getContext(),member.userId);
                    }
                });
            }
            // 更多的按钮
            if (moreCount > 0) {
                mMemberMore.setText(String.format("+%s", moreCount));
                mMemberMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 显示成员列表
                    }
                });
            } else {
                mMemberMore.setVisibility(View.GONE);
            }
        }
    }
}
