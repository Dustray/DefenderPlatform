package cn.dustray.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.dustray.webfilter.KeywordEntity;

public class SqliteUtil {
    private SQLiteDatabase db;
    private List<KeywordEntity> keywordList = new ArrayList<KeywordEntity>();
    private Context mContext;

    public SqliteUtil(Context context, SQLiteDatabase db) {
        this.mContext = context;
        this.db = db;
    }

    /**
     * 创建数据库和表
     */
    private void initializeDatabase(Activity activity) {
        /*创建私有数据库*/
        db = activity.openOrCreateDatabase("browser.db", activity.MODE_PRIVATE, null);

        /*创建表，integer为整形，text为字符串，primary key代表主键，autoincrement代表自增，not null代表不为空*/
        db.execSQL("create table if not exists temphistory(_id integer primary key autoincrement, title text not null, url text not null)");//临时历史表
        db.execSQL("create table if not exists allhistory(_id integer primary key autoincrement, title text not null, url text not null)");//所有历史表
        db.execSQL("create table if not exists allkeyword(_id integer primary key autoincrement, keyword text not null)");//所有过滤关键字表
        //删除临时历史纪录表重新计算
        db.delete("temphistory", null, null);
        getKeywordFromNet();

    }

    /**
     * 转存keyword到数据库
     */
    public void recordKeywordToSqlite() {
        for (KeywordEntity ke : keywordList) {
            db.execSQL("insert into allkeyword(keyword)values('" + ke.getKeyword() + "')");
        }
    }

    /**
     * 从bmob获取keyword
     */
    public void getKeywordFromNet() {
        BmobQuery<KeywordEntity> query = new BmobQuery<KeywordEntity>();
        query.setLimit(100);
        //执行查询方法
        query.order("-createdAt");
        query.findObjects(new FindListener<KeywordEntity>() {
            @Override
            public void done(List<KeywordEntity> object, BmobException e) {
                if (e == null) {
                    keywordList = object;
                    db.delete("allkeyword", null, null);
                    recordKeywordToSqlite();
                    xToast.toast(mContext, "更新过滤器成功：共" + object.size() + "条数据。");
                } else {
                    //Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    xToast.toast(mContext, "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
