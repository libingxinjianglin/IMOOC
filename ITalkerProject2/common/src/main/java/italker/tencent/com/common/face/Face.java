package italker.tencent.com.common.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import italker.tencent.com.common.utils.StreamUtil;

/**
 * 表情工具类
 */

public class Face {
    // 全局的表情的映射ArrayMap，更加轻量级
    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();
    private static List<FaceTab> FACE_TABS = null;

    private static void init(Context context) {
        if (FACE_TABS == null) {
            synchronized (Face.class) {
                if (FACE_TABS == null) {
                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    FaceTab tab = initAssetsFace(context);
                    if (tab != null)
                        faceTabs.add(tab);


                    FaceTab tabSecond = initResourceFace(context);
                    if (tab != null)
                        faceTabs.add(tabSecond);

                    // init map
                    for (FaceTab faceTab : faceTabs) {
                        faceTab.copyToMap(FACE_MAP);
                    }

                    // init list 不可变的集合
                    FACE_TABS = (faceTabs);
                }
            }

        }

    }

    // 从face-t.zip包解析我们的表情
    private static FaceTab initAssetsFace(Context context) {
        String faceAsset = "face-t.zip";
        // data/data/包名/files/face/ft/*
        String faceCacheDir = String.format("%s/face/tf", context.getFilesDir());
        File faceFolder = new File(faceCacheDir);
        if (!faceFolder.exists()) {
            // 不存在进行初始化
            if (faceFolder.mkdirs()) {
                try {
                    InputStream inputStream = context.getAssets().open(faceAsset);
                    // 存储文件
                    File faceSource = new File(faceFolder, "source.zip");
                    // copy
                    StreamUtil.copy(inputStream, faceSource);

                    // 解压
                    unZipFile(faceSource, faceFolder);

                    // 清理文件
                    StreamUtil.delete(faceSource.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // info.json
        File infoFile = new File(faceCacheDir, "info.json");
        // Gson

        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = gson.newJsonReader(new FileReader(infoFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // 解析
        FaceTab tab = gson.fromJson(reader, FaceTab.class);

        // 相对路径到绝对路径
        for (Bean face : tab.faces) {
            face.preview = String.format("%s/%s", faceCacheDir, face.preview);
            face.source = String.format("%s/%s", faceCacheDir, face.source);
        }
        for (Bean bean : tab.faces) {
            String key = bean.key;
            Object source = bean.source;
        }

        return tab;
    }

    // 把zipFile解压到desDir目录
    private static void unZipFile(File zipFile, File desDir) throws IOException {
        final String folderPath = desDir.getAbsolutePath();

        ZipFile zf = new ZipFile(zipFile);
        // 判断节点进行循环
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 过滤缓存的文件
            String name = entry.getName();
            if (name.startsWith("."))
                continue;

            // 输入流
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + name;

            // 防止名字错乱
            str = new String(str.getBytes("8859_1"), "GB2312");

            File desFile = new File(str);
            // 输出文件
            StreamUtil.copy(in, desFile);
        }
    }

    // 从drawable资源中加载数据并映射到对应的key
    private static FaceTab initResourceFace(Context context) {
        final List<Bean> faces = new ArrayList<>();
        final Resources resources = context.getResources();
        String packageName = context.getApplicationInfo().packageName;
        for (int i = 1; i <= 142; i++) {

            // i=1=>  001
            String key = String.format(Locale.ENGLISH, "fb%03d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);

            // 根据资源名称去拿资源对应的ID
            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            if (resId == 0)
                continue;

            Bean bean = new Bean(key, resId);

            String mKey = bean.key;
            Object mSource = bean.source;
            // 添加表情
            faces.add(bean);

        }
        for (Bean bean : faces) {
            String mKey = bean.key;
            Object mSource = bean.source;
        }

        if (faces.size() == 0)
            return null;

        return new FaceTab("NAME", faces.get(0).preview, (ArrayList<Bean>) faces);
    }

    // 获取所有的表情
    public static List<FaceTab> all(@NonNull Context context) {
        init(context);
        return FACE_TABS;
    }

    // 输入表情到editable
    public static void inputFace(@NonNull final Context context, final Editable editable,
                                 final Face.Bean bean, final int size) {
        Glide.with(context)
                .load(bean.preview)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(size, size) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Spannable spannable = new SpannableString(String.format("[%s]", bean.key));
                        ImageSpan span = new ImageSpan(context, resource, ImageSpan.ALIGN_BASELINE);
                        // 前后不关联
                        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editable.append(spannable);
                    }
                });
    }

    // 从spannable解析表情并替换显示
    public static List<FaceTab> decode(@NonNull View target, final Spannable spannable, final int size) {
        return null;
    }


    /**
     * 每一个表情盘，含有很多表情
     */
    public static class FaceTab {
        public List<Bean> faces = new ArrayList<>();
        public String name;
        // 预览图, 包括了drawable下面的资源int类型
        public Object preview;

        public FaceTab(String name, Object preview, ArrayList<Bean> faces) {
            this.name = name;
            this.faces = faces;

            this.preview = preview;
        }

        public void copyToMap(ArrayMap<String, Bean> faceMap) {
            for (Bean face : faces) {

                faceMap.put(face.key,face);
            }
        }
    }

    /**
     * 每一个表情
     */
    public static class Bean {
        public  String key;
        public  String desc;
        public  Object source;
        public  Object preview;

        public Bean(String key, int resId) {
            this.key = key;
            this.source = resId;
            this.preview = resId;
        }
    }

}
