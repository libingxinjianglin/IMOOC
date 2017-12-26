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

    /**
     * 外部获得单例
     * @return
     */
    public static MyApplication getInstance(){
        return mlication;
    }

    public static File getCacheDirw(){
        return mlication.getCacheDir();
    }

    /**
     * 获取头像的临时存储头像地址
     * @return
     */
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

    /**
     * 获取声音文件的缓存地址
     */
    public static File getAudioFile(Boolean isTrue){
        File mFile = new File(getCacheDirw(),"audio");
        mFile.mkdir();

        File[] listFiles = mFile.listFiles();
        for (File file:listFiles) {
            file.delete();
        }

        File files = new File(mFile,isTrue ? "tms.mp3":SystemClock.currentThreadTimeMillis()+".mp3");
        return files.getAbsoluteFile();
    }

}
