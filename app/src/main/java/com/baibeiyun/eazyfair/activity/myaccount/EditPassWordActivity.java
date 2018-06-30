package com.baibeiyun.eazyfair.activity.myaccount;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class EditPassWordActivity extends BaseActivity implements View.OnClickListener {
    private EditText oldEd, newEd, confirmEd;
    private Button comitBtn;
    private RelativeLayout fanhui_rl;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass_word);
        initYuyan();
        initView();
        initEvent();

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        select();
        if (listforlanguage != null) {
            String tag = language.getTag();
            if (tag.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);

            } else if (tag.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }

    // 查询数据库的方法
    private ArrayList<Language> select() {
        listforlanguage.clear();
        LanguageDao languageDao = new LanguageDao(this);
        Cursor cursor = languageDao.selectById("easyfair");
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            language = new Language(id, tag);
            listforlanguage.add(language);
        }
        cursor.close();
        return listforlanguage;

    }

    private void initEvent() {
        comitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateEdPsw();
            }
        });
    }

    private void initView() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        oldEd = (EditText) findViewById(R.id.old_psw_ed);
        newEd = (EditText) findViewById(R.id.new_psw_ed);
        confirmEd = (EditText) findViewById(R.id.confirm_psw_ed);
        comitBtn = (Button) findViewById(R.id.comit_btn);
        fanhui_rl.setOnClickListener(this);
    }

    private void initDateEdPsw() {
        JSONObject jsonObject = new JSONObject();
        String url = BaseUrl.HTTP_URL + "user/updatePassword";
        String oldPsw = oldEd.getText().toString();
        final String newPassword1 = newEd.getText().toString();
        String newPassword2 = confirmEd.getText().toString();
        String tag = "EditPsw";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String userType = edit.getString(SharedprefenceStore.REGISTERTYPE, "");
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        try {
            jsonObject.put("oldPassword", HttpUtils.md5(oldPsw));
            jsonObject.put("newPassword1", newPassword1);
            jsonObject.put("newPassword2", newPassword2);
            jsonObject.put("userType", userType);
            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(EditPassWordActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    ToastUtil.showToast(EditPassWordActivity.this, result.getString("message"));
                    if ("200".equals(result.getString("code"))) {
                        SharedPreferences.Editor comitEdit = SharedprefenceStore.getSp().edit();
                        comitEdit.putString(SharedprefenceStore.PASSWORD, newPassword1);
                        comitEdit.apply();
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("EditPsw", "onResponse: " + result.toString());
            }

            @Override
            public void onError(VolleyError error) {
//                Log.d("EditPsw", "volleyError: " + error);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyAppclication.getHttpQueues().cancelAll("EditPsw");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
        }
    }
}
