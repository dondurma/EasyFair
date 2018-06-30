package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.LoginActivity;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.dao.MyUserDao;
import com.baibeiyun.eazyfair.entities.BaseInfoBean;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.baibeiyun.eazyfair.view.RoundImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class AccountActivity extends BaseActivity implements View.OnClickListener {
    private ImageView background_card_iv;//半透明背景图片
    private RelativeLayout fanhui_rl;//返回
    private RoundImageView logo;//公司的logo
    private TextView name;//公司名
    private TextView type_member_tv;//会员类型
    private TextView number_member_tv;//等级数

    private LinearLayout basic_data_ll;//基本资料
    private LinearLayout my_qr_code_ll;//我的二维码
    private LinearLayout about_ll;//关于易非
    private LinearLayout system_set_ll;//系统设置
    private LinearLayout clean_ll;//清理缓存
    private LinearLayout exit_login_ll;//退出登录
    private MyUser myUser;

    private MyDialog myDialog;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private String tag = "BaseInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initYuyan();
        myUser = CursorUtils.selectOurInfo(AccountActivity.this);
        initview();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(AccountActivity.this);
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


    @Override
    protected void onRestart() {
        super.onRestart();
        myUser = CursorUtils.selectOurInfo(AccountActivity.this);
        initview();
    }


    private void initview() {
        myDialog = new MyDialog(AccountActivity.this);
        background_card_iv = (ImageView) findViewById(R.id.background_card_iv);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        logo = (RoundImageView) findViewById(R.id.logo);
        name = (TextView) findViewById(R.id.name);
        type_member_tv = (TextView) findViewById(R.id.type_member_tv);
        number_member_tv = (TextView) findViewById(R.id.number_member_tv);
        basic_data_ll = (LinearLayout) findViewById(R.id.basic_data_ll);
        my_qr_code_ll = (LinearLayout) findViewById(R.id.my_qr_code_ll);
        about_ll = (LinearLayout) findViewById(R.id.about_ll);
        system_set_ll = (LinearLayout) findViewById(R.id.system_set_ll);
        exit_login_ll = (LinearLayout) findViewById(R.id.exit_login_ll);
        clean_ll = (LinearLayout) findViewById(R.id.clean_ll);

        fanhui_rl.setOnClickListener(AccountActivity.this);
        basic_data_ll.setOnClickListener(AccountActivity.this);
        my_qr_code_ll.setOnClickListener(AccountActivity.this);
        about_ll.setOnClickListener(AccountActivity.this);
        system_set_ll.setOnClickListener(AccountActivity.this);
        exit_login_ll.setOnClickListener(AccountActivity.this);
        clean_ll.setOnClickListener(AccountActivity.this);
        initDateBaseInfo("ch");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //基本资料
            case R.id.basic_data_ll:
                intent = new Intent(AccountActivity.this, BaseDataActivity.class);
                startActivity(intent);
                break;
            //我的二维码
            case R.id.my_qr_code_ll:
                intent = new Intent(AccountActivity.this, MyQRcodeActivity.class);
                startActivity(intent);
                break;
            //关于易非
            case R.id.about_ll:
                intent = new Intent(AccountActivity.this, AboutEasyFairActivity.class);
                startActivity(intent);
                break;
            //系统设置
            case R.id.system_set_ll:
                intent = new Intent(AccountActivity.this, SystemSettingsActivity.class);
                startActivity(intent);
                break;
            //退出登陆
            case R.id.exit_login_ll:
                ExitDialog(AccountActivity.this, R.string.exit_sure);
                break;
            //清理缓存
            case R.id.clean_ll:
                deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/EasyFair/"));
                Toast.makeText(AccountActivity.this, R.string.clean_complete, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    //退出登录的操作
    private void ExitDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = SharedprefenceStore.getSp().edit();
                editor.clear();
                editor.apply();
                MyUserDao myUserDao = new MyUserDao(AccountActivity.this);
                if (myUser != null) {
                    myUser.set_id(1);
                    myUserDao.deletdata(myUser);
                }
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception ignored) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
    }

    //获取用户的基本资料的接口
    private void initDateBaseInfo(String userType) {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "user/getBasicInfo";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(AccountActivity.this, url, tag, jsonObject,
                new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if ("200".equals(code)) {
                        Gson gson = new Gson();
                        BaseInfoBean baseInfoBean = gson.fromJson(result + "", BaseInfoBean.class);
                        BaseInfoBean.UserInfoBean userInfo = baseInfoBean.getUserInfo();
                        name.setText(userInfo.getChCompanyName());
                        type_member_tv.setText(userInfo.getVipType());
                        number_member_tv.setText(userInfo.getVipLevel());
                        String url = userInfo.getLogo();
                        Picasso.with(AccountActivity.this).load(url).into(logo);
                        Picasso.with(AccountActivity.this).load(url).into(background_card_iv);
                        background_card_iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    myDialog.dialogDismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    @Override
    protected void onStop() {
        MyAppclication.getHttpQueues().cancelAll(tag);
        super.onStop();
    }


}
