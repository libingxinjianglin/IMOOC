package drawable.tencent.com.factory.presenter.message;

import java.util.List;

import drawable.tencent.com.factory.data.helper.GroupHelper;
import drawable.tencent.com.factory.data.helper.UserHelper;
import drawable.tencent.com.factory.data.message.MessageDataSource;
import drawable.tencent.com.factory.data.message.MessageGroupRepository;
import drawable.tencent.com.factory.data.message.MessageRepository;
import drawable.tencent.com.factory.model.db.Group;
import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.model.db.User;
import drawable.tencent.com.factory.model.db.view.MemberUserModel;
import drawable.tencent.com.factory.persistence.Account;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class ChatGroupPresenter extends ChatPresenter<ChatControl.GroupView> implements ChatControl.Presenter{

    public ChatGroupPresenter(ChatControl.GroupView view, String receiverId) {
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void statr() {
        super.statr();
        Group group = GroupHelper.findFromLocal(mReceiverId);
        ChatControl.GroupView view = getView();
        view.showAdminOption(Account.getUser().getId().equalsIgnoreCase(group.getOwner().getId()));
        // 基础信息初始化
        view.onInit(group);
        // 成员初始化
        List<MemberUserModel> models = group.getLatelyGroupMembers();
        final long memberCount = group.getGroupMemberCount();
        // 没有显示的成员的数量
        long moreCount = memberCount - models.size();
        view.onInitGroupMembers(models, moreCount);

    }
}
