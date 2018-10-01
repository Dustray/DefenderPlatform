package cn.dustray.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.dustray.defenderplatform.R;
import cn.dustray.entity.ChatRecordEntity;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    Context context;
    List<ChatRecordEntity> list;

    public ChatListAdapter(Context context, List<ChatRecordEntity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_recycle, parent, false);

        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setIsRecyclable(false);//混乱临时解决办法
        ChatRecordEntity s = list.get(position);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) holder.headBtn.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) holder.textContent.getLayoutParams();


        holder.textContent.setText(s.getChatContent());
        // TODO: 2018/9/30 0030  Fix Bug：刷新混乱
        if (holder.textContent.getTag() == null) {//tag

            if (list.get(position).getMessageType() == ChatRecordEntity.TYPE_RECEIVED) {//接收的的消息
                holder.textContent.setBackgroundResource(R.drawable.bubble_left_lightblue);
                holder.textContent.setTextColor(Color.WHITE);
                params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else {//发送的消息
                holder.textContent.setBackgroundResource(R.drawable.bubble_right_gray);
                holder.textContent.setTextColor(Color.BLACK);
                params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            holder.textContent.setTag(position);//tag
            holder.headBtn.setLayoutParams(params1);
            holder.textContent.setLayoutParams(params2);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        Button headBtn;
        TextView textContent;

        public Holder(View itemView) {
            super(itemView);
            headBtn = itemView.findViewById(R.id.chat_list_item_head);
            textContent = itemView.findViewById(R.id.chat_list_item);
        }
    }
}
