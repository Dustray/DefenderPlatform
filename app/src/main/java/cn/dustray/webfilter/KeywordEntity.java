package cn.dustray.webfilter;

import cn.bmob.v3.BmobObject;

/**
 * Created by Dustray on 2016/11/27 0027.
 */

public class KeywordEntity extends BmobObject {
    private String keyword;
    private int state;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
