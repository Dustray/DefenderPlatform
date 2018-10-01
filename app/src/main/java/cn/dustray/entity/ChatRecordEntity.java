package cn.dustray.entity;

public class ChatRecordEntity {

    public static final int TYPE_RECEIVED = 1;
    public static final int TYPE_SENT = 2;

    String chatContent;
    int messageType;

    public ChatRecordEntity(String chatContent, int messageType) {
        this.chatContent = chatContent;
        this.messageType = messageType;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
