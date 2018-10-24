package cn.dustray.chat;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.dustray.defenderplatform.R;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.popupwindow.TextMenuPopup;

import static android.view.View.generateViewId;

public class ChatHolder extends RecyclerView.ViewHolder {
    Context context;
    ImageButton headBtn;
    TextView textView;
    SimpleDraweeView imageView;
    RelativeLayout frame;

    public ChatHolder(Context context, View itemView, int type) {
        super(itemView);
        this.context = context;
        headBtn = itemView.findViewById(R.id.chat_list_item_head);
        frame = itemView.findViewById(R.id.chat_list_item_frame);
        switch (type) {
            case ChatRecordEntity.MESSAGE_TYPE_TEXT:
                initTextView();
                break;
            case ChatRecordEntity.MESSAGE_TYPE_IMAGE:
                initImageView();
                break;
        }
    }


    public void addView(ChatRecordEntity entity) {
        switch (entity.getMessageType()) {
            case ChatRecordEntity.MESSAGE_TYPE_TEXT:
                textView.setText(entity.getChatContent());
                if (entity.getTransmitType() == ChatRecordEntity.TRANSMIT_TYPE_RECEIVED)
                    textView.setTextColor(Color.WHITE);
                else
                    textView.setTextColor(Color.BLACK);
                break;
            case ChatRecordEntity.MESSAGE_TYPE_IMAGE:
                //Log.i("Constraints", "" +entity.getChatContent());
                Uri uri = Uri.parse(entity.getChatContent());
                imageView.setImageURI(uri);
                break;
        }
    }


    private void initTextView() {
        textView = new TextView(context);
        textView.setText(" ");
        textView.setTextSize(16f);
        textView.setId(generateViewId());
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new TextMenuPopup(context, textView.getText().toString()).showAtBottom(textView);
                return false;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "0---"+holder.textContent.getText().toString() , Toast.LENGTH_LONG).show();

            }
        });
        frame.addView(textView);
    }

    private void initImageView() {
        imageView = new SimpleDraweeView(context);
        imageView.setId(generateViewId());
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //new TextMenuPopup(context,imageView).showAtBottom(textView);
                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "0---"+holder.textContent.getText().toString() , Toast.LENGTH_LONG).show();

            }
        });
        frame.addView(imageView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        imageView.setLayoutParams(params);
        imageView.setAspectRatio(1.33f);
        imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);

    }
}
