package drawable.tencent.com.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import java.util.List;

import drawable.tencent.com.factory.data.helper.MessageHelper;
import drawable.tencent.com.factory.data.message.MessageDataSource;
import drawable.tencent.com.factory.model.api.MsgCreateModel;
import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.persistence.Account;
import drawable.tencent.com.factory.presenter.BaseSourcePresenter;
import drawable.tencent.com.factory.utils.DiffUiDataCallback;
import italker.tencent.com.common.weiget.recycler.RecyclerAdapter;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class ChatPresenter<View extends ChatControl.View>
        extends BaseSourcePresenter<Message,Message,MessageDataSource,View>
        implements ChatControl.Presenter {
    protected String mReceiverId;
    protected int mReceiverType;


    public ChatPresenter(MessageDataSource source, View view,String receiverId,int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        MsgCreateModel model = new MsgCreateModel
                .Builder()
                .receiver(mReceiverId,mReceiverType)
                .content(content,Message.TYPE_STR)
                .build();
        MessageHelper.put(model);
    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushImages(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {
        if(Account.getUser().getId().equalsIgnoreCase(message.getSender().getId()) && message.getStatus() == Message.STATUS_FAILED){
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.put(model);
            return true;
        }
        return false;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatControl.View view = getView();
        if(view == null)
            return;
        RecyclerAdapter adapter = view.getRecycler();
        List<Message> items = adapter.getItems();
        DiffUtil.Callback call = new DiffUiDataCallback<Message>(items,messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(call);
        refreshData(result,messages);
    }
}
