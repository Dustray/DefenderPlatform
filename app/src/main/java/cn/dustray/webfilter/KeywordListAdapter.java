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

    public KeywordListAdapter(Context context, List<KeywordEntity> list) {
        this.context = context;
        this.list = list;
    }
public void sync(List<KeywordEntity> list){
    this.list = list;
    notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull KeywordHolder holder, int position) {
        KeywordEntity ke = list.get(position);
        holder.setText(ke.getKeyword());
        holder.setId(ke.getObjectId());
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
    }
}
