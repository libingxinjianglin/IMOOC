package drawable.tencent.com.factory.presenter.message;

import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.factory.presenter.BaseControl;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public interface ChatControl {
    interface Presenter extends BaseControl.Presenter {
        // 发送文字
        void pushText(String content);

        // 发送语音
        void pushAudio(String path);

        // 发送图片
        void pushImages(String[] paths);

        // 重新发送一个消息，返回是否调度成功
        boolean rePush(Message message);
    }

    // 界面的基类
    interface View<InitModel> extends BaseControl.RecyclerView<Presenter, Message> {
        // 初始化的Model
        void onInit(InitModel model);
    }

    // 人聊天的界面
    interface UserView extends View<User> {

    }

    // 群聊天的界面
    interface GroupView extends View<Group> {

    }
}
