package com.baibeiyun.eazyfair.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;

import java.io.File;
import java.util.Locale;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    //登陆 注册 试用
    private Button first_login_bt, register_bt, tryout_bt;
    private long exitTime = 0;
    private TextView version_number_tv;
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initYuyan();
        SharedPreferences preferences = SharedprefenceStore.getSp();
        String account = preferences.getString(SharedprefenceStore.ACCOUNT, "");
        if (!account.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, EnterActivity.class);
            startActivity(intent);
        }
        init();
        createSDCardDir();
        createSDCardDir2();

    }


    private void initYuyan() {
        // 获得res资源对象
        resources = getResources();
        // 获得设置对象
        config = resources.getConfiguration();
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MainActivity.this);
        if (language != null) {
            String tag = this.language.getTag();
            if (tag.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);
            } else if (tag.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }


    private void init() {
        first_login_bt = (Button) findViewById(R.id.first_login_bt);
        register_bt = (Button) findViewById(R.id.register_bt);
        tryout_bt = (Button) findViewById(R.id.tryout_bt);
        version_number_tv = (TextView) findViewById(R.id.version_number_tv);
        first_login_bt.setOnClickListener(MainActivity.this);
        register_bt.setOnClickListener(MainActivity.this);
        tryout_bt.setOnClickListener(MainActivity.this);
        version_number_tv.setText("v1.0");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.first_login_bt:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.register_bt:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tryout_bt:
                intent = new Intent(MainActivity.this, EnterActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;

        }
    }


    /**
     * 在SD卡上创建一个文件夹
     */
    public void createSDCardDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + "/EasyFair";
            File path1 = new File(path);
            if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();
                setTitle("paht ok,path:" + path);
            }
        } else {
            setTitle("false");
        }
    }

    /**
     * 在SD卡上创建一个文件夹(储存备份数据库)
     */
    public void createSDCardDir2() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + "/EFbackups";
            File path1 = new File(path);
            if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();
                setTitle("paht ok,path:" + path);
            }
        } else {
            setTitle("false");
        }
    }

    /**
     * back键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, R.string.to_exit, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
        return true;
    }


}
