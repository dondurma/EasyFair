package com.baibeiyun.eazyfair.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.TimeUtils;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MessageDetailsActivity extends BaseActivity implements View.OnClickListener {

    private MyDialog myDialog;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private String tag = "Message";

    private RelativeLayout fanhui_rl;
    private TextView title_tv;
    private TextView time_tv;
    private TextView content_tv;
    //接收到的数据
    private String acceptTime, content, createTime, hasReadState, messageId, readTime, title, type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        initYuyan();
        initView();
        initData();
        initSetData();
        initDateMessage();
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MessageDetailsActivity.this);
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

    private void initView() {
        myDialog = new MyDialog(this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
        content_tv = (TextView) findViewById(R.id.content_tv);
    }

    private void initData() {
        Intent intent = getIntent();
        acceptTime = intent.getStringExtra("acceptTime");
        content = intent.getStringExtra("content_tv");
        createTime = intent.getStringExtra("createTime");
        hasReadState = intent.getStringExtra("hasReadState");
        messageId = intent.getStringExtra("messageId");
        readTime = intent.getStringExtra("readTime");
        title = intent.getStringExtra("title_tv");
        type = intent.getStringExtra("type");
    }

    private void initSetData() {
        title_tv.setText(title);
        String time = TimeUtils.timeFormat(Long.parseLong(readTime));
        time_tv.setText(time);
        content_tv.setText("\t\t\t\t" + content);
    }


    private void initDateMessage() {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "sysUserMessage/changeMessageHasReadStatus";
        JSONObject jsonObject = new JSONObject();
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
            jsonObject.put("messageId", messageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(MessageDetailsActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                myDialog.dialogDismiss();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        MyAppclication.getHttpQueues().cancelAll(tag);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui_rl:
                Intent intent = new Intent(this, InformationActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    //back键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

}
