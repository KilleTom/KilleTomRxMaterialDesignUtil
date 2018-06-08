package cn.ypz.com.killetomrxmaterialdesignutil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    protected <T extends AppCompatActivity> void easyStartActivity(Class<T> cls) {
        this.startActivity(new Intent(this, cls));
    }
}
