package tw.tcnrcloud110.quiz;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class Q0401a_RecyclerAdapter extends RecyclerView.Adapter<Q0401a_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
        private Context mContext;
        private ArrayList<Q0401a_Post> mData;
        //    -------------------------------------------------------------------
        private OnItemClickListener mOnItemClickListener = null;
        //--------------------------------------------
        public Q0401a_RecyclerAdapter(Context context, ArrayList<Q0401a_Post> data) {
            this.mContext = context;
            this.mData = data;
        }
        //    -------------------------------------------------------------------
        public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }
        //-------------------------------------------------------------------
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.q0401a_cell_post, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.Name = (TextView) view.findViewById(R.id.name);
            holder.Content = (TextView) view.findViewById(R.id.Content);
            holder.Text_url = (TextView) view.findViewById(R.id.text_url);
            holder.Description = (TextView) view.findViewById(R.id.description);
            //----------------------------------------------------
            //將創建的View註冊點擊事件
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Q0401a_Post post = mData.get(position);
            holder.Name.setText(post.Name);
            holder.Description.setText(post.Description);
            holder.Content.setText(post.Context);
            holder.Text_url.setText("網址 : "+post.Text_url);

//            if (post.Zipcode.length()>0){
//                holder.Zipcode.setText("["+post.Zipcode+"]");
//            }else{
//                holder.Zipcode.setText("[000]");
//            }
//            if (post.Website.length()>0){
//                holder.Website.setText(post.Website);
//            }else{
//                holder.Website.setText("無網站");
//            }
//============網址轉圖片================
            Glide.with(mContext)
                    .load(post.Picture1)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(100, 75)
                    .transition(withCrossFade())
                    .error(
                            Glide.with(mContext)
                                    .load("https://tcnr2021a12.000webhostapp.com/picture/nopic1.jpg"))
                    .into(holder.img);

            //將position保存在itemView的Tag中，以便點擊時進行獲取
            holder.itemView.setTag(position);
        }

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
            public ImageView img;
            public TextView Name;
            public TextView Description;
            public TextView Content;
            public TextView Text_url;

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    //-----------------------------------------------
    }