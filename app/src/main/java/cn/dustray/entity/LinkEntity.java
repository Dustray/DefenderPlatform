package cn.dustray.entity;

public class LinkEntity {
    private String linkTitle;
    private String linkDescription;
    private String linkUrl;

    public LinkEntity(String linkTitle, String linkDescription, String linkUrl) {
        this.linkTitle = linkTitle;
        this.linkDescription = linkDescription;
        this.linkUrl = linkUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
}

