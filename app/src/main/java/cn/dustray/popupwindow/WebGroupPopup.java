package cn.dustray.popupwindow;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import cn.dustray.browser.WebGroupListAdapter;
import cn.dustray.control.xWebPopupWindow;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.browser.WebTabFragment;

public class WebGroupPopup extends xWebPopupWindow implements View.OnClickListener {

    private Context context;
    private RecyclerView webGroupList;
    private WebGroupListAdapter webGroupAdapter;
    private List<WebTabFragment> list;
    private ImageButton btnWebAdd, btnWebCloseAll, btnExit;

    public WebGroupPopup(Context context, List<WebTabFragment> list) {
        super(context);
        this.context = context;
        this.list = list;
        init();

        // list= webGroupAdapter.readSavedStateFromDisk();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_web_group, null);

        setContentView(view);
        initRecycle();
        initButton();
    }

    private void initButton() {
        btnWebAdd = getContentView().findViewById(R.id.btn_web_add);
        btnWebAdd.setOnClickListener(this);
        btnWebCloseAll = getContentView().findViewById(R.id.btn_web_closeall);
        btnWebCloseAll.setOnClickListener(this);
        btnExit = getContentView().findViewById(R.id.btn_web_group_down);
        btnExit.setOnClickListener(this);
    }

    private void initRecycle() {
        webGroupList = getContentView().findViewById(R.id.web_group_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setStackFromEnd(true);
        webGroupList.setLayoutManager(layoutManager);
        //new LinearSnapHelper().attachToRecyclerView(webGroupList);
        webGroupAdapter = new WebGroupListAdapter(context, list, this);
        webGroupList.setAdapter(webGroupAdapter);

    }

    public void showAtBottom(View view) {        //弹窗位置设置
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_web_add:
                ((MainActivity) context).browserFragment.createNewFragment();
                //webGroupAdapter.saveToFile();
                break;
            case R.id.btn_web_closeall:
                webGroupAdapter.removeAllItem();
                break;
            case R.id.btn_web_group_down:
                break;
        }
        dismiss();
    }
}
