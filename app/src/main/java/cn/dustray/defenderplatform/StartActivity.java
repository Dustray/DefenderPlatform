package cn.dustray.defenderplatform;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        version = findViewById(R.id.version);

        try {
            version.setText("Version " + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        };
        timer.schedule(task, 1000 ); //10秒后

    }

    /**
     * 获取程序版本号
     * @return
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
