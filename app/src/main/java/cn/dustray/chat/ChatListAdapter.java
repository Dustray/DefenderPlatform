package cn.dustray.chat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.dustray.defenderplatform.R;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.popupwindow.TextMenuPopup;

public class ChatListAdapter extends RecyclerView.Adapter<ChatHolder> {

    Context context;
    List<ChatRecordEntity> list;

    public ChatListAdapter(Context context, List<ChatRecordEntity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_recycle, parent, false);

        final ChatHolder holder = new ChatHolder(context, view, viewType);
        //Log.i("Constraints", "" + viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
         holder.setIsRecyclable(false);//混乱临时解决办法

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) holder.headBtn.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) holder.frame.getLayoutParams();

        int tempPosition;
        // TODO: 2018/9/30 0030  Fix Bug：刷新混乱
        if (holder.frame.getTag() == null) {//tag
            tempPosition = position;
            holder.frame.setTag(position);//tag
        } else {
            tempPosition = Integer.parseInt(holder.frame.getTag().toString());
        }

        ChatRecordEntity s = list.get(tempPosition);
        holder.addView(s);
        //Log.i("Constraints", "" + list.get(position).getTransmitType());
        if (list.get(tempPosition).getTransmitType() == ChatRecordEntity.TRANSMIT_TYPE_RECEIVED) {//接收的的消息
            holder.frame.setBackgroundResource(R.drawable.bubble_left_lightblue);
            // holder.textContent.setTextColor(Color.WHITE);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {//发送的消息
            holder.frame.setBackgroundResource(R.drawable.bubble_right_gray);
            //holder.textContent.setTextColor(Color.BLACK);
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        //holder.frame.setTag(position);//tag
        holder.headBtn.setLayoutParams(params1);
        holder.frame.setLayoutParams(params2);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getMessageType();

    }
}
