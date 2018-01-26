package drawable.tencent.com.factory.presenter.message;

import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.data.message.MessageDataSource;
import drawable.tencent.com.factory.data.message.MessageRepository;
import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.model.db.User;
import italker.tencent.com.common.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class ChatUserPresenter extends ChatPresenter<ChatControl.UserView> implements ChatControl.Presenter{

    public ChatUserPresenter(ChatControl.UserView view, String receiverId) {
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void statr() {
        super.statr();
        User user = UserHelper.findFromLocal(mReceiverId);
        ChatControl.UserView view = getView();
        view.onInit(user);

    }
}
