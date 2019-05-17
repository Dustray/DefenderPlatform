package cn.dustray.defenderplatform;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.dustray.entity.UserEntity;
import cn.dustray.utils.Alert;
import cn.dustray.utils.FilterPreferenceHelper;
import cn.dustray.utils.xToast;
import cn.dustray.webfilter.NoFilterEntity;

public class ShieldingActivity extends AppCompatActivity implements View.OnClickListener {

    public static String SHIELDING_STATE = "正在屏蔽网站";
    public static String UNSHIELDING_STATE = "正在免屏蔽";

    private Button nofilterTimeBtn;
    private FloatingActionButton startNofilterBtn;
    private TextView nofilterTimeText, tvNoFilterState;
    private TextInputEditText applyPwdEdt, applyTimeEdt;

    private TextView tvRemainTime;
    private FilterPreferenceHelper spHelper;

    private boolean isSubmit = false, isApply = false;
    private PopupWindow mPopUpWindow;
    private int userType = 0;
    private int waitingForApplyTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shielding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("免屏蔽");

        spHelper = new FilterPreferenceHelper(this);
        initView();
        getAllNoFilterTimeFromNet();
    }

    private void initView() {
        tvRemainTime = findViewById(R.id.tv_remain_time);
        startNofilterBtn = (FloatingActionButton) findViewById(R.id.fab);
        startNofilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (spHelper.getNoFilterTime() == 0) {//时长用尽
                    xToast.toast(ShieldingActivity.this, "您的免屏蔽时长已用尽");
                    startNofilterBtn.setBackgroundResource(R.color.colorDanger);
                    //startNofilterBtn.setText("时长已用尽");
                    getSupportActionBar().setTitle("免屏蔽时长已用尽");
                    spHelper.setIsNoFilter(false);
                } else if (!spHelper.getIsNoFilter()) {//免屏蔽开启
                    xToast.toast(ShieldingActivity.this, "免屏蔽已开启");
                    startNofilterBtn.setBackgroundResource(R.color.colorSafe);
                    //startNofilterBtn.setText("暂停");
                    getSupportActionBar().setTitle("免屏蔽已开启");
                    spHelper.setIsNoFilter(true);
                } else if (spHelper.getIsNoFilter()) {//关闭
                    xToast.toast(ShieldingActivity.this, "免屏蔽已关闭");
                    startNofilterBtn.setBackgroundResource(R.color.colorPrimary);
                    //startNofilterBtn.setText("开始");
                    getSupportActionBar().setTitle("免屏蔽已关闭");
                    spHelper.setIsNoFilter(false);
                }


            }
        });
    }


    /**
     * 查询多条Nofilter Time
     */

    private void getAllNoFilterTimeFromNet() {
        BmobQuery<NoFilterEntity> query = new BmobQuery<NoFilterEntity>();
        UserEntity userEntity = BmobUser.getCurrentUser(UserEntity.class);
        if (userEntity.isGuardian())
            query.addWhereEqualTo("userEntity", userEntity);
        else
            query.addWhereEqualTo("userEntity", userEntity.getGuardianUserEntity());
        //执行查询方法
        query.findObjects(new FindListener<NoFilterEntity>() {
            @Override
            public void done(List<NoFilterEntity> object, BmobException e) {
                if (e == null) {
                    if (isSubmit && object.size() != 0) {
                        // MyToast.toast(ApplyNoFilterActivity.this, "成功");
                        updateNoFilterTimeToNet();
                        isSubmit = false;
                    } else if (isSubmit && object.size() == 0) {
                        submitNoFilterTimeToNet();
                        isSubmit = false;
                    }
                    if (object.size() == 0) {

                    } else {
                        NoFilterEntity noFilter = object.get(0);
                        //获得playerName的信息
                        spHelper.setNoFilterID(noFilter.getObjectId());
                        tvRemainTime.setText("当前剩余时长：" + noFilter.getNoFilterTime() + "分钟");
                        spHelper.setNoFilterTime(noFilter.getNoFilterTime() * 60 * 1000);
                        //spHelper.setNoFilterTime(noFilterTime*1000);

                        waitingForApplyTime = noFilter.getWaitingForApplyTime();

                        judgeIsNoFilter();
                    }
                } else {
                    if (isSubmit) {
                        submitNoFilterTimeToNet();
                        isSubmit = false;
                    }
                    xToast.toast(ShieldingActivity.this, "失败1：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 提交time到Bmob
     */
    private void submitNoFilterTimeToNet() {
        NoFilterEntity nfe = new NoFilterEntity();
        nfe.setNoFilterTime(Integer.parseInt(applyTimeEdt.getText().toString()));
        if (isApply) {
            nfe.setWaitingForApplyTime(Integer.parseInt(applyTimeEdt.getText().toString()));
        } else {
            nfe.setWaitingForApplyTime(0);
        }
        nfe.setUserEntity(BmobUser.getCurrentUser(UserEntity.class));
        nfe.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    //spHelper.setNoFilterID(objectId);////？？？？？？？？
                    getAllNoFilterTimeFromNet();
                    //getNoFilterTimeFromNet();
                    xToast.toast(ShieldingActivity.this, "添加数据成功");
                } else {
                    xToast.toast(ShieldingActivity.this, "创建数据失败：" + e.getMessage());
                }
            }
        });

    }

    /**
     * 修改time到Bmob
     */
    private void updateNoFilterTimeToNet() {
        NoFilterEntity nfe = new NoFilterEntity();
        nfe.setNoFilterTime(Integer.parseInt(applyTimeEdt.getText().toString()));
        if (isApply) {
            nfe.setWaitingForApplyTime(Integer.parseInt(applyTimeEdt.getText().toString()));
        } else {
            nfe.setWaitingForApplyTime(0);
        }
        nfe.update(spHelper.getNoFilterID(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    getAllNoFilterTimeFromNet();
                    xToast.toast(ShieldingActivity.this, "已提交");
                    if (mPopUpWindow != null)
                        mPopUpWindow.dismiss();
                } else {
                    submitNoFilterTimeToNet();
                    xToast.toast(ShieldingActivity.this, "修改数据失败：" + e.getMessage());
                }
            }

        });
    }


    /**
     * 修改applyTtime到Bmob
     */
    private void updateApplyTimeToNet(int applyTime) {
        NoFilterEntity nfe = new NoFilterEntity();
        //nfe.setNoFilterTime(Integer.parseInt(applyTimeEdt.getText().toString()));
        nfe.setWaitingForApplyTime(applyTime);
        nfe.update(spHelper.getNoFilterID(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    getAllNoFilterTimeFromNet();//查询申请
                    xToast.toast(ShieldingActivity.this, "提交成功");

                } else {

                    xToast.toast(ShieldingActivity.this, "修改数据失败:" + e.getMessage());
                }
            }
        });
    }

    public void judgeIsNoFilter() {
        if (spHelper.getNoFilterTime() == 0) {//时长用尽
            startNofilterBtn.setBackgroundResource(R.color.colorDanger);
            //startNofilterBtn.setText("时长已用尽");
            getSupportActionBar().setTitle("免屏蔽时长已用尽");
            spHelper.setIsNoFilter(false);
        } else if (spHelper.getIsNoFilter()) {//免屏蔽状态已开启
            startNofilterBtn.setBackgroundResource(R.color.colorSafe);
            //startNofilterBtn.setText("暂停");
            getSupportActionBar().setTitle("免屏蔽已开启");
            spHelper.setIsNoFilter(true);
        } else if (!spHelper.getIsNoFilter()) {//免屏蔽状态已关闭
            startNofilterBtn.setBackgroundResource(R.color.colorPrimary);
            //startNofilterBtn.setText("开始");
            getSupportActionBar().setTitle("免屏蔽已关闭");
            spHelper.setIsNoFilter(false);
        }
    }

    public void applyDialog() {


        Alert alert = new Alert(this);
        alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
            @Override
            public void onClickOk() {
                updateApplyTimeToNet(Integer.parseInt(applyTimeEdt.getText().toString()));
                if (mPopUpWindow != null)
                    mPopUpWindow.dismiss();
            }
            @Override
            public void onClickCancel() {
            }
        });
        alert.popupAlert(this.getWindow().getDecorView(), "申请免屏蔽时长将清空当前已有时长，是否继续？", "继续");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.shield_apply) {
            showApplyWindow();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shielding, menu);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nofilter_time:
                if (BmobUser.getCurrentUser(UserEntity.class).isGuardian())
                    if (TextUtils.isEmpty(applyPwdEdt.getText())) {
                        xToast.toast(ShieldingActivity.this, "密码不能为空");
                    } else if (applyPwdEdt.getText().toString().equals(spHelper.getRegisterPassword())) {
                        isSubmit = true;
                        getAllNoFilterTimeFromNet();
                    } else {
                        xToast.toast(ShieldingActivity.this, "密码错误" + applyPwdEdt.getText());
                    }
                else if (BmobUser.getCurrentUser(UserEntity.class).isUnGuardian()) {
                    if (applyTimeEdt.getText().toString().equals("")) {
                        xToast.toast(ShieldingActivity.this, "未执行任何操作");
                    } else if (Integer.parseInt(applyTimeEdt.getText().toString()) == 0) {
                        xToast.toast(ShieldingActivity.this, "未执行任何操作");
                    } else if (isApply) {
                        updateApplyTimeToNet(0);
                        applyTimeEdt.setText("");
                        applyPwdEdt.setText("");
                        //nofilterTimeBtn.setBackgroundResource(R.color.colorSafe);
                        nofilterTimeBtn.setText("确认");
                        isApply = false;
                    } else {
                        //xToast.toast(ShieldingActivity.this, "eee" );
                        applyDialog();
                    }
                }
                break;

        }

    }

    public void showApplyWindow() {
        View mContentView = LayoutInflater.from(this).inflate(R.layout.popup_nofilter_apply, null);

        if (mPopUpWindow == null) {
            mPopUpWindow = new PopupWindow(mContentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            mPopUpWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopUpWindow.setOutsideTouchable(true);
            initPopupView(mContentView);
        }
//显示申请时长详情
        if (waitingForApplyTime > 0) {
            //nofilterTimeBtn.setBackgroundResource(R.color.colorDanger);
            applyTimeEdt.setText("" + waitingForApplyTime);
            applyPwdEdt.setText("");
            if (BmobUser.getCurrentUser(UserEntity.class).isGuardian()) {//监督者
                nofilterTimeBtn.setText("修改");
            } else if (BmobUser.getCurrentUser(UserEntity.class).isUnGuardian()) {//被监督者
                nofilterTimeBtn.setText("取消");
                isApply = true;
            }
        } else {
            //nofilterTimeBtn.setBackgroundResource(R.color.colorSafe);
            nofilterTimeBtn.setText("确定");
            applyTimeEdt.setText("");
            applyPwdEdt.setText("");
            isApply = false;
        }
        mPopUpWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void initPopupView(View mContentView) {
        nofilterTimeBtn = (Button) mContentView.findViewById(R.id.btn_nofilter_time);
        nofilterTimeBtn.setOnClickListener(this);
        applyPwdEdt = (TextInputEditText) mContentView.findViewById(R.id.edt_apply_pwd);
        applyTimeEdt = (TextInputEditText) mContentView.findViewById(R.id.edt_apply_time);
        if (BmobUser.getCurrentUser(UserEntity.class).isUnGuardian())
            applyPwdEdt.setVisibility(View.GONE);
    }
}
