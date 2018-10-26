package cn.dustray.entity;

import android.content.Context;
public class ChatRecordEntity {
    private Context context;
    public static final int TRANSMIT_TYPE_RECEIVED = 1;
    public static final int TRANSMIT_TYPE_SENT = 2;
    public static final int MESSAGE_TYPE_TEXT = 1001;
    public static final int MESSAGE_TYPE_IMAGE = 1002;

    private int transmitType;
    private int messageType;

    private String chatContent;

    public ChatRecordEntity(Context context, String chatContent, int transmitType,int messageType) {
        this.chatContent = chatContent;
        this.transmitType = transmitType;
        this.context = context;
        this.messageType=messageType;
        //messageType = MESSAGE_TYPE_TEXT;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public int getTransmitType() {
        return transmitType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
