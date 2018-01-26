package drawable.tencent.com.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.model.api.MsgCreateModel;
import drawable.tencent.com.factory.model.api.RspModel;
import drawable.tencent.com.factory.model.card.MessageCard;
import drawable.tencent.com.factory.model.card.UserCard;
import drawable.tencent.com.factory.model.db.Message;
import drawable.tencent.com.factory.model.db.Message_Table;
import drawable.tencent.com.factory.net.Network;
import drawable.tencent.com.factory.net.RemoteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class MessageHelper {
    public static Message findFromLocal(String id) {
        //TODO 得到本地的一个消息
        return SQLite.select().from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void put(final MsgCreateModel model) {
        Factory.RunAsync(new Runnable() {
            @Override
            public void run() {
                //如果是一个已经发送过得消息我们就不进行发送
                Message local = findFromLocal(model.getId());
                if(local != null && local.getStatus()!=Message.STATUS_FAILED){
                    return ;
                }
                //如果是一个文件语音啥的就先上传在发送

                //当我们发送的时候通知界面刷新我们的card
                final MessageCard messageCard = model.buildCard();
                Factory.getMessageCenter().dispatch(messageCard);

                //直接发送
                RemoteService service = Network.getRetrofit().create(RemoteService.class);
                Call<RspModel<MessageCard>> call = service.msgPush(model);
                call.enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> body = response.body();
                        if(body != null && body.success()){
                            MessageCard card = body.getResult();
                            card.setStatus(Message.STATUS_DONE);
                            Factory.getMessageCenter().dispatch(card);
                        }else{
                            onFailure(call,null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        messageCard.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(messageCard);
                    }
                });
            }
        });
    }
}
