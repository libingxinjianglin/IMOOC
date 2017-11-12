package italker.tencent.com.common.weiget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/12 0012.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<MyViewHolder<Data>> implements View.OnClickListener,View.OnLongClickListener,RecyclerCallBack{

    protected List<Data> mList = new ArrayList<Data>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mRoot = View.inflate(null,viewType,null);
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
