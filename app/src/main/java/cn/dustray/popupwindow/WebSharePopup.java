package cn.dustray.popupwindow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.dustray.adapter.WebGroupListAdapter;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.defenderplatform.WebTabFragment;
import cn.dustray.tool.PixelConvert;
import cn.dustray.tool.xToast;

public class WebSharePopup extends PopupWindow implements View.OnClickListener {

    private Context context;
    private Button btnShareChat,btnShareCopy;
    private String title, url;
private TextView textTitle;
    public WebSharePopup(Context context, String title, String url) {
        this.context = context;
        this.title = title;
        this.url = url;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_web_share, null);

        setContentView(view);
        initWindow();
        initButton();
    }

    private void initButton() {
        textTitle = getContentView().findViewById(R.id.text_web_share_title);
        textTitle.setText(title);
        btnShareChat = getContentView().findViewById(R.id.btn_web_share_chat);
        btnShareChat.setOnClickListener(this);
        btnShareCopy = getContentView().findViewById(R.id.btn_web_share_copy);
        btnShareCopy.setOnClickListener(this);
    }


    private void initWindow() {
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(PixelConvert.dip2px(context, 295));

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setElevation(1);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.WHITE);        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        backgroundAlpha((Activity) context, 0.8f);//0.0-1.0
//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha((Activity) context, 1f);
//            }
//        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {        //弹窗位置设置
      setAnimationStyle(R.style.pop_animation);
        showAtLocation(view, Gravity.BOTTOM, 0, PixelConvert.dip2px(context, 45));

        //showAsDropDown(view, 0, PixelConvert.dip2px(context, 45),Gravity.BOTTOM);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_web_share_chat:
                String shareContent = "[" + title + "] " + url;
                ((MainActivity) context).chatFragment.sendMessage(shareContent);

                ((MainActivity) context).switchToChat();
                break;
            case R.id.btn_web_share_copy:
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                ClipData clipData = ClipData.newPlainText("DefenderPlatform", url);
                cm.setPrimaryClip(clipData);
                xToast.toast(context,"复制成功");
                break;
        } dismiss();
    }
}
