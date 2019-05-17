package cn.dustray.chat;

import android.os.Looper;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import cn.dustray.defenderplatform.LoginActivity;

public class ChatHelper {
    public void sendMessageToEase( String content,  String toChatUsername) {

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMClient.getInstance().chatManager().sendMessage(message);

    }

    public void sendLinkMessageToEase( String title,String description, String LinkUrl,  String toChatUsername) {
        EMMessage message = EMMessage.createTxtSendMessage(LinkUrl, toChatUsername);
        message.setAttribute("isLinkUrl", true);
        message.setAttribute("linkTitle", title);
        message.setAttribute("linkDescription", description);
        EMClient.getInstance().chatManager().sendMessage(message);


    }
}
