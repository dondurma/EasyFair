package com.baibeiyun.eazyfair.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ForgetPassWordActivity extends BaseActivity implements View.OnClickListener {
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private RelativeLayout fanhui_rl;
    private EditText inputphonenumber_et;
    private EditText input_verification_et;
    private TextView achieve_verification_tv;
    private EditText inputpassword_et;
    private Button update_pw_bt;
    private EditText again_input_password_et;
    private boolean tag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_word);
        initYuyan();
        initView();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(ForgetPassWordActivity.this);
        if (language != null) {
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

    private void initView() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        inputphonenumber_et = (EditText) findViewById(R.id.inputphonenumber_et);
        input_verification_et = (EditText) findViewById(R.id.input_verification_et);
        achieve_verification_tv = (TextView) findViewById(R.id.achieve_verification_tv);
        inputpassword_et = (EditText) findViewById(R.id.inputpassword_et);
        update_pw_bt = (Button) findViewById(R.id.update_pw_bt);
        again_input_password_et = (EditText) findViewById(R.id.again_input_password_et);
        again_input_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String againpassword = s.toString().trim();
                String password = inputpassword_et.getText().toString().trim();
                if (againpassword.equals(password)) {
                    tag = true;
                } else {
                    tag = false;
                    ToastUtil.showToast2(ForgetPassWordActivity.this, R.string.pw_curror);
                }
            }
        });
        fanhui_rl.setOnClickListener(this);
        achieve_verification_tv.setOnClickListener(this);
        update_pw_bt.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            //获取验证码
            case R.id.achieve_verification_tv:
                if (!"".equals(inputphonenumber_et.getText().toString().trim())) {
                    SendVerificationCode();
                } else {
                    ToastUtil.showToast2(this, R.string.input_sjhhyx);//请输入邮箱
                }

                break;
            case R.id.update_pw_bt:
                String email = inputphonenumber_et.getText().toString().trim();
                String verfication = input_verification_et.getText().toString().trim();
                String password = inputpassword_et.getText().toString().trim();
                String againpassword = again_input_password_et.getText().toString().trim();
                if ("".equals(email) && "".equals(verfication) && "".equals(password) && "".equals(againpassword)) {
                    ToastUtil.showToast2(this, R.string.please_srwzxx);
                } else {
                    if (tag == true) {
                        UpdatePassWord(email, verfication, password);
                    } else {
                        ToastUtil.showToast2(ForgetPassWordActivity.this, R.string.pw_curror);
                    }

                }


                break;
        }
    }


    //倒计时
    private void achieve() {
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                achieve_verification_tv.setText(l / 1000 + "秒后重试");
                achieve_verification_tv.setEnabled(false);
            }

            @Override
            public void onFinish() {
                achieve_verification_tv.setEnabled(true);
                achieve_verification_tv.setText(R.string.achieve_yzm);
            }
        };
        timer.start();
    }


    //发送验证码的接口
    private void SendVerificationCode() {
        JSONObject jsonObject = new JSONObject();
        String url = BaseUrl.HTTP_URL + "user/verificationByPassword";
        String email = inputphonenumber_et.getText().toString();
        String tag = "Email";
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(ForgetPassWordActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String message = result.getString("message");
                    ToastUtil.showToast(ForgetPassWordActivity.this, message);
                    String code = result.getString("code");
                    if (!"400".equals(code)) {
                        achieve();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    //修改密码的接口
    private void UpdatePassWord(String email, String code, String password) {
        JSONObject jsonObject = new JSONObject();
        String url = BaseUrl.HTTP_URL + "user/edit";
        String tag = "Edit";
        try {
            jsonObject.put("email", email);
            jsonObject.put("code", code);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(ForgetPassWordActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String message = result.getString("message");
                    ToastUtil.showToast(ForgetPassWordActivity.this, message);
                    String code = result.getString("code");
                    if ("200".equals(code)) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

}
