package cn.dustray.webfilter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.dustray.chat.ChatHolder;
import cn.dustray.defenderplatform.R;

public class KeywordListAdapter extends RecyclerView.Adapter<KeywordListAdapter.KeywordHolder> {
    private Context context;
    private List<KeywordEntity> list;
    private OnItemClickListener itemClickListener;

    public KeywordListAdapter(Context context, List<KeywordEntity> list) {
        this.context = context;
        this.list = list;
    }

    public void insert(List<KeywordEntity> list) {
        this.list=list;
        notifyItemRangeChanged(0,list.size());
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        if (position != list.size()) {
            notifyItemRangeChanged(position, list.size() - position);
        }
        
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public KeywordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_keyword, parent, false);

        final KeywordHolder holder = new KeywordHolder(context, view);
        //Log.i("Constraints", "" + viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final KeywordHolder holder, final int position) {
        KeywordEntity ke = list.get(position);
        holder.setText(ke.getKeyword());
        holder.setId(ke.getObjectId());
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(holder.tvKeyword, position);
            }
        });
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemClickListener.onItemLongClick(holder.tvKeyword, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class KeywordHolder extends RecyclerView.ViewHolder {

        private TextView tvKeyword;
        private String id = "";

        public KeywordHolder(Context context, View itemView) {
            super(itemView);
            tvKeyword = itemView.findViewById(R.id.tv_keyword);
        }

        public void setText(String text) {
            tvKeyword.setText(text);
        }

        public void setId(String objectId) {
            id = objectId;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            tvKeyword.setOnClickListener(listener);
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            tvKeyword.setOnLongClickListener(listener);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
