package italker.tencent.com.common.weiget.recycler;

/**
 * Created by Administrator on 2017/11/12 0012.
 */

public interface RecyclerCallBack<Data> {
     void updata(Data data,MyViewHolder<Data> ViewHolder);
}
