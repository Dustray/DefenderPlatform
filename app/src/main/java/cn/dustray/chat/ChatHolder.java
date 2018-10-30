package cn.dustray.chat;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.entity.LinkEntity;
import cn.dustray.popupwindow.TextMenuPopup;
import cn.dustray.utils.PixelConvert;
import cn.dustray.utils.xToast;

import static android.view.View.generateViewId;

public class ChatHolder extends RecyclerView.ViewHolder {
    Context context;
    ImageButton headBtn;
    TextView textView, linkView;
    SimpleDraweeView imageView;
    RelativeLayout frame;
    private static int ROUND_CORNER_SIZE;
    private static int IMAGE_SIZE;
    String link = "";

    public ChatHolder(Context context, View itemView, int type) {
        super(itemView);
        this.context = context;
        ROUND_CORNER_SIZE = PixelConvert.dip2px(context, 10);
        IMAGE_SIZE = PixelConvert.dip2px(context, 150);
        headBtn = itemView.findViewById(R.id.chat_list_item_head);
        frame = itemView.findViewById(R.id.chat_list_item_frame);
        switch (type) {
            case ChatRecordEntity.MESSAGE_TYPE_TEXT:
                initTextView();
                break;
            case ChatRecordEntity.MESSAGE_TYPE_IMAGE:
                initImageView();
                break;
            case ChatRecordEntity.MESSAGE_TYPE_LINK:
                initLinkView();
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
                //imageView.setImageURI(uri);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setProgressiveRenderingEnabled(true)
                        .setResizeOptions(new ResizeOptions(IMAGE_SIZE, IMAGE_SIZE))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(imageView.getController())
                        .setAutoPlayAnimations(true)
                        .setTapToRetryEnabled(true)
                        .build();
                imageView.setController(controller);
                break;
            case ChatRecordEntity.MESSAGE_TYPE_LINK:
                LinkEntity linkEntity = entity.getLinkEntity();
                textView.setText(linkEntity.getLinkTitle());
                linkView.setText(linkEntity.getLinkDescription());
                link = linkEntity.getLinkUrl();
                break;
        }
    }

    private void adjustFrame(@NonNull ChatRecordEntity entity) {
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) headBtn.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) frame.getLayoutParams();
        if (entity.getTransmitType() == ChatRecordEntity.TRANSMIT_TYPE_RECEIVED) {//接收的的消息
            if (entity.getMessageType() == ChatRecordEntity.MESSAGE_TYPE_LINK) {//链接格式
                frame.setBackgroundResource(R.drawable.bubble_left_lightgray);
            } else
                frame.setBackgroundResource(R.drawable.bubble_left_lightblue);
            // holder.textContent.setTextColor(Color.WHITE);
            params1.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params2.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {//发送的消息
            if (entity.getMessageType() == ChatRecordEntity.MESSAGE_TYPE_LINK) {//链接格式
                frame.setBackgroundResource(R.drawable.bubble_right_lightgray);
            } else
                frame.setBackgroundResource(R.drawable.bubble_right_gray);
            //holder.textContent.setTextColor(Color.BLACK);
            params1.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
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
        textView.setPadding(ROUND_CORNER_SIZE, ROUND_CORNER_SIZE, ROUND_CORNER_SIZE, ROUND_CORNER_SIZE);
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
                //xToast.toast(context,"sss");
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
        params.width = IMAGE_SIZE;
        imageView.setLayoutParams(params);
        imageView.setAspectRatio(1f);
        imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        imageView.getHierarchy().setPlaceholderImage(R.drawable.img_picture_loading);//正在加载图片
        imageView.getHierarchy().setFailureImage(R.drawable.img_picture_load_failed);//加载失败图片
        imageView.getHierarchy().setRetryImage(R.drawable.img_picture_reload);//重试图片
    }

    private void initLinkView() {
        textView = new TextView(context);
        textView.setText("");
        textView.setTextSize(14f);
        textView.setMaxLines(2);
        textView.setTextColor(Color.BLACK);
        textView.setId(generateViewId());
        textView.setPadding(ROUND_CORNER_SIZE, ROUND_CORNER_SIZE, ROUND_CORNER_SIZE, 0);
        textView.setWidth(PixelConvert.dip2px(context, 200));
        frame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new TextMenuPopup(context, link).showAtBottom(textView);
                return false;
            }
        });
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "0---"+holder.textContent.getText().toString() , Toast.LENGTH_LONG).show();
                ((MainActivity) context).browserFragment.search(link, 0);
                ((MainActivity) context).switchToWeb();
            }
        });
        frame.addView(textView);

        linkView = new TextView(context);
        linkView.setText("");
        linkView.setTextSize(11f);
        linkView.setMaxLines(3);
        linkView.setId(generateViewId());
        linkView.setPadding(ROUND_CORNER_SIZE, 5, ROUND_CORNER_SIZE, ROUND_CORNER_SIZE);
        frame.addView(linkView);

        //frame.setBackgroundColor(Color.rgb(240, 240, 240));
        //linkView.setBackgroundColor(Color.rgb(240, 240, 240));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linkView.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, textView.getId());
        linkView.setWidth(PixelConvert.dip2px(context, 220));
        linkView.setLayoutParams(params);

    }
}















