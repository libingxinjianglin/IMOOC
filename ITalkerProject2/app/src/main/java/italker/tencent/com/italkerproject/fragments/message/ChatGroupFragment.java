package italker.tencent.com.italkerproject.fragments.message;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.presenter.message.ChatControl;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGroupFragment extends ChatFragment<Group> {


    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @Override
    public void onInit(Group group) {

    }

    @Override
    protected ChatControl.Presenter initPresenter() {
        return null;
    }
}
