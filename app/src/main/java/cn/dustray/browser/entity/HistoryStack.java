package cn.dustray.browser.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.control.xWebView;

public class HistoryStack {
    private List<XWebViewHistoryEntity> list = new ArrayList<>();
    private int StackPosition = 0;//从0开始为第一页
    private Bitmap capture;
    private static final int BACK_PAGE = -1;
    private static final int NOW_PAGE = 0;
    private static final int FOREWARD_PAGE = 1;

    public boolean isStackBottom() {
        if (StackPosition == 0) return true;
        else return false;
    }

    public boolean isStackTop() {
        if (StackPosition == size() - 1) return true;
        else return false;
    }

    /**
     * 获取历史栈长度（页面数）
     *
     * @return
     */
    public int size() {
        return list.size();
    }

    /**
     * 入栈
     *
     * @param url
     */
    public void push(String url) {
        clearForward();
        XWebViewHistoryEntity entity = new XWebViewHistoryEntity(url);
        list.add(entity);
        StackPosition = size() - 1;
    }

    /**
     * 获取上一页Url
     *
     * @return
     * @throws NullPointerException
     */
    public String getBackUrl() throws NullPointerException {
        if (StackPosition == 0) throw new NullPointerException("栈已到达底部");
        StackPosition--;
        return list.get(StackPosition).getUrl();
    }

    /**
     * 获取下一页Url
     *
     * @return
     * @throws NullPointerException
     */
    public String getForwardUrl() throws NullPointerException {
        if (StackPosition == size() - 1) throw new NullPointerException("栈已到达顶部");
        StackPosition++;
        return list.get(StackPosition).getUrl();
    }

    /**
     * 清除当前页面以后的页面
     */
    private void clearForward() {
        if (StackPosition == size() - 1) return;//栈顶
        for (int i = StackPosition + 1; i < size(); i++) {
            list.remove(i);
        }
    }

    /**
     * 更新当前页面滚动位置
     *
     * @param PagePosition
     */
    public void updatePagePosition(int PagePosition) {
        list.get(StackPosition).position = PagePosition;
    }

    /**
     * 获取当前页面滚动位置
     */
    public int getPagePosition() {
        return list.get(StackPosition).position;
    }

    /**
     * 获取当前页面在栈中的位置
     *
     * @return
     */
    public int getStackPosition() {
        return StackPosition;
    }

    /**
     * 设置当前页面在栈中的位置
     *
     * @param stackPosition
     */
    public void setStackPosition(int stackPosition) {
        StackPosition = stackPosition;
    }

    /**
     * 判断网址是否相同
     *
     * @param nowUrl
     * @param whichPage
     * @return
     */
    public boolean isUrlSameAsHistory(String nowUrl, int whichPage) {

        if (list.get(StackPosition + whichPage).getUrl().equals(nowUrl)) return true;
        else return false;
    }

    /**
     * 获取快照
     *
     * @return
     */
    public Bitmap getCapture() {
        return capture;
    }

    /**
     * 升级快照
     *
     * @param capture
     */
    public void updateCapture(Bitmap capture) {
        this.capture = capture;
    }

    /**
     * 销毁栈
     */
    public void destroy() {
        list.clear();
        list = null;
    }
    class XWebViewHistoryEntity {
        private String Url;
        private int position;// webview.getScrollY()/newScale*oldScale

        public XWebViewHistoryEntity(String url) {
            Url = url;
            this.position = 0;
        }

        public XWebViewHistoryEntity(String url, int position) {
            Url = url;
            this.position = position;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }


    }
}
