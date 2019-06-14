package cn.dustray.webfilter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.dustray.utils.xToast;
import cn.dustray.webfilter.KeywordEntity;


/**
 * Created by Dustray on 2016/11/8 0008.
 */

public class FilterUtil implements FilterHelper.OnSyncListener {
    private List<KeywordEntity> keywordList = new ArrayList<>();
    private SQLiteDatabase db;
    private Context mContext;
    private String theKey = "";

    public FilterUtil(Context mContext) {
        this.mContext = mContext;
        initData();
    }

    public FilterUtil(Context mContext, SQLiteDatabase db) {
        this.mContext = mContext;
        this.db = db;
        initData();
    }

    //网页链接过滤
    public boolean filterWebsite(String url) {
        if (url == null) {
            return true;
        }
        //String keyword[] = {"weibo", "tieba", "taobao", "tmail", "mbd.baidu", "ithome", "video", "image.baidu", "news", "qq.com", "sohu.com", "huanqiu.com", "xinhuanet.com", "3g.163.com", "s.pae.baidu", "chinanews"};
        //initData();
        //getKeyword();
        PatternMatching patternMatching = new PatternMatching();
        for (KeywordEntity ke : keywordList) {

            //xToast.toast(mContext,"正在匹配"+ke.getKeyword());
            //if (url.contains(ke.getKeyword())) {
            if(patternMatching.isBelongToSource(ke.getKeyword(),url)){
                theKey = ke.getKeyword();
                return false;
            }
        }
//        for (int i = 0; i < keyword.length; i++) {
//            if (url.contains(keyword[i])) {
//                theKey = keyword[i];
//                result = false;
//            }
//        }
        return true;
    }

    //获取拦截关键字
    public String getFilterKey() {
        return theKey;
    }

    private void getKeyword() {
        /*查询所有信息*/
        Cursor c = db.rawQuery("select * from allkeyword", null);
        if (c != null) {
            while (c.moveToNext()) {
                KeywordEntity ke = new KeywordEntity();
                ke.setKeyword(c.getString(c.getColumnIndex("keyword")));
                keywordList.add(ke);
            }
            c.close();//释放游标
        }
    }

    //页内文本过滤探测
    public boolean filterKeyWord(String html) {
        boolean result = true, isDouble = false;

        String keyword[] = {"新闻网", "朋友圈", "日报", "记者", "日讯", "近日", "媒体", "报道", "依法", "调查", "摄影师", "网友", "爆料", "微博", "全国", "新华", "央视"};
        for (int i = 0; i < keyword.length; i++) {
            if (html.contains(keyword[i]) && isDouble) {
                theKey += "/" + keyword[i];
                result = false;
            } else if (html.contains(keyword[i])) {
                theKey = keyword[i];
                isDouble = true;
            }
        }
        return result;
    }

    public void refleshData() {
        initData();
    }

    private void initData() {
        FilterHelper fh = new FilterHelper();
        keywordList = fh.getKeywordList();
//        xToast.toast(mContext,"已同步"+keywordList.size());
        fh.setOnSyncListener(this);
        fh.updateKeywordFromBmob(mContext);

    }

    @Override
    public void InsertSuccess(List<KeywordEntity> list) {
        keywordList = list;
    }



    @Override
    public void onDeleteSuccess(int itemPosition) {

    }
    public void close() {
        if (db != null) db.close();
    }
}
