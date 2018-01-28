package italker.tencent.com.common.utils;

/**
 * Created by Administrator on 2018/1/28 0028.
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataTime {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);
    public static String getSampleData(Date data){
        return FORMAT.format(data);
    }
}
