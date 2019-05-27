package cn.dustray.defenderplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.BmobUser;
import cn.dustray.entity.UserEntity;
import cn.dustray.utils.xToast;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnExit;
    private TextView username, email, emailVerify, identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        emailVerify = findViewById(R.id.email_Verify);
        identity = findViewById(R.id.identity);

        initData();
    }

    private void initData() {
        if (!BmobUser.isLogin()) return;
        UserEntity u = BmobUser.getCurrentUser(UserEntity.class);
        username.setText(u.getUsername());
        email.setText(u.getEmail());
        emailVerify.setText(u.getEmailVerified() ? "已验证" : "未验证");
        identity.setText(u.isGuardian() ? "监护人" : "被监护人");
    }

    public void logoutEaseMob() {
        new Thread(new Runnable() {
            public void run() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        }).start();
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
        switch ((view.getId())) {
            case R.id.btn_exit:
                BmobUser.logOut();
                logoutEaseMob();
                finish();
                break;
        }
    }
}
