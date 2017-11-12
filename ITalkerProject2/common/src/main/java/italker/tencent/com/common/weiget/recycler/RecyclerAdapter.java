package italker.tencent.com.common.weiget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/11/12 0012.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<MyViewHolder<Data>> implements View.OnClickListener,View.OnLongClickListener,RecyclerCallBack{

    protected Context context;
    protected List<Data> mList = new ArrayList<Data>();
    protected  AdapterListener mListener;

    public RecyclerAdapter(List<Data> mList, Context context){
        this.mList = mList;
        this.context = context;
    }
    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，其实复写后返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数据
     * @return XML文件的ID，用于创建ViewHolder
     */
    protected abstract int getItemViewType(int position, Data data);


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mRoot = View.inflate(context,viewType,null);
        MyViewHolder<Data> mHolder = getViewHolder(mRoot,viewType);
        mRoot.setOnClickListener(this);
        mRoot.setOnLongClickListener(this);
        mRoot.setTag(mHolder);
        mHolder.mCallBack = this;
        return mHolder;
    }

    protected abstract MyViewHolder<Data> getViewHolder(View mRoot, int viewType);

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //得到具体的数据
        Data data = mList.get(position);
        //触发绑定
        holder.onbind();

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 添加了一条数据
     * @param data
     */
    public void addData(Data data){
        mList.add(data);
        notifyItemInserted(mList.size() - 1);
    }
    /**
     * 添加了一堆数据
     */
    public void addData(Data...data){
        if(data.length > 0 && data != null){
            int start = data.length;
            Collections.addAll(mList,data);
            notifyItemRangeChanged(start,mList.size());
        }
    }
    /**
     * 删除操作
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加一堆数据
     */
    public void addData(Collection<Data> data){
        mList.addAll(data);
        notifyDataSetChanged();
    }
    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        mList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        MyViewHolder viewHolder = (MyViewHolder) v.getTag();
        if (this.mListener != null) {
            // 得到ViewHolder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回掉方法
            this.mListener.onItemClick(viewHolder, mList.get(pos));
        }

    }

    @Override
    public boolean onLongClick(View v) {
        MyViewHolder viewHolder = (MyViewHolder) v.getTag();
        if (this.mListener != null) {
            // 得到ViewHolder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回掉方法
            this.mListener.onItemLongClick(viewHolder, mList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器的监听
     *
     * @param adapterListener AdapterListener
     */
    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 我们的自定义监听器
     *
     * @param <Data> 范型
     */
    public interface AdapterListener<Data> {
        // 当Cell点击的时候触发
        void onItemClick(MyViewHolder holder, Data data);

        // 当Cell长按时触发
        void onItemLongClick(MyViewHolder holder, Data data);
    }

}
abstract class MyViewHolder<Data> extends RecyclerView.ViewHolder{
    protected Data mData;
    protected RecyclerCallBack mCallBack;
     public MyViewHolder(View itemView) {
         super(itemView);
     }

     /**
      * 用于绑定数据的触发
      */
     public void onBind(Data data){
         this.mData = data;
         onbind();
     }

     protected abstract void onbind();
     public void updataData(Data data){
         mCallBack.updata(data,this);
    }
 }
