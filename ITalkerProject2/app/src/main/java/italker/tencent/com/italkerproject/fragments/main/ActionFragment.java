package italker.tencent.com.italkerproject.fragments.main;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.weiget.recycler.GalleyView;
import italker.tencent.com.italkerproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionFragment extends Fragment {
    private GalleyView mGalley;

    public ActionFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_action;
    }

    @Override
    protected void initWidget(View view) {
        mGalley = (GalleyView)view.findViewById(R.id.galley);
    }

    @Override
    protected void initData() {
        super.initData();
        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int size) {

            }
        },getContext());
    }
}
