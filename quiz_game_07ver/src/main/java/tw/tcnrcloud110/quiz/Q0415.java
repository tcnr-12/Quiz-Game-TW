package tw.tcnrcloud110.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Q0415 extends RecyclerView.Adapter<Q0415.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Q0414> mData;
    //    -------------------------------------------------------------------
    private OnItemClickListener mOnItemClickListener = null;

    //--------------------------------------------
    public Q0415(Context context, ArrayList<Q0414> data) {
        this.mContext = context;
        this.mData = data;
    }

    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //---------------onCreateViewHolder()是將item layout建立並回傳view holder----------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.q0414, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.TransDate = (TextView) view.findViewById(R.id.q0414_t001);
        holder.CropName = (TextView) view.findViewById(R.id.q0414_t002);
        holder.MarketName = (TextView) view.findViewById(R.id.q0414_t003);
        holder.Upper_Price = (TextView) view.findViewById(R.id.q0414_t004);
        holder.Middle_Price = (TextView) view.findViewById(R.id.q0414_t005);
        holder.Lower_Price = (TextView) view.findViewById(R.id.q0414_t006);
        holder.Avg_Price = (TextView) view.findViewById(R.id.q0414_t007);
        holder.Trans_Quantity = (TextView) view.findViewById(R.id.q0414_t008);
        //----------------------------------------------------
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
        return holder;
    }

    // 將資料與每個清單列表位置的view holder綁定,並傳入recycler view 在滑動時更換holder內的資料
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Q0414 post = mData.get(position);
        holder.TransDate.setText(post.TransDate);
        holder.CropName.setText("農產品名稱: " + post.CropName);
        holder.MarketName.setText("市場名稱: " + post.MarketName);
        holder.Upper_Price.setText("上價(元/公斤): " + post.Upper_Price);
        holder.Middle_Price.setText("中價(元/公斤): " + post.Middle_Price);
        holder.Lower_Price.setText("下價(元/公斤): " + post.Lower_Price);
        holder.Avg_Price.setText("平均價(元/公斤): " + post.Avg_Price);
        holder.Trans_Quantity.setText("交易量(公斤): " + post.Trans_Quantity);

        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);
    }

    //    回傳list的size,RecyclerView依據getItemCount顯示資料筆數
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //======= sub class   ==================
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView TransDate;
        public TextView CropName;
        public TextView MarketName;
        public TextView Upper_Price;
        public TextView Middle_Price;
        public TextView Lower_Price;
        public TextView Avg_Price;
        public TextView Trans_Quantity;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
//-----------------------------------------------
}
