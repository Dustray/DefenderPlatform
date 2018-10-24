package cn.dustray.popupwindow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.dustray.control.xWebPopupWindow;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.utils.xToast;

public class WebSharePopup extends xWebPopupWindow implements View.OnClickListener {

    private Context context;
    private Button btnShareChat, btnShareCopy;
    private ImageButton btnExit;
    private String title, url;
    private TextView textTitle;

    public WebSharePopup(Context context, String title, String url) {
        super(context);
        this.context = context;
        this.title = title;
        this.url = url;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_web_share, null);
        setContentView(view);
        initButton();
    }

    private void initButton() {
        textTitle = getContentView().findViewById(R.id.text_web_share_title);
        textTitle.setText(title);
        btnShareChat = getContentView().findViewById(R.id.btn_web_share_chat);
        btnShareChat.setOnClickListener(this);
        btnShareCopy = getContentView().findViewById(R.id.btn_web_share_copy);
        btnShareCopy.setOnClickListener(this);
        btnExit = getContentView().findViewById(R.id.btn_web_share_down);
        btnExit.setOnClickListener(this);
    }


    public void showAtBottom(View view) {
        //弹窗位置设置
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
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
                xToast.toast(context, "复制成功");
                break;
            case R.id.btn_web_menu_down:
                break;
        }
        dismiss();
    }
}
