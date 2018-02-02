package drawable.tencent.com.factory.presenter.group;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.R;
import drawable.tencent.com.factory.data.helper.GroupHelper;
import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.model.api.GroupCreateModel;
import drawable.tencent.com.factory.model.card.GroupCard;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.view.UserSampleModel;
import drawable.tencent.com.factory.net.UpLoadHelper;
import italker.tencent.com.common.factory.data.DataSource;
import italker.tencent.com.common.factory.presenter.BaseRecyclerPresenter;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateControl.ViewModel,GroupCreateControl.View> implements GroupCreateControl.Presenter{
    private Set<String> users = new HashSet<>();
    public GroupCreatePresenter(GroupCreateControl.View view) {
        super(view);
    }

    @Override
    public void statr() {
        super.statr();
        // 加载
       Factory.RunAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateControl.View view = getView();
        view.showLoading();
        //判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
            return;
        }
        //上传图片
            Factory.RunAsync(new Runnable() {
                @Override
                public void run() {
                    String uri = uploadPicture(picture);
                    if(TextUtils.isEmpty(uri)){
                        return ;
                    }
                    GroupCreateModel model = new GroupCreateModel(name,desc,uri,users);
                    GroupHelper.create(model,new DataSource.Callback<GroupCard>(){

                        @Override
                        public void onDataLoaded(GroupCard groupCard) {
                            // 成功
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    GroupCreateControl.View view = getView();
                                    if (view != null) {
                                        view.onCreateSucceed();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onDataNotAvailable(final @StringRes int strRes) {
                            // 失败情况
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    GroupCreateControl.View view = getView();
                                    if (view != null) {
                                        view.showError(strRes);
                                    }
                                }
                            });
                        }
                    });
                }
            });
        //请求接口

        //请求回调
    }
    // 同步上传操作
    private String uploadPicture(String path) {
        String url = UpLoadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            // 切换到UI线程 提示信息
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateControl.View view = getView();
                    if (view == null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }
    @Override
    public void changeSelect(GroupCreateControl.ViewModel model, boolean isSelected) {
        if (isSelected)
            users.add(model.author.getId());
        else
            users.remove(model.author.getId());
    }

    private Runnable loader = new Runnable() {
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateControl.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateControl.ViewModel viewModel = new GroupCreateControl.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };
}
