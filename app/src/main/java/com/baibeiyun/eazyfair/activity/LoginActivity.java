package com.baibeiyun.eazyfair.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.ActivityCollector;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.SystemBarTintManager;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText input_emailaccount_et;
    private EditText input_password_et;
    private Button login_bt;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private TextView forget_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.blue);//通知栏所需颜色
        }
        initYuyan();
        init();
        ActivityCollector.finishAll();
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(LoginActivity.this);
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
        forget_tv = (TextView) findViewById(R.id.forget_tv);
        forget_tv.setOnClickListener(this);
        input_emailaccount_et = (EditText) findViewById(R.id.input_emailaccount_et);
        input_password_et = (EditText) findViewById(R.id.input_password_et);
        input_password_et.setTypeface(input_emailaccount_et.getTypeface());
        login_bt = (Button) findViewById(R.id.login_bt);
        input_emailaccount_et.setOnClickListener(LoginActivity.this);
        input_password_et.setOnClickListener(LoginActivity.this);
        login_bt.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_bt:
                initDateLogin(new LoginCallBack() {
                    @Override
                    public void success() {
                        Intent intent = new Intent(LoginActivity.this, EnterActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failed() {
                        if (language.getTag().equals("ch")) {
                            ToastUtil.showToast(LoginActivity.this, "登录失败！");
                        } else if (language.getTag().equals("en")) {
                            ToastUtil.showToast(LoginActivity.this, "Login failed！");
                        }

                    }
                });

                break;
            //忘记密码
            case R.id.forget_tv:
                Intent intent = new Intent(LoginActivity.this, ForgetPassWordActivity.class);
                startActivity(intent);
                break;
        }
    }

    //登陆接口的调用 密码需要Md5加密才能成功调用
    private void initDateLogin(final LoginCallBack loginCallBack) {
        final String account = input_emailaccount_et.getText().toString();
        final String passWord = input_password_et.getText().toString();
        String url = BaseUrl.HTTP_URL + "user/login";
        String tag = "Login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", account);
            jsonObject.put("password", HttpUtils.md5(passWord));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Login", "initDateLogin: " + HttpUtils.md5(passWord));
        Log.d("Login", "initDateLogin: " + jsonObject.toString());
        HttpUtils.doPost(LoginActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Login", "initDateLogin: " + result.toString());
                try {
                    if ("200".equals(result.getString("code"))) {
                        loginCallBack.success();

                        SharedPreferences.Editor edit = SharedprefenceStore.getSp().edit();
                        edit.putString(SharedprefenceStore.ACCOUNT, account);
                        edit.putString(SharedprefenceStore.PASSWORD, passWord);
                        edit.putString(SharedprefenceStore.TOKEN, result.getString("token"));
                        edit.putString(SharedprefenceStore.INDUSTRYTYPE, result.getString("industry_type"));
                        edit.commit();
                    }
                    ToastUtil.showToast(LoginActivity.this, result.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                loginCallBack.failed();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyAppclication.getHttpQueues().cancelAll("Login");
    }

    interface LoginCallBack {
        void success();

        void failed();
    }
}
