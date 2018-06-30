package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.dao.MyUserDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * 我的二维码
 *
 * @author RuanWei
 * @date 2016/12/17
 **/
public class MyQRcodeActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout qrcode_rl;
    private ImageView qrcode_iv;
    private RelativeLayout qrcode_en_rl;
    private ImageView qrcode_en_iv;
    private RelativeLayout chinese_rl, english_rl;//中文 英文
    private TextView chinese_tv, english_tv;
    private RelativeLayout fanhui_rl;
    private MyUser myUser;
    private String ch_portrait, ch_company_name, ch_industry_type, ch_address, ch_contact, ch_job, ch_telephone, ch_email, en_portrait, en_company_name, en_industry_type, en_address, en_contact, en_job, en_telephone, en_email;
    private JSONObject jsonObject;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private ArrayList<MyUser>myUsers=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        initYuyan();
        init();
        selectInfo();
        makeQRcode();
    }

    private ArrayList<MyUser> selectInfo() {
        myUsers.clear();
        MyUserDao myUserDao = new MyUserDao(MyQRcodeActivity.this);
        Cursor cursor = myUserDao.selectAll();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int user_type = cursor.getInt(cursor.getColumnIndex("user_type"));
            int system_language = cursor.getInt(cursor.getColumnIndex("system_language"));
            byte[] ch_portrait = cursor.getBlob(cursor.getColumnIndex("ch_portrait"));
            String ch_company_name = cursor.getString(cursor.getColumnIndex("ch_company_name"));
            String ch_industry_type = cursor.getString(cursor.getColumnIndex("ch_industry_type"));
            String ch_company_size = cursor.getString(cursor.getColumnIndex("ch_company_size"));
            String ch_main_business = cursor.getString(cursor.getColumnIndex("ch_main_business"));
            String ch_main_product = cursor.getString(cursor.getColumnIndex("ch_main_product"));
            String ch_official_website = cursor.getString(cursor.getColumnIndex("ch_official_website"));
            String ch_address = cursor.getString(cursor.getColumnIndex("ch_address"));
            String ch_contact = cursor.getString(cursor.getColumnIndex("ch_contact"));
            String ch_job = cursor.getString(cursor.getColumnIndex("ch_job"));
            String ch_telephone = cursor.getString(cursor.getColumnIndex("ch_telephone"));
            String ch_email = cursor.getString(cursor.getColumnIndex("ch_email"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));

            String en_company_name = cursor.getString(cursor.getColumnIndex("en_company_name"));
            String en_industry_type = cursor.getString(cursor.getColumnIndex("en_industry_type"));
            String en_company_size = cursor.getString(cursor.getColumnIndex("en_company_size"));
            String en_main_bussiness = cursor.getString(cursor.getColumnIndex("en_main_bussiness"));
            String en_main_product = cursor.getString(cursor.getColumnIndex("en_main_product"));
            String en_address = cursor.getString(cursor.getColumnIndex("en_address"));
            String en_contact = cursor.getString(cursor.getColumnIndex("en_contact"));
            String en_job = cursor.getString(cursor.getColumnIndex("en_job"));
            myUser = new MyUser(id, user_type, system_language, ch_portrait, ch_company_name, ch_industry_type,
                    ch_company_size, ch_main_business, ch_main_product, ch_official_website, ch_address, ch_contact,
                    ch_job, ch_telephone, ch_email, unique_id, en_company_name, en_industry_type, en_company_size,
                    en_main_bussiness, en_main_product, en_address, en_contact, en_job);
            myUsers.add(myUser);
        }
        cursor.close();
        return myUsers;
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(this);
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
        qrcode_rl = (RelativeLayout) findViewById(R.id.qrcode_rl);
        qrcode_iv = (ImageView) findViewById(R.id.qrcode_iv);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(MyQRcodeActivity.this);
        qrcode_en_rl = (RelativeLayout) findViewById(R.id.qrcode_en_rl);
        qrcode_en_rl.setOnClickListener(this);
        qrcode_en_iv = (ImageView) findViewById(R.id.qrcode_en_iv);
        qrcode_en_iv.setOnClickListener(this);
        chinese_rl = (RelativeLayout) findViewById(R.id.chinese_rl);
        english_rl = (RelativeLayout) findViewById(R.id.english_rl);
        chinese_tv = (TextView) findViewById(R.id.chinese_tv);
        english_tv = (TextView) findViewById(R.id.english_tv);
        chinese_rl.setOnClickListener(this);
        english_rl.setOnClickListener(this);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams rl_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl_params.height = dm.widthPixels * 95 / 100;
        rl_params.width = dm.widthPixels * 95 / 100;
        qrcode_rl.setLayoutParams(rl_params);
        qrcode_rl.setVerticalGravity(RelativeLayout.CENTER_IN_PARENT);
        qrcode_en_rl.setLayoutParams(rl_params);
        qrcode_en_rl.setVerticalGravity(RelativeLayout.CENTER_IN_PARENT);


        RelativeLayout.LayoutParams iv_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv_params.height = dm.widthPixels * 9 / 10;
        iv_params.width = dm.heightPixels * 9 / 10;
        qrcode_iv.setLayoutParams(iv_params);
        qrcode_en_iv.setLayoutParams(iv_params);

    }

    //生成二维码
    private void makeQRcode() {
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        if (myUser != null) {
            //获取数据
            ch_portrait = null;
            ch_company_name = myUser.getCh_company_name();
            ch_industry_type = myUser.getCh_industry_type();
            ch_address = myUser.getCh_address();
            ch_contact = myUser.getCh_contact();
            ch_job = myUser.getCh_job();
            ch_telephone = myUser.getCh_telephone();
            ch_email = myUser.getCh_email();
            constructorTest();
            Bitmap bitmap = EncodingUtils.createQRCode(token + "|" + "ch", 1000, 1000, null);
            qrcode_iv.setImageBitmap(bitmap);

        } else {
            showDialog(MyQRcodeActivity.this, R.string.save_info);
        }

    }


    //生成二维码-英文
    private void makeQRcodeforEn() {
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        constructorTest();
        String s = jsonObject.toString();
        Bitmap bitmap = EncodingUtils.createQRCode(token + "|" + "en", 1000, 1000, null);
        qrcode_en_iv.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        //白色
        int color_white = getResources().getColor(R.color.color_white);
        //蓝色
        int color_blue = getResources().getColor(R.color.blue);
        //右边倒角的蓝色背景
        int right_corner_round_blue = R.drawable.right_corner_solid_transparent_stroke_white;
        //右边倒角的白色背景
        int right_corner_round_white = R.drawable.right_corner_round;
        //左边倒角的蓝色背景
        int left_corner_round_blue = R.drawable.left_corner_blue_and_stroke_white;
        //左边倒角的白色背景
        int left_corner_round_white = R.drawable.left_corner_round_white;
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            //中文
            case R.id.chinese_rl:
                //将中文字体变为蓝色  将背景样式改为左边倒角白色
                chinese_tv.setTextColor(color_blue);
                chinese_rl.setBackgroundResource(left_corner_round_white);
                //将英文字体改为蓝色 将背景样式改为右边倒角蓝色
                english_tv.setTextColor(color_white);
                english_rl.setBackgroundResource(right_corner_round_blue);


                qrcode_rl.setVisibility(View.VISIBLE);
                qrcode_en_rl.setVisibility(View.GONE);
                ToastUtil.showToast2(this, R.string.now_ch);

                SharedPreferences edit = SharedprefenceStore.getSp();
                String token = edit.getString(SharedprefenceStore.TOKEN, "");
                Bitmap bitmap = EncodingUtils.createQRCode(token + "|" + "ch", 1000, 1000, null);
                qrcode_iv.setImageBitmap(bitmap);

                break;
            //英文
            case R.id.english_rl:
                //将英文字体改为蓝色 将背景样式改为右边倒角白色
                english_tv.setTextColor(color_blue);
                english_rl.setBackgroundResource(right_corner_round_white);
                //将中文字体改为白色 将背景样式改为左边蓝色
                chinese_tv.setTextColor(color_white);
                chinese_rl.setBackgroundResource(left_corner_round_blue);

                qrcode_rl.setVisibility(View.GONE);
                qrcode_en_rl.setVisibility(View.VISIBLE);
                ToastUtil.showToast2(this, R.string.now_en);

                makeQRcodeforEn();
                break;
        }
    }


    private void constructorTest() {
        Map<String, Object> map = new ArrayMap<>();
        map.put("ch_portrait", ch_portrait);
        map.put("ch_company_name", ch_company_name);
        map.put("ch_industry_type", ch_industry_type);
        map.put("ch_address", ch_address);
        map.put("ch_contact", ch_contact);
        map.put("ch_job", ch_job);
        map.put("ch_telephone", ch_telephone);
        map.put("ch_email", ch_email);
        map.put("en_portrait", en_portrait);
        map.put("en_company_name", en_company_name);
        map.put("en_industry_type", en_industry_type);
        map.put("en_address", en_address);
        map.put("en_contact", en_contact);
        map.put("en_job", en_job);
        map.put("en_telephone", en_telephone);
        map.put("en_email", en_email);

        jsonObject = new JSONObject(map); // 传入Bean类型
    }

    private void showDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.click_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MyQRcodeActivity.this, BaseDataActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();

            }
        });
        builder.setNegativeButton(R.string.to_see, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        Bitmap bitmap = EncodingUtils.createQRCode(token + "|" + "ch", 1000, 1000, null);
        qrcode_iv.setImageBitmap(bitmap);
    }
}
