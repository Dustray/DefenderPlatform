package cn.dustray.webfilter;

import android.content.Context;
import android.util.Log;

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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.dustray.utils.PreferenceHelper;
import cn.dustray.utils.xToast;

public class FilterHelper {

    private OnSyncListener listener;

    public void setOnSyncListener(OnSyncListener listener) {
        this.listener = listener;
    }

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
                    fs.close();
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

        final String createdAt = new PreferenceHelper(context).getLastUpdateDate();//获取上次更新日期"2018-11-23 10:30:00"
        Log.i("filter", "保存的时间" + createdAt);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = null;
        try {
            createdAtDate = sdf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        BmobQuery<KeywordEntity> query = new BmobQuery<>();
        query.addWhereGreaterThan("updatedAt", bmobCreatedAtDate);
        query.findObjects(new FindListener<KeywordEntity>() {
            @Override
            public void done(List<KeywordEntity> object, BmobException e) {
                if (e == null) {
                    Log.i("filter", "从Bmob获取：" + object.size() + "个");
                    // 同步
                    if (object.size() == 0) return;
                    FilterSQLite fs = new FilterSQLite();
                    Log.i("filter", "----------第二步，转存Bmob至sqlite--------------");
                    fs.recordKeywordToSqlite(object, true);
                    Log.i("filter", "比较" + bmobCreatedAtDate.getDate() + "///" + object.get(0).getUpdatedAt());
                    Log.i("filter", "当前的时间" + sdf.format(new Date()));
                    new PreferenceHelper(context).setLastUpdateDate(sdf.format(new Date()));//设置本次更新日期
                    Log.i("filter", "新保存的时间" + new PreferenceHelper(context).getLastUpdateDate());
                    Log.i("filter", "----------第三步，重新将sqlite中显示出来---------------");
                    listener.onDownloaded(fs.getKeywordList());

                    fs.close();
                } else {
                    Log.i("filter", "从Bmob获取异常：" + e.toString());
                }
            }
        });
    }

    /**
     * 云端删除keyword
     */
    public void deleteKeyword(final String id) {
        final KeywordEntity ke = new KeywordEntity();
        ke.setObjectId(id);
        ke.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //  toast("删除成功:"+p2.getUpdatedAt());
                    FilterSQLite fs = new FilterSQLite();
                    fs.deleteKeyword(id);
                    fs.close();
                } else {
                    //toast("删除失败：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 从本地获取list
     *
     * @return
     */
    public List<KeywordEntity> getKeywordList() {
        FilterSQLite fs = new FilterSQLite();
        List<KeywordEntity> list = fs.getKeywordList();
        fs.close();
        return list;
    }
    /**
     * 从bmob获取一条keyword
     *
     * @param id
     */
//    public void getAllKeywordFromBmob(String id) {
//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        BmobQuery<KeywordEntity> query = new BmobQuery<>();
//        query.addWhereNotEqualTo("state", "0");
//        query.findObjects(new FindListener<KeywordEntity>() {
//            @Override
//            public void done(List<KeywordEntity> object, BmobException e) {
//                if (e == null) {
//                    // 同步
//                    if (object.size() == 0) return;
//                    FilterSQLite fs = new FilterSQLite();
//                    fs.recordKeywordToSqlite(object, false);
//                    fs.close();
//                    new PreferenceHelper(context).setLastUpdateDate(sdf.format(new Date()));//设置本次更新日期
//                } else {
//                    // ...
//                }
//            }
//        });
//    }

    /**
     * 添加keyword
     *
     * @param keyword
     */
    public void addToDatebase(final Context context, final String keyword) {
        final KeywordEntity ke = new KeywordEntity();
        ke.setKeyword(keyword);
        ke.setState(1);
        ke.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    updateKeywordFromBmob(context);
//                    FilterSQLite fs = new FilterSQLite();
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String date = sdf.format(new Date());
//                    fs.insertKeyword(objectId, keyword, date);
//                    //new PreferenceHelper(context).setLastUpdateDate(date);//设置本次更新日期
////                    Log.i("filter", "s" + fs.getKeywordList().size());
//                    listener.onInsertSuccess(fs.getKeywordList());
//                    fs.close();
                    //toast("添加数据成功，返回objectId为："+objectId);

                } else {
                    Log.i("filter", "add失败" + e.toString());
                    //toast("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    public interface OnSyncListener {
        void onDownloaded(List<KeywordEntity> list);
    }
}
