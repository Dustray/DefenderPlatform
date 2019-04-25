package cn.dustray.webfilter;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.dustray.utils.PreferenceHelper;

public class FilterHelper {

    /**
     * 从bmob获取全部keyword
     *
     * @param context
     */
    public void getAllKeywordFromBmob(final Context context) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BmobQuery<KeywordEntity> query = new BmobQuery<>();
        query.addWhereNotEqualTo("state", "0");
        query.findObjects(new FindListener<KeywordEntity>() {
            @Override
            public void done(List<KeywordEntity> object, BmobException e) {
                if (e == null) {
                    // 同步
                    if (object.size() == 0) return;
                    FilterSQLite fs = new FilterSQLite();
                    fs.recordKeywordToSqlite(object, false);
                    new PreferenceHelper(context).setLastUpdateDate(sdf.format(new Date()));//设置本次更新日期
                } else {
                    // ...
                }
            }
        });
    }

    /**
     * 根据update时间查询、同步
     */
    public void updateKeywordFromBmob(final Context context) {

        String createdAt = new PreferenceHelper(context).getLastUpdateDate();//获取上次更新日期"2018-11-23 10:30:00"
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = null;
        try {
            createdAtDate = sdf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        BmobQuery<KeywordEntity> query = new BmobQuery<>();
        query.addWhereGreaterThan("updatedAt", bmobCreatedAtDate);
        query.findObjects(new FindListener<KeywordEntity>() {
            @Override
            public void done(List<KeywordEntity> object, BmobException e) {
                if (e == null) {
                    // 同步
                    if (object.size() == 0) return;
                    FilterSQLite fs = new FilterSQLite();
                    fs.recordKeywordToSqlite(object, true);
                    new PreferenceHelper(context).setLastUpdateDate(sdf.format(new Date()));//设置本次更新日期
                } else {
                    // ...

                }
            }
        });
    }

    /**
     * 删除keyword
     */
    public void deleteKeyword(final String id) {
        final KeywordEntity ke = new KeywordEntity();
        ke.setObjectId(id);
        ke.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //  toast("删除成功:"+p2.getUpdatedAt());
                    new FilterSQLite().deleteKeyword(id);
                } else {
                    //toast("删除失败：" + e.getMessage());
                }
            }
        });
    }
    public List<KeywordEntity> getKeywordList(){
        List<KeywordEntity> list = new FilterSQLite().getKeywordList();
        return list;
    }
}
