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
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.dustray.defenderplatform.R;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.popupwindow.TextMenuPopup;
import cn.dustray.utils.PixelConvert;

import static android.view.View.generateViewId;

public class ChatHolder extends RecyclerView.ViewHolder {
    Context context;
    ImageButton headBtn;
    TextView textView;
    SimpleDraweeView imageView;
    RelativeLayout frame;
    private static int ROUND_CORNER_SIZE;

    public ChatHolder(Context context, View itemView, int type) {
        super(itemView);
        this.context = context;
        ROUND_CORNER_SIZE=PixelConvert.dip2px(context,10);
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
        adjustFrame(entity);
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
                RoundingParams roundingParams;
                if (entity.getTransmitType() == ChatRecordEntity.TRANSMIT_TYPE_RECEIVED)
                    roundingParams = RoundingParams.fromCornersRadii(
                            0,
                            ROUND_CORNER_SIZE,
                            ROUND_CORNER_SIZE,
                            ROUND_CORNER_SIZE);
                else
                    roundingParams = RoundingParams.fromCornersRadii(
                            ROUND_CORNER_SIZE,
                            0,
                            ROUND_CORNER_SIZE,
                            ROUND_CORNER_SIZE);
                imageView.getHierarchy().setRoundingParams(roundingParams);
                Uri uri = Uri.parse(entity.getChatContent());
                imageView.setImageURI(uri);
                break;
        }
    }

    private void adjustFrame(ChatRecordEntity entity) {
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) headBtn.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) frame.getLayoutParams();
        if (entity.getTransmitType() == ChatRecordEntity.TRANSMIT_TYPE_RECEIVED) {//接收的的消息
            frame.setBackgroundResource(R.drawable.bubble_left_lightblue);
            // holder.textContent.setTextColor(Color.WHITE);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {//发送的消息
            frame.setBackgroundResource(R.drawable.bubble_right_gray);
            //holder.textContent.setTextColor(Color.BLACK);
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        //holder.frame.setTag(position);//tag
        headBtn.setLayoutParams(params1);
        frame.setLayoutParams(params2);
    }


    private void initTextView() {
        textView = new TextView(context);
        textView.setText(" ");
        textView.setTextSize(16f);
        textView.setId(generateViewId());
        textView.setPadding(ROUND_CORNER_SIZE, ROUND_CORNER_SIZE,ROUND_CORNER_SIZE, ROUND_CORNER_SIZE);
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
        params.width = PixelConvert.dip2px(context, 150);
        imageView.setLayoutParams(params);
        imageView.setAspectRatio(1.33f);
        imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);


    }
}
