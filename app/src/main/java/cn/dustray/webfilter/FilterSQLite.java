package cn.dustray.webfilter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FilterSQLite {
    private SQLiteDatabase db;

    public FilterSQLite() {
        openOrCreateDatabase();
    }

    private void openOrCreateDatabase() {
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.dustray.db/databases/filter.db", null);
    }

    private void createTable(SQLiteDatabase db) {

        //创建表SQL语句
        String stu_table = "create table keywordEntity(objectId text,keyword text,createAt timestamp,updateAt timestamp)";
        //执行SQL语句
        db.execSQL(stu_table);
    }

    /**
     * 从本地获取全部数据
     *
     * @return
     */
    public List<KeywordEntity> getKeywordList() {
        //查询获得游标
        Cursor cursor = db.query("keywordEntity", null, null, null, null, null, null);
        List<KeywordEntity> list = new ArrayList<>();
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                KeywordEntity ke = new KeywordEntity();
                cursor.move(i);
                //获得ID
                ke.setObjectId(cursor.getString(0));
                ke.setKeyword(cursor.getString(1));
                //ke.setCreateAt(cursor.getString(2));
                ke.setUpdatedAt(cursor.getString(3));
                list.add(ke);
            }
        }
        return list;
    }

    /**
     * 转存keyword到SQLite数据库，若已存在则修改
     *
     * @param keywordList
     */
    public void recordKeywordToSqlite(List<KeywordEntity> keywordList,boolean isNeedJudgeExist) {
        for (KeywordEntity ke : keywordList) {
//            if (isNeedJudgeExist && checkIfKeywordExist(ke.getKeyword())) {
//                updateKeyword(ke.getObjectId(), ke.getKeyword());
            if (isNeedJudgeExist && ke.getState() == 0) {
                deleteKeyword(ke.getObjectId());//删除
            } else {
                ContentValues cValue = new ContentValues();
                //添加用户名
                cValue.put("objectId", ke.getObjectId());
                cValue.put("keyword", ke.getKeyword());
                //cValue.put("createAt", ke.getCreatedAt());
                cValue.put("updateAt", ke.getUpdatedAt());
                db.insert("keywordEntity", null, cValue);
//          db.execSQL("insert into keywordEntity values('" + ke.getKeyword() + "')");
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
        String sql = "delete from keywordEntity where objectId = " + id;
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
}
