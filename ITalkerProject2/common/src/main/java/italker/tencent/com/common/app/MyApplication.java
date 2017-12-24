package italker.tencent.com.common.app;

import android.os.SystemClock;

import java.io.File;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class MyApplication extends android.app.Application {
    public static MyApplication mlication;
    @Override
    public void onCreate() {
        super.onCreate();
        mlication = this;
    }

    public static File getCacheDirw(){
        return mlication.getCacheDir();
    }

    public static File getProparain(){
        File mFile = new File(getCacheDirw(),"Proparain");
        mFile.mkdir();

        File[] listFiles = mFile.listFiles();
        if(listFiles.length>0 && listFiles!= null) {
            for (File file :listFiles) {
                file.delete();
            }
        }

        File files = new File(mFile, SystemClock.currentThreadTimeMillis()+".jpg");
        return files.getAbsoluteFile();
    }

}
