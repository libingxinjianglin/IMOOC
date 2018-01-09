package italker.tencent.com.italkerproject.application;

import com.igexin.sdk.PushManager;

import drawable.tencent.com.factory.Factory;
import drawable.tencent.com.factory.persistence.Account;
import italker.tencent.com.common.app.MyApplication;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class app extends MyApplication {
   @Override
   public void onCreate() {
      super.onCreate();
      Factory.setup();
      PushManager.getInstance().initialize(this);
   }
}
