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
