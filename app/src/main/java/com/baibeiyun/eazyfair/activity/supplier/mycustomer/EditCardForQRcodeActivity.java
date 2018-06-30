package com.baibeiyun.eazyfair.activity.supplier.mycustomer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.dao.MyCustomerDao;
import com.baibeiyun.eazyfair.entities.BaseInfoBean;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class EditCardForQRcodeActivity extends BaseActivity implements View.OnClickListener {
    private TextView again_photo_rl;//重新拍照
    private TextView save_rl;//保存
    private EditText custom_name_et;//姓名
    private EditText custom_job_et;//职位
    private EditText phonenumber_et;//电话
    private EditText email_et;//邮箱
    private EditText commpany_name_et;//公司名
    private EditText commpany_address_et;//公司地址


    private EditText industry_type_et;//行业类型
    private EditText web_et;//网址
    private EditText custom_other_et;//爱好
    private TextView unique_id_et;
    private ImageView company_logo_iv;
    private String result;
    private MyDialog myDialog;
    //从网络获取的数据
    private String contactPerson, address, companyName, companySize, contactPhone,
            email, industryType, jobTitle, mainBussiness, mainProduct, website, uniqueid;
    private byte[] bytes_bg;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private MyCustomer myCustomer;


    //得到用户当前输入的数据
    private String name_s, job_s, phone_s, email_s, companyname_s,
            companyaddress_s, industry_s, other_s, unique_id, web_s;
    private String tag = "QR";
    private Bitmap drawingCache;
    private Bitmap bitmap_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_list);
        initYuyan();
        initview();
        initDateQr(new InitDateQr() {
            @Override
            public void initDateQrSuccess() {
                myDialog.dialogDismiss();
            }
        });
        myCustomer = CursorUtils.selectPartBuyer(EditCardForQRcodeActivity.this);
    }

    @Override
    protected void onDestroy() {
        MyAppclication.getHttpQueues().cancelAll(tag);
        super.onDestroy();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(EditCardForQRcodeActivity.this);
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


    private void initview() {
        Intent intent = getIntent();
        result = intent.getStringExtra("result");
        myDialog = new MyDialog(this);
        again_photo_rl = (TextView) findViewById(R.id.again_photo_rl);
        save_rl = (TextView) findViewById(R.id.save_rl);
        custom_name_et = (EditText) findViewById(R.id.custom_name_et);
        custom_job_et = (EditText) findViewById(R.id.custom_job_et);
        phonenumber_et = (EditText) findViewById(R.id.phonenumber_et);
        email_et = (EditText) findViewById(R.id.email_et);
        web_et = (EditText) findViewById(R.id.web_et);
        commpany_name_et = (EditText) findViewById(R.id.commpany_name_et);
        commpany_address_et = (EditText) findViewById(R.id.commpany_address_et);
        industry_type_et = (EditText) findViewById(R.id.industry_type_et);
        custom_other_et = (EditText) findViewById(R.id.custom_other_et);
        company_logo_iv = (ImageView) findViewById(R.id.company_logo_iv);
        unique_id_et = (TextView) findViewById(R.id.unique_id_et);

        again_photo_rl.setOnClickListener(EditCardForQRcodeActivity.this);
        save_rl.setOnClickListener(EditCardForQRcodeActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //重新拍摄
            case R.id.again_photo_rl:
                finish();
                break;
            //保存到数据库
            case R.id.save_rl:
                getData();
                if (myCustomer != null) {
                    if (myCustomer.getName().equals(name_s) && myCustomer.getCompany_name().equals(companyname_s) && myCustomer.getUnique_id().equals(unique_id)) {
                        Toast.makeText(getApplicationContext(), R.string.not_add, Toast.LENGTH_SHORT).show();
                    } else {
                        saved();
                        Toast.makeText(getApplicationContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    saved();
                }

                break;
        }
    }

    private void getData() {
        company_logo_iv.setDrawingCacheEnabled(true);
        drawingCache = company_logo_iv.getDrawingCache();
        name_s = custom_name_et.getText().toString().trim();
        job_s = custom_job_et.getText().toString().trim();
        phone_s = phonenumber_et.getText().toString().trim();
        email_s = email_et.getText().toString().trim();
        companyname_s = commpany_name_et.getText().toString().trim();
        companyaddress_s = commpany_address_et.getText().toString().trim();
        industry_s = industry_type_et.getText().toString().trim();
        other_s = custom_other_et.getText().toString().trim();


        if (!unique_id_et.getText().toString().trim().equals("")) {
            unique_id = unique_id_et.getText().toString().trim();
        } else {
            String stringRandom = ImageFactoryandOther.getStringRandom(6);
            unique_id = stringRandom;
        }
        web_s = web_et.getText().toString().trim();
        Bitmap bitmap_bg = BitmapFactory.decodeResource(getResources(), R.drawable.a3);
        bytes_bg = BitmapUtils.compressBmpFromBmp(bitmap_bg);

    }

    //保存数据的方法
    private void saved() {
        if (!email_s.equals("") && !companyname_s.equals("") && !email_s.equals("")) {
            bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
            //创建一个实体类对象
            MyCustomer myCustomer = new MyCustomer();
            //将得到的所有数据加载到myCustomer对象中
            String date = DateUtil.getDate();
            myCustomer.setCreate_time(date);
            myCustomer.setName(name_s);
            myCustomer.setJob_position(job_s);
            myCustomer.setPhone(phone_s);
            myCustomer.setEmail(email_s);
            myCustomer.setCompany_name(companyname_s);
            myCustomer.setCompany_address(companyaddress_s);
            myCustomer.setIndustry_type(industry_s);
            myCustomer.setCustomer_ohter(other_s);
            myCustomer.setCustomer_type(0);//0-供应商的客户
            myCustomer.setUnique_id(unique_id);
            myCustomer.setWeb(web_s);


            myCustomer.setBackground_pic(bytes_bg);

            if (drawingCache != null) {
                byte[] bytes = BitmapUtils.Bitmap2Bytes(drawingCache);
                myCustomer.setCompany_logo(bytes);
                company_logo_iv.setDrawingCacheEnabled(false);
            } else {
                Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap_no);
                myCustomer.setCompany_logo(bytes);
            }

            //创建一个方法层的对象
            MyCustomerDao myCustomerDao = new MyCustomerDao(EditCardForQRcodeActivity.this);
            //执行该方法层的添加的方法
            myCustomerDao.insertdata(myCustomer);
            finish();
        } else {
            ToastUtil.showToast2(EditCardForQRcodeActivity.this, R.string.check_info);
        }

    }


    private void initDateQr(final InitDateQr initDateQr) {
        myDialog.dialogShow();
        final String url = BaseUrl.HTTP_URL + "user/getBasicInfo";
        JSONObject jsonObject = new JSONObject();
        final String[] split = result.split("\\|");
        try {
            jsonObject.put("token", split[0]);
            jsonObject.put("userType", split[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(EditCardForQRcodeActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                Gson gson = new Gson();
                BaseInfoBean baseInfoBean = gson.fromJson(result + "", BaseInfoBean.class);
                String code = baseInfoBean.getCode();
                if ("200".equals(code)) {
                    BaseInfoBean.UserInfoBean userInfo = baseInfoBean.getUserInfo();
                    if ("ch".equals(split[1])) {
                        contactPerson = userInfo.getChContactPerson();//姓名
                        address = userInfo.getChAddress();//公司地址
                        companyName = userInfo.getChCompanyName();//公司名
                        companySize = userInfo.getChCompanySize();//公司规模
                        industryType = userInfo.getChIndustryType();//行业类型
                        jobTitle = userInfo.getChJobTitle();//职位
                        mainBussiness = userInfo.getChMainBussiness();//主营业务
                        mainProduct = userInfo.getChMainProduct();//主要产品


                        custom_name_et.setText(contactPerson);
                        custom_job_et.setText(jobTitle);
                        commpany_name_et.setText(companyName);
                        commpany_address_et.setText(address);
                        industry_type_et.setText(industryType);

                    } else if (split[1].equals("en")) {
                        contactPerson = userInfo.getEnContactPerson();//姓名
                        address = userInfo.getEnAddress();//公司地址
                        companyName = userInfo.getEnCompanyName();//公司名
                        companySize = userInfo.getEnCompanySize();//公司规模
                        industryType = userInfo.getEnIndustryType();//行业类型
                        jobTitle = userInfo.getEnJobTitle();//职位
                        mainBussiness = userInfo.getEnMainBussiness();//主营业务
                        mainProduct = userInfo.getEnMainProduct();//主要产品

                        custom_name_et.setText(contactPerson);
                        custom_job_et.setText(jobTitle);
                        commpany_name_et.setText(companyName);
                        commpany_address_et.setText(address);
                        industry_type_et.setText(industryType);

                    }
                    email = userInfo.getEmail();//Email
                    contactPhone = userInfo.getContactPhone();//电话
                    website = userInfo.getWebsite();//公司官网
                    uniqueid = userInfo.getUniqueId();//公司官网
                    String url = userInfo.getLogo();//logo
                    phonenumber_et.setText(contactPhone);
                    email_et.setText(email);
                    web_et.setText(website);
                    custom_other_et.setText("");
                    unique_id_et.setText(uniqueid);
                    Picasso.with(EditCardForQRcodeActivity.this).load(url).into(company_logo_iv);
                }
                initDateQr.initDateQrSuccess();
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }


    interface InitDateQr {
        void initDateQrSuccess();
    }


}
