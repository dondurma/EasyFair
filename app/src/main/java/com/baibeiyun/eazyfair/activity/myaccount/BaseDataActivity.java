package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.dao.MyUserDao;
import com.baibeiyun.eazyfair.entities.BaseInfoBean;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CreameBitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class BaseDataActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout chinese_rl, english_rl;//中文 英文
    private Button save;//保存按钮
    private TextView chinese_tv, english_tv;
    //中文
    private ImageView company_logo_cn_iv;//上传公司logo图片

    private TextView unique_id_et;//生成唯一的id

    private TextView logo_ch_tv, logo_en_tv;
    private TextView company_name_ch_tv, company_name_en_tv;
    private TextView industry_ch_tv, industry_en_tv;
    private TextView company_scale_ch_tv, company_scale_en_tv;
    private TextView main_business_ch_tv, main_business_en_tv;
    private TextView main_product_ch_tv, main_product_en_tv;
    private TextView website_ch_tv, website_en_tv;
    private TextView address_ch_tv, address_en_tv;
    private TextView contacts_ch_tv, contacts_en_tv;
    private TextView job_ch_tv, job_en_tv;
    private TextView tel_ch_tv, tel_en_tv;
    private TextView email_tv;

    //popupwindow相关
    private int from = 0;
    private PopupWindow mPopupWindow;

    //得到用户输入的数据
    //中文
    private String ch_companyname, ch_industry, ch_companysize, ch_mainbusiness, ch_mainproduct, ch_website, ch_address, ch_contacts, ch_job, ch_phone, ch_email, unique_id;
    //英文
    private String en_companyname, en_industry, en_companysize, en_mainbusiness, en_mainproduct, en_address, en_contacts, en_job;
    //logo
    private Bitmap drawingCache;


    private static final int REQ_2 = 2;//从相册选择图片

    private MyDialog myDialog;//自定义的Dialog

    private EditText phone_cn_et;//输入联系电话
    private EditText email_cn_et;//输入email
    private EditText official_website_cn_et;//输入官网地址
    private String encode;//图片编码之后得到的String

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private EditText company_name_cn_et;
    private EditText industry_cn_et;//输入行业类型
    private EditText company_size_cn_et;//输入公司规模
    private EditText main_business_cn_et;//输入主营业务
    private EditText main_product_cn_et;//输入主要产品
    private EditText address_cn_et;//输入公司地址
    private EditText contacts_cn_et;//输入联系人
    private EditText job_cn_et;//联系人职位


    private EditText company_name_en_et;
    private EditText industry_en_et;
    private EditText company_size_en_et;
    private EditText main_business_en_et;
    private EditText main_product_en_et;
    private EditText address_en_et;
    private EditText contacts_en_et;
    private EditText job_en_et;

    private Bitmap bitmap;
    private String tag = "BaseInfo";
    private MyUser myUser;

    private List<ImageView> imageViewList = new ArrayList<>();//存放显示图片的四个ImageView
    private final int REQUEST_IMAGE = 2;
    private final int maxNum = 1;//最多显示一张图片
    private ArrayList<String> mSelectPath;//所选的图片的地址
    private List<Bitmap> bitmapList = new ArrayList<>();//存放所选的图片
    private List<String> finalSelectPaths = new ArrayList<>();//存放最终所选择的图片的地址
    private String allPictureUrl;
    private String tag1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_data);
        initYuyan();
        initView();
        myUser = CursorUtils.selectOurInfo(BaseDataActivity.this);
        initDateBaseInfo("en");
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(BaseDataActivity.this);
        if (language != null) {
            tag1 = this.language.getTag();
            if (tag1.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);
            } else if (tag1.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }


    private void initView() {
        myDialog = new MyDialog(this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        chinese_rl = (RelativeLayout) findViewById(R.id.chinese_rl);
        english_rl = (RelativeLayout) findViewById(R.id.english_rl);
        save = (Button) findViewById(R.id.save);
        chinese_tv = (TextView) findViewById(R.id.chinese_tv);
        english_tv = (TextView) findViewById(R.id.english_tv);
        company_logo_cn_iv = (ImageView) findViewById(R.id.company_logo_cn_iv);
        company_name_cn_et = (EditText) findViewById(R.id.company_name_cn_et);
        industry_cn_et = (EditText) findViewById(R.id.industry_cn_et);
        company_size_cn_et = (EditText) findViewById(R.id.company_size_cn_et);
        main_business_cn_et = (EditText) findViewById(R.id.main_business_cn_et);
        main_product_cn_et = (EditText) findViewById(R.id.main_product_cn_et);
        official_website_cn_et = (EditText) findViewById(R.id.official_website_cn_et);
        address_cn_et = (EditText) findViewById(R.id.address_cn_et);
        contacts_cn_et = (EditText) findViewById(R.id.contacts_cn_et);
        job_cn_et = (EditText) findViewById(R.id.job_cn_et);
        phone_cn_et = (EditText) findViewById(R.id.phone_cn_et);
        email_cn_et = (EditText) findViewById(R.id.email_cn_et);
        unique_id_et = (TextView) findViewById(R.id.unique_id_et);
        contacts_en_et = (EditText) findViewById(R.id.contacts_en_et);
        job_en_et = (EditText) findViewById(R.id.job_en_et);

        logo_ch_tv = (TextView) findViewById(R.id.logo_ch_tv);
        company_name_ch_tv = (TextView) findViewById(R.id.company_name_ch_tv);
        industry_ch_tv = (TextView) findViewById(R.id.industry_ch_tv);
        company_scale_ch_tv = (TextView) findViewById(R.id.company_scale_ch_tv);
        main_business_ch_tv = (TextView) findViewById(R.id.main_business_ch_tv);
        main_product_ch_tv = (TextView) findViewById(R.id.main_product_ch_tv);
        website_ch_tv = (TextView) findViewById(R.id.website_ch_tv);
        address_ch_tv = (TextView) findViewById(R.id.address_ch_tv);
        contacts_ch_tv = (TextView) findViewById(R.id.contacts_ch_tv);
        job_ch_tv = (TextView) findViewById(R.id.job_ch_tv);
        tel_ch_tv = (TextView) findViewById(R.id.tel_ch_tv);
        email_tv = (TextView) findViewById(R.id.email_tv);

        logo_en_tv = (TextView) findViewById(R.id.logo_en_tv);
        company_name_en_tv = (TextView) findViewById(R.id.company_name_en_tv);
        industry_en_tv = (TextView) findViewById(R.id.industry_en_tv);
        company_scale_en_tv = (TextView) findViewById(R.id.company_scale_en_tv);
        main_business_en_tv = (TextView) findViewById(R.id.main_business_en_tv);
        main_product_en_tv = (TextView) findViewById(R.id.main_product_en_tv);
        website_en_tv = (TextView) findViewById(R.id.website_en_tv);
        address_en_tv = (TextView) findViewById(R.id.address_en_tv);
        contacts_en_tv = (TextView) findViewById(R.id.contacts_en_tv);
        job_en_tv = (TextView) findViewById(R.id.job_en_tv);
        tel_en_tv = (TextView) findViewById(R.id.tel_en_tv);

        company_name_en_et = (EditText) findViewById(R.id.company_name_en_et);
        industry_en_et = (EditText) findViewById(R.id.industry_en_et);
        company_size_en_et = (EditText) findViewById(R.id.company_size_en_et);
        main_business_en_et = (EditText) findViewById(R.id.main_business_en_et);
        main_product_en_et = (EditText) findViewById(R.id.main_product_en_et);
        address_en_et = (EditText) findViewById(R.id.address_en_et);


        fanhui_rl.setOnClickListener(BaseDataActivity.this);
        chinese_rl.setOnClickListener(BaseDataActivity.this);
        english_rl.setOnClickListener(BaseDataActivity.this);
        save.setOnClickListener(BaseDataActivity.this);
        company_logo_cn_iv.setOnClickListener(BaseDataActivity.this);
        imageViewList.add(company_logo_cn_iv);
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
            //返回
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
                //显示所有中文的textView
                logo_ch_tv.setVisibility(View.VISIBLE);
                company_name_ch_tv.setVisibility(View.VISIBLE);
                industry_ch_tv.setVisibility(View.VISIBLE);
                company_scale_ch_tv.setVisibility(View.VISIBLE);
                main_business_ch_tv.setVisibility(View.VISIBLE);
                main_product_ch_tv.setVisibility(View.VISIBLE);
                website_ch_tv.setVisibility(View.VISIBLE);
                address_ch_tv.setVisibility(View.VISIBLE);
                contacts_ch_tv.setVisibility(View.VISIBLE);
                job_ch_tv.setVisibility(View.VISIBLE);
                tel_ch_tv.setVisibility(View.VISIBLE);
                //隐藏所有英文textview
                logo_en_tv.setVisibility(View.GONE);
                company_name_en_tv.setVisibility(View.GONE);
                industry_en_tv.setVisibility(View.GONE);
                company_scale_en_tv.setVisibility(View.GONE);
                main_business_en_tv.setVisibility(View.GONE);
                main_product_en_tv.setVisibility(View.GONE);
                website_en_tv.setVisibility(View.GONE);
                address_en_tv.setVisibility(View.GONE);
                contacts_en_tv.setVisibility(View.GONE);
                job_en_tv.setVisibility(View.GONE);
                tel_en_tv.setVisibility(View.GONE);
                //显示所有中文edittext
                company_name_cn_et.setVisibility(View.VISIBLE);
                industry_cn_et.setVisibility(View.VISIBLE);
                company_size_cn_et.setVisibility(View.VISIBLE);
                main_business_cn_et.setVisibility(View.VISIBLE);
                main_product_cn_et.setVisibility(View.VISIBLE);
                address_cn_et.setVisibility(View.VISIBLE);
                contacts_cn_et.setVisibility(View.VISIBLE);
                job_cn_et.setVisibility(View.VISIBLE);
                //隐藏所有英文edittext
                company_name_en_et.setVisibility(View.GONE);
                industry_en_et.setVisibility(View.GONE);
                company_size_en_et.setVisibility(View.GONE);
                main_business_en_et.setVisibility(View.GONE);
                main_product_en_et.setVisibility(View.GONE);
                address_en_et.setVisibility(View.GONE);
                contacts_en_et.setVisibility(View.GONE);
                job_en_et.setVisibility(View.GONE);

                break;
            //英文
            case R.id.english_rl:
                //将英文字体改为蓝色 将背景样式改为右边倒角白色
                english_tv.setTextColor(color_blue);
                english_rl.setBackgroundResource(right_corner_round_white);
                //将中文字体改为白色 将背景样式改为左边蓝色
                chinese_tv.setTextColor(color_white);
                chinese_rl.setBackgroundResource(left_corner_round_blue);
                //隐藏所有中文的textView
                logo_ch_tv.setVisibility(View.GONE);
                company_name_ch_tv.setVisibility(View.GONE);
                industry_ch_tv.setVisibility(View.GONE);
                company_scale_ch_tv.setVisibility(View.GONE);
                main_business_ch_tv.setVisibility(View.GONE);
                main_product_ch_tv.setVisibility(View.GONE);
                website_ch_tv.setVisibility(View.GONE);
                address_ch_tv.setVisibility(View.GONE);
                contacts_ch_tv.setVisibility(View.GONE);
                job_ch_tv.setVisibility(View.GONE);
                tel_ch_tv.setVisibility(View.GONE);
                //显示所有英文textview
                logo_en_tv.setVisibility(View.VISIBLE);
                company_name_en_tv.setVisibility(View.VISIBLE);
                industry_en_tv.setVisibility(View.VISIBLE);
                company_scale_en_tv.setVisibility(View.VISIBLE);
                main_business_en_tv.setVisibility(View.VISIBLE);
                main_product_en_tv.setVisibility(View.VISIBLE);
                website_en_tv.setVisibility(View.VISIBLE);
                address_en_tv.setVisibility(View.VISIBLE);
                contacts_en_tv.setVisibility(View.VISIBLE);
                job_en_tv.setVisibility(View.VISIBLE);
                tel_en_tv.setVisibility(View.VISIBLE);

                //显示所有英文edittext
                company_name_en_et.setVisibility(View.VISIBLE);
                industry_en_et.setVisibility(View.VISIBLE);
                company_size_en_et.setVisibility(View.VISIBLE);
                main_business_en_et.setVisibility(View.VISIBLE);
                main_product_en_et.setVisibility(View.VISIBLE);
                address_en_et.setVisibility(View.VISIBLE);
                contacts_en_et.setVisibility(View.VISIBLE);
                job_en_et.setVisibility(View.VISIBLE);

                //隐藏所有中文edittext
                company_name_cn_et.setVisibility(View.GONE);
                industry_cn_et.setVisibility(View.GONE);
                company_size_cn_et.setVisibility(View.GONE);
                main_business_cn_et.setVisibility(View.GONE);
                main_product_cn_et.setVisibility(View.GONE);
                address_cn_et.setVisibility(View.GONE);
                contacts_cn_et.setVisibility(View.GONE);
                job_cn_et.setVisibility(View.GONE);
                break;
            //中文部分上传图片
            case R.id.company_logo_cn_iv:
                from = Location.BOTTOM.ordinal();
                initPopupWindow();
                break;
            //保存
            case R.id.save:
                saveBaseInfo();
                break;
        }

    }


    //得到用户输入的数据
    private void getData() {
        company_logo_cn_iv.setDrawingCacheEnabled(true);
        drawingCache = company_logo_cn_iv.getDrawingCache();

        ch_companyname = company_name_cn_et.getText().toString().trim();
        ch_industry = industry_cn_et.getText().toString().trim();
        ch_companysize = company_size_cn_et.getText().toString().trim();
        ch_mainbusiness = main_business_cn_et.getText().toString().trim();
        ch_mainproduct = main_product_cn_et.getText().toString().trim();
        ch_address = address_cn_et.getText().toString().trim();
        ch_contacts = contacts_cn_et.getText().toString().trim();
        ch_job = job_cn_et.getText().toString().trim();

        en_companyname = company_name_en_et.getText().toString().trim();
        en_industry = industry_en_et.getText().toString().trim();
        en_companysize = company_size_en_et.getText().toString().trim();
        en_mainbusiness = main_business_en_et.getText().toString().trim();
        en_mainproduct = main_product_en_et.getText().toString().trim();
        en_address = address_en_et.getText().toString().trim();
        en_contacts = contacts_en_et.getText().toString().trim();
        en_job = job_en_et.getText().toString().trim();

        ch_website = official_website_cn_et.getText().toString().trim();
        ch_phone = phone_cn_et.getText().toString().trim();
        ch_email = email_cn_et.getText().toString().trim();
        unique_id = unique_id_et.getText().toString().trim();
    }

    //上传图片的PopupWindow弹出的方向
    public enum Location {
        BOTTOM
    }

    //上传图片弹出的popupWindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_upload, null);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_goods_activitythree, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(BaseDataActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        TextView photo = (TextView) popupWindowView.findViewById(R.id.photo_tv);
        TextView cancel = (TextView) popupWindowView.findViewById(R.id.cancel_tv);
        //相册
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapList.clear();
                selectGallery();
                mPopupWindow.dismiss();
            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(BaseDataActivity.this, 1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                String tag = data.getStringExtra("Tag");
                if (tag.equals("1")) {

                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
                        //加入最后选择的集合
                        finalSelectPaths.add(p);
                        sb.append(p + "|");
                        String picstring = sb.toString();
                        if (!picstring.isEmpty()) {
                            allPictureUrl = picstring.substring(0, picstring.length() - 1);
                        }
                        //加载缩略图
                        Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(p, 200, 240);
                        bitmapList.add(bitmap);
                    }
                    int size = bitmapList.size();
                    for (int i = 0; i < size; i++) {
                        imageViewList.get(i).setImageBitmap(bitmapList.get(i));
                        Bitmap smallBitmap = ImageFactoryandOther.getSmallBitmap(allPictureUrl);
                        encode = ImageFactoryandOther.convertIconToString(smallBitmap);
                    }
                }

            }
        }


    }


    //修改的方法
    public void updata() {
        //获得修改后的数据
        int id = myUser.get_id();
        int user_type = myUser.getUser_type();
        int system_language = myUser.getSystem_language();
        byte[] bytes = BitmapUtils.Bitmap2Bytes(drawingCache);
        //构建需要修改的数据
        myUser = new MyUser(id, user_type, system_language, bytes, ch_companyname, ch_industry, ch_companysize,
                ch_mainbusiness, ch_mainproduct, ch_website, ch_address, ch_contacts, ch_job, ch_phone, ch_email, unique_id,
                en_companyname, en_industry, en_companysize, en_mainbusiness, en_mainproduct, en_address, en_contacts, en_job);
        //修改数据
        MyUserDao myUserDao = new MyUserDao(BaseDataActivity.this);
        int rows = myUserDao.updataData(myUser);
        if (rows == 0) {
            ToastUtil.showToast2(BaseDataActivity.this, R.string.modify_failed);
        } else {
            ToastUtil.showToast2(BaseDataActivity.this, R.string.modify_success);
            company_logo_cn_iv.setDrawingCacheEnabled(false);
            finish();
        }

    }

    //从后台获取用户基本资料的方法
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
        HttpUtils.doPost(BaseDataActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                Log.e("result", result.toString());
                try {
                    String code = result.getString("code");
                    if ("200".equals(code)) {
                        Gson gson = new Gson();
                        BaseInfoBean baseInfoBean = gson.fromJson(result + "", BaseInfoBean.class);
                        BaseInfoBean.UserInfoBean userInfo = baseInfoBean.getUserInfo();
                        if (company_name_cn_et.getText().toString().isEmpty() && contacts_cn_et.getText().toString().isEmpty()) {

                            company_name_cn_et.setText(userInfo.getChCompanyName());
                            industry_cn_et.setText(userInfo.getChIndustryType());
                            company_size_cn_et.setText(userInfo.getChCompanySize());
                            main_business_cn_et.setText(userInfo.getChMainBussiness());
                            main_product_cn_et.setText(userInfo.getChMainProduct());
                            address_cn_et.setText(userInfo.getChAddress());
                            contacts_cn_et.setText(userInfo.getChContactPerson());
                            job_cn_et.setText(userInfo.getChJobTitle());

                            company_name_en_et.setText(userInfo.getEnCompanyName());
                            industry_en_et.setText(userInfo.getEnIndustryType());
                            company_size_en_et.setText(userInfo.getEnCompanySize());
                            main_business_en_et.setText(userInfo.getEnMainBussiness());
                            main_product_en_et.setText(userInfo.getEnMainProduct());
                            address_en_et.setText(userInfo.getEnAddress());
                            contacts_en_et.setText(userInfo.getEnContactPerson());
                            job_en_et.setText(userInfo.getEnJobTitle());

                            official_website_cn_et.setText(userInfo.getWebsite());
                            phone_cn_et.setText(userInfo.getContactPhone());
                            email_cn_et.setText(userInfo.getEmail());

                            if (userInfo.getUniqueId() != null && !userInfo.getUniqueId().isEmpty()) {
                                unique_id_et.setText(userInfo.getUniqueId());
                            }
                            String logo = userInfo.getLogo();
                            Picasso.with(BaseDataActivity.this).load(logo).into(company_logo_cn_iv);
                        }
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


    //更新用户资料的方法  向后台上传个人资料数据
    private void saveBaseInfo() {
        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "user/updateBasicInfo";
        String tag = "Update";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", tag1);
            jsonObject.put("logo", encode);

            jsonObject.put("chCompanyName", company_name_cn_et.getText().toString());
            jsonObject.put("chIndustryType", industry_cn_et.getText().toString());
            jsonObject.put("chCompanySize", company_size_cn_et.getText().toString());
            jsonObject.put("chMainBusiness", main_business_cn_et.getText().toString());
            jsonObject.put("chMainProduct", main_product_cn_et.getText().toString());
            jsonObject.put("chAddress", address_cn_et.getText().toString());
            jsonObject.put("chContact", contacts_cn_et.getText().toString());
            jsonObject.put("chJobTitle", job_cn_et.getText().toString());

            jsonObject.put("enCompanyName", company_name_en_et.getText().toString());
            jsonObject.put("enIndustryType", industry_en_et.getText().toString());
            jsonObject.put("enCompanySize", company_size_en_et.getText().toString());
            jsonObject.put("enMainBusiness", main_business_en_et.getText().toString());
            jsonObject.put("enMainProduct", main_product_en_et.getText().toString());
            jsonObject.put("enAddress", address_en_et.getText().toString());
            jsonObject.put("enContact", contacts_en_et.getText().toString());
            jsonObject.put("enJobTitle", job_en_et.getText().toString());

            jsonObject.put("website", official_website_cn_et.getText().toString());
            jsonObject.put("contactPhone", phone_cn_et.getText().toString());
            jsonObject.put("email", email_cn_et.getText().toString());
            jsonObject.put("uniqueId", unique_id_et.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.doPost(BaseDataActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if ("200".equals(result.getString("code"))) {
                        getData();//得到数据
                        if (myUser == null) {
                            saved();//将得到的数据插入到数据库
                        } else {
                            updata();
                        }
                        myDialog.dialogDismiss();
                    }
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
        MyAppclication.getHttpQueues().cancelAll("Update");
        super.onStop();
    }

    private interface UdateBaseInfo {
        void updateSuccess();
    }

    //保存用户的所有数据 并存入本地数据库的方法
    private void saved() {
        //创建一个实体类
        myUser = new MyUser();
        byte[] bytes;
        if (bitmap != null) {
            bytes = BitmapUtils.Bitmap2Bytes(bitmap);
        } else {
            if (drawingCache != null) {
                bytes = BitmapUtils.Bitmap2Bytes(drawingCache);
            } else {
                Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                bytes = BitmapUtils.Bitmap2Bytes(bitmap_no);
            }
        }
        //将用户的所有数据加载到myUser中
        myUser.setCh_portrait(bytes);
        myUser.setCh_company_name(ch_companyname);
        myUser.setCh_industry_type(ch_industry);
        myUser.setCh_company_size(ch_companysize);
        myUser.setCh_main_business(ch_mainbusiness);
        myUser.setCh_main_product(ch_mainproduct);
        myUser.setCh_official_website(ch_website);
        myUser.setCh_address(ch_address);
        myUser.setCh_contact(ch_contacts);
        myUser.setCh_job(ch_job);
        myUser.setCh_telephone(ch_phone);
        myUser.setCh_email(ch_email);
        myUser.setUnique_id(unique_id_et.getText().toString());
        myUser.setEn_company_name(en_companyname);
        myUser.setEn_industry_type(en_industry);
        myUser.setEn_company_size(en_companysize);
        myUser.setEn_main_bussiness(en_mainbusiness);
        myUser.setEn_main_product(en_mainproduct);
        myUser.setEn_address(en_address);
        myUser.setEn_contact(en_contacts);
        myUser.setEn_job(en_job);

        //创建一个方法层的对象
        MyUserDao myUserDao = new MyUserDao(BaseDataActivity.this);
        //执行该方法中添加的方法
        myUserDao.insertData(myUser);
        finish();
    }


    //拍照和选择相册的方法
    private void selectGallery() {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片,显示
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量 1张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式,选取多张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }


}
