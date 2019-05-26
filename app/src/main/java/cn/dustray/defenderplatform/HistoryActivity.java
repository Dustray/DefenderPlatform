package cn.dustray.defenderplatform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.adapter.HistoryAdapter;
import cn.dustray.entity.LinkEntity;
import cn.dustray.utils.xToast;

public class HistoryActivity extends AppCompatActivity {
    private List<LinkEntity> historyList = new ArrayList<>();
    private RecyclerView rvHistory;
    private HistoryAdapter historyAdapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("历史纪录");
        db = openOrCreateDatabase("browser.db", MODE_PRIVATE, null);

        rvHistory = findViewById(R.id.rv_history);
        //获取历史纪录操作
        initData();
        initRecyclerView();
    }

    /**
     * 初始化/刷新Adapter，初始化/刷新RecyclerView
     */
    private void initRecyclerView() {
        historyAdapter = new HistoryAdapter(this, historyList);
        //item长按/短按事件
        historyAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(int position) {
                xToast.toast(HistoryActivity.this, "长按还没啥用。。。");
            }

            @Override
            public void onClick(int position) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                String passString = historyList.get(position).getLinkUrl();
                intent.putExtra("resultUrl", passString);
                setResult(RESULT_OK, intent);
                finish();
                finish();
            }
        });

        //MyToast.toast(getActivity(), "查询成功：共" + historyList.size() + "条数据。");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        rvHistory.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        rvHistory.setAdapter(historyAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getHistory();
    }

    /**
     * 获取历史纪录
     */
    private void getHistory() {
        /*查询所有信息*/
        Cursor c = db.rawQuery("select * from allhistory order by _id desc", null);

        if (c != null) {
            while (c.moveToNext()) {
                LinkEntity ue = new LinkEntity(c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("url")));
                historyList.add(ue);
            }
            c.close();//释放游标
        }

    }
    public void deleteHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle("警告");
        builder.setMessage("确认清空历史纪录？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete("allhistory", null, null);
                xToast.toast(HistoryActivity.this, "清空历史纪录成功");
                finish();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_history_delete:
                deleteHistoryDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);

        return true;
    }
}
