package cn.dustray.webfilter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FilterSQLite {
    private SQLiteDatabase db;

    public FilterSQLite() {
        openOrCreateDatabase();
    }

    private void openOrCreateDatabase() {
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/cn.dustray.defenderplatform/databases/browser.db", null);
        createTable();
    }

    private void createTable() {

        //创建表SQL语句
        String stu_table = "create table if not exists keywordEntity(objectId text,keyword text,createAt datetime,updateAt datetime)";
        //执行SQL语句
        db.execSQL(stu_table);
    }

    /**
     * 从本地获取全部数据
     *
     * @return
     */
    public List<KeywordEntity> getKeywordList() {
        List<KeywordEntity> list = new ArrayList<>();

        Cursor cursor = null;
        try {
            //查询获得游标
            cursor = db.query("keywordEntity", null, null, null, null, null, "updateAt desc");

            //判断游标是否为空
            if (cursor != null) {
                cursor.moveToFirst();
                //遍历游标
                do{
//                for (int i = 0; i < cursor.getCount(); i++) {
                    KeywordEntity ke = new KeywordEntity();
                    Log.i("filter", "来自sqlite的getKeyword ...keyword:" + cursor.getString(1) + "总共：" + cursor.getCount());
                    //cursor.move(i);
                    //获得ID
                    ke.setObjectId(cursor.getString(0));
                    ke.setKeyword(cursor.getString(1));
                    //ke.setCreateAt(cursor.getString(2));
                    ke.setUpdatedAt(cursor.getString(3));
                    list.add(ke);
                   // cursor.moveToNext();
                }while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("filter", "Cursor错误：" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 转存keyword到SQLite数据库，若已存在则修改
     *
     * @param keywordList
     */
    public void recordKeywordToSqlite(List<KeywordEntity> keywordList,
                                      boolean isNeedJudgeExist) {
        for (KeywordEntity ke : keywordList) {
//            if (isNeedJudgeExist && checkIfKeywordExist(ke.getKeyword())) {
//                updateKeyword(ke.getObjectId(), ke.getKeyword());

            Log.i("filter", "来自Bmob的keyword：" + ke.getKeyword());
            if (isNeedJudgeExist && ke.getState() == -1) {

                Log.i("filter", "已存在，删除现有：" + ke.getKeyword());
                deleteKeyword(ke.getObjectId());//删除
            } else {
                ContentValues cValue = new ContentValues();
                try {
                    //添加用户名
                    cValue.put("objectId", ke.getObjectId());
                    cValue.put("keyword", ke.getKeyword());
                    //cValue.put("createAt", ke.getCreatedAt());
                    cValue.put("updateAt", ke.getUpdatedAt());
                    db.insert("keywordEntity", null, cValue);
                } catch (Exception e) {

                    Log.i("filter", "设置到SQLite中异常：" + e.toString());
                }
                //db.execSQL("insert into keywordEntity(objectId,keyword,updateAt) values('" +ke.getObjectId()+"','"+ ke.getKeyword() +"',datetime('"+ke.getUpdatedAt()+ "'))");
            }
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param keyword
     */
    private void updateKeyword(String id, String keyword) {
//实例化内容值
        ContentValues values = new ContentValues();
//在values中添加内容
        values.put("keyword", keyword);
//修改条件
        String whereClause = "id=?";
//修改添加参数
        String[] whereArgs = {id};
//修改
        db.update("keywordEntity", values, whereClause, whereArgs);
    }

    /**
     * 删除行
     *
     * @param id
     */
    public void deleteKeyword(String id) {
        //删除SQL语句
        String sql = "delete from keywordEntity where objectId = '" + id + "'";
        //执行SQL语句
        db.execSQL(sql);
    }

    /**
     * 查询keyword是否存在
     *
     * @return
     */
    public boolean checkIfKeywordExist(String keyword) {
        //查询获得游标
        Cursor cursor = db.query("keywordEntity", null, "keyword=" + keyword, null, null, null, null, null);
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }
}
