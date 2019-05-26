package cn.dustray.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public SqliteUtil(Activity activity) {
        this.mContext = activity;
        /*创建私有数据库*/
        db = activity.openOrCreateDatabase("/data/data/cn.dustray.defenderplatform/databases/browser.db", activity.MODE_PRIVATE, null);

        /*创建表，integer为整形，text为字符串，primary key代表主键，autoincrement代表自增，not null代表不为空*/
        db.execSQL("create table if not exists allhistory(_id integer primary key autoincrement, title text not null, url text not null)");//所有历史表
    }

    /**
     * 向sqlite中添加信息
     */
    public void recordHistoryToSqlite(String nowTitle, String nowUrl) {

        if (nowTitle != null && !nowUrl.equals("")) {
            /*添加方式一*/
            //db.execSQL("insert into temphistory(title,url)values('"+nowTitle+"','"+nowUrl+"')");

            /*添加方式二*/
            ContentValues values = new ContentValues();
            values.put("title", nowTitle);
            values.put("url", nowUrl);
            db.insert("allhistory", null, values);
            values.clear();//清空values值，便于后边继续使用该变量
        }


    }

    public void close() {
        if (db != null) db.close();
    }

}
