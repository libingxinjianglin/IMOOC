package drawable.tencent.com.factory.net;

import android.text.format.DateFormat;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.Date;

import drawable.tencent.com.factory.Factory;
import italker.tencent.com.common.utils.HashUtil;

/**
 * Created by Administrator on 2017/12/27 0027.
 * 上传图片
 */

public class UpLoadHelper {
    private static final String TAG = UpLoadHelper.class.getSimpleName();
    // 与你们的存储区域有关系
    private static final String ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";
    // 上传的仓库名
    private static final String BUCKET_NAME = "italker-new-666";


    public static OSS getClient(){
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考访问控制章节
        // 也可查看sample 中 sts 使用方式了解更多(https://github.com/aliyun/aliyun-oss-android-sdk/tree/master/app/src/main/java/com/alibaba/sdk/android/oss/app)
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIx4LqgHIPfb3N","NqO9z9u1j5Ygxrucg9gWtX5dEwAqVW");

        return new OSSClient(Factory.getApp(), ENDPOINT, credentialProvider);
    }

    public static String upload(String objkey,String path){
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objkey, path);
        try {
            OSS client = getClient();
            //开始同步上传
            PutObjectResult result = client.putObject(request);
            //得到一个外网访问可以访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME,objkey);
            return url;
        }catch (Exception e){
           System.out.println("aaaa"+e);
            return null;
        }
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }

    /**
     * 分月存储，避免一个文件夹太多
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    // image/201703/dawewqfas243rfawr234.jpg
    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    // portrait/201703/dawewqfas243rfawr234.jpg
    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    // audio/201703/dawewqfas243rfawr234.mp3
    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }
}

