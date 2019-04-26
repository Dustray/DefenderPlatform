package cn.dustray.defenderplatform;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import cn.dustray.control.xRecycleViewDivider;
import cn.dustray.entity.ChatRecordEntity;
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
        btnAdd = findViewById(R.id.btn_add);
        inputAdd.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvKeyword.setLayoutManager(layoutManager);
        rvKeyword.addItemDecoration(new xRecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 3, getResources().getColor(R.color.black_overlay)));
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
            public void onItemClick(View view, int postion) {
                xToast.toast(KeywordListActivity.this, "点击");
            }

            @Override
            public void onItemLongClick(View view, int postion) {
                xToast.toast(KeywordListActivity.this, "长按");

            }
        });
        filterHelper.updateKeywordFromBmob(this);
        rvKeyword.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                filterHelper.addToDatebase(this, inputAdd.getText().toString());
        }
    }


    @Override
    public void onDownloaded(List<KeywordEntity> list) {
        Log.i("filter", "从Bmob取回后，从SQLite中取出" + list.size() + "个数据");
        adapter.sync(list);
        xToast.toast(this, "同步完成");
    }

    @Override
    public void onInsertSuccess(List<KeywordEntity> list) {
        Log.i("filter", "插入成功后，从SQLite中取出" + list.size() + "个数据");
        adapter.sync(list);
        xToast.toast(this, "添加成功");
    }
}
