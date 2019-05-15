package cn.dustray.defenderplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cn.bmob.v3.BmobUser;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {
private Button btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnExit=findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch ((view.getId()))
        {
            case R.id.btn_exit:
                BmobUser.logOut();
                finish();
                break;
        }
    }
}
