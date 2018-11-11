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
import cn.dustray.entity.LinkEntity;
import cn.dustray.utils.xToast;

public class WebSharePopup extends xWebPopupWindow implements View.OnClickListener {

    private Context context;
    private Button btnShareChat, btnShareCopy;
    private ImageButton btnExit;
    private String title, url;
    private TextView textTitle;
    private boolean textOnly = false;

    public WebSharePopup(Context context, String title, String url) {
        super(context);
        this.context = context;
        this.title = title;
        this.url = url;
        init();
    }

    public WebSharePopup(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WebSharePopup(Context context, boolean textOnly) {
        super(context);
        this.context = context;
        this.textOnly = textOnly;
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
        if (title != null)
            textTitle.setText(title);
        btnShareChat = getContentView().findViewById(R.id.btn_web_share_chat);
        btnShareChat.setOnClickListener(this);

        btnShareCopy = getContentView().findViewById(R.id.btn_web_share_copy);
        btnShareCopy.setOnClickListener(this);
        btnExit = getContentView().findViewById(R.id.btn_web_share_down);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void showAtBottom(View view) {
        super.showAtBottom(view);
        //弹窗位置设置
        if (url == null)
            throw new NullPointerException("WebSharePopup URL为空");
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void showAtBottom(View view, String title, String url) {

        this.title = title;
        this.url = url;
        textTitle.setText(title);
        showAtBottom(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_web_share_chat:
                if (textOnly) {//发送文本格式
                    ((MainActivity) context).chatFragment.sendMessage(url);
                } else {//发送网页格式
                    String shareContent = "[" + title + "] " + url;
                    LinkEntity entity = new LinkEntity(title, "描述", url);
                    ((MainActivity) context).chatFragment.sendMessage(entity);
                }
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
