package cn.dustray.entity;

import android.content.Context;

public class ChatRecordEntity {
    private Context context;
    public static final int TRANSMIT_TYPE_RECEIVED = 1;
    public static final int TRANSMIT_TYPE_SENT = 2;
    public static final int MESSAGE_TYPE_TEXT = 1001;
    public static final int MESSAGE_TYPE_IMAGE = 1002;
    public static final int MESSAGE_TYPE_LINK = 1003;

    private int transmitType;
    private int messageType;

    private String chatContent;
    private LinkEntity linkEntity;

    public ChatRecordEntity(Context context, String chatContent, int transmitType, int messageType) {
        this.chatContent = chatContent;
        this.transmitType = transmitType;
        this.context = context;
        this.messageType = messageType;
        //messageType = MESSAGE_TYPE_TEXT;
    }

    public ChatRecordEntity(Context context, LinkEntity linkEntity, int transmitType) {
        this.linkEntity = linkEntity;
        this.transmitType = transmitType;
        this.context = context;
        this.messageType = MESSAGE_TYPE_LINK;
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

    public LinkEntity getLinkEntity() {
        return linkEntity;
    }
}
