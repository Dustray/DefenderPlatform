package cn.dustray.defenderplatform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.dustray.control.xRecycleViewDivider;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.entity.UserEntity;
import cn.dustray.utils.xToast;
import cn.dustray.webfilter.FilterHelper;
import cn.dustray.webfilter.KeywordEntity;
import cn.dustray.webfilter.KeywordListAdapter;

public class KeywordListActivity extends AppCompatActivity implements View.OnClickListener, FilterHelper.OnSyncListener {

    private RecyclerView rvKeyword;
    private EditText inputAdd;
    private Button btnAdd;
    private KeywordListAdapter adapter;
    private FilterHelper filterHelper;
    List<KeywordEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("免屏蔽");
        initView();
        initData();
    }


    private void initView() {
//        for (int i = 0; i < 10; i++) {
//            KeywordEntity ke = new KeywordEntity();
//            ke.setKeyword("ss");
//            list.add(ke);


        rvKeyword = findViewById(R.id.rv_keyword);
        inputAdd = findViewById(R.id.input_add);

        if (!BmobUser.isLogin()) {
            inputAdd.setEnabled(false);
        }
        btnAdd = findViewById(R.id.btn_add);
        inputAdd.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvKeyword.setLayoutManager(layoutManager);
        rvKeyword.addItemDecoration(new xRecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 0, getResources().getColor(R.color.black_overlay)));
    }

    private void initData() {

        filterHelper = new FilterHelper();
        filterHelper.setOnSyncListener(this);

        Log.i("filter", "----------第一步，先将sqlite存的展示出来--------------");
        list = filterHelper.getKeywordList();
        Log.i("filter", "打开页面，从SQLite中取出" + list.size() + "个数据");
        adapter = new KeywordListAdapter(this, list);
        adapter.setItemClickListener(new KeywordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                xToast.toast(KeywordListActivity.this, "点击");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //xToast.toast(KeywordListActivity.this, list.size()+"正在删除---"+list.get(position).getUpdatedAt());
                if (!UserEntity.isLogin()) {
                    xToast.toast(KeywordListActivity.this, "请先登录");
                    return;
                }
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d1 = null, d2 = null;
                try {
                    d1 = sDateFormat.parse(sDateFormat.format(new Date()));
                    d2 = sDateFormat.parse(list.get(position).getUpdatedAt());
                    long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别

                    if (BmobUser.getCurrentUser(UserEntity.class).isGuardian() || diff < 300000) {//用户类型为监护人或时长小于300秒
                        filterHelper.deleteKeyword(list.get(position).getObjectId(), KeywordListActivity.this, position);
                        xToast.toast(KeywordListActivity.this, "删除成功");
                    } else {
                        xToast.toast(KeywordListActivity.this, "时间超过5分钟，不能删除");

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    xToast.toast(KeywordListActivity.this, "删除异常" + e.toString());
                }
                ////-------------------------------------
            }
        });
        filterHelper.updateKeywordFromBmob(this);
        rvKeyword.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if(!inputAdd.getText().equals("")) {
                    filterHelper.addToDatebase(this, inputAdd.getText().toString());
                    inputAdd.setText("");
                }
                break;
        }
    }


    @Override
    public void InsertSuccess(List<KeywordEntity> list) {
        Log.i("filter", "从Bmob取回后，从SQLite中取出" + list.size() + "个数据");
        adapter.insert(list);
        xToast.toast(this, "已同步");
    }


    @Override
    public void onDeleteSuccess(int itemPosition) {
        Log.i("filter", "从Bmob取回后，从SQLite中取出" + list.size() + "个数据");
        adapter.delete(itemPosition);
        xToast.toast(this, "删除成功");
    }
}
