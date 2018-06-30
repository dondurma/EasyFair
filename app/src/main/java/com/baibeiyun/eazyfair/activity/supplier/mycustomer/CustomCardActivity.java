package com.baibeiyun.eazyfair.activity.supplier.mycustomer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.HorizontalListViewAdapter;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.dao.MyCustomerDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.model.FourModel;
import com.baibeiyun.eazyfair.model.OneModel;
import com.baibeiyun.eazyfair.model.ThreeModel;
import com.baibeiyun.eazyfair.model.TwoModel;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.HorizontalListView;
import com.baibeiyun.eazyfair.view.RoundImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomCardActivity extends BaseActivity implements View.OnClickListener {
    private ImageView background_card_iv;
    private RelativeLayout fanhui_rl;
    private RoundImageView logo;
    private TextView name;//姓名
    private TextView job_et;//职位
    private TextView commpany_et;//公司名
    private LinearLayout phone_ll;//电话的监听范围
    private TextView phonenumber_et;//电话
    private LinearLayout email_ll;//email的监听范围
    private TextView email_et;//email
    private LinearLayout address_ll;//地址的监听范围
    private TextView address_et;//地址
    private TextView one_tv;
    private TextView two_tv;
    private TextView three_tv;
    private LinearLayout one_ll;
    private LinearLayout two_ll;
    private LinearLayout three_ll;
    private RelativeLayout four_ll;
    private LinearLayout edit_phone_ll;
    private LinearLayout edit_email_ll;
    private LinearLayout edit_address_ll;
    private LinearLayout call_phone_ll;

    private ArrayList<MyCustomer> li = new ArrayList<>();
    private MyCustomer myCustomer;
    private int id;//得到上一个页面传递过来的客户信息的id

    //popupwindow相关
    private int from = 0;
    private PopupWindow mPopupWindow;
    //从相册选择图片
    private static final int REQ_1 = 1;
    private static final int REQ_2 = 2;
    private Bitmap bm;//公司logo的图片
    private Bitmap bm1;//背景图片的图片


    private LayoutInflater ml;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;


    //----------------------------------

    private String IndustryXML;
    private List<OneModel> oneList;
    private int onePosition;
    private int twoPosition;
    private int threePosition;
    private boolean isTwo = true;
    private boolean isThree = true;
    private boolean isFour = true;

    private HorizontalListView horizontalListView_goods;
    private HorizontalListViewAdapter horizontalListViewAdapter_goods;

    private List<String> tempSelcetGoodsList = new ArrayList<>();//横向ListView中Item的信息
    private List<FourModel> four_list;

    //------------------------
    private LinearLayout edit_fax_ll;//传真
    private TextView fax_tv;
    private LinearLayout edit_web_ll;//网址
    private TextView web_tv;
    private LinearLayout edit_accounts_ll;
    private TextView accounts_tv;
    private LinearLayout edit_remarks_ll;
    private TextView remarks_tv;
    private LinearLayout edit_postcode_ll;
    private TextView postcode_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_card);
        initYuyan();
        initview();
        initDatas();
        initDataforIndustry();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updata();
        if (li != null) {
            li.clear();
            li = null;
        }
        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
        if (oneList != null) {
            oneList.clear();
            oneList = null;
        }
        if (four_list != null) {
            four_list.clear();
            four_list = null;
        }
        if (tempSelcetGoodsList != null) {
            tempSelcetGoodsList.clear();
            tempSelcetGoodsList = null;
        }

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

    private void initview() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        SelectById();
        background_card_iv = (ImageView) findViewById(R.id.background_card_iv);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        logo = (RoundImageView) findViewById(R.id.logo);
        name = (TextView) findViewById(R.id.name);
        job_et = (TextView) findViewById(R.id.job_et);
        commpany_et = (TextView) findViewById(R.id.commpany_et);
        phone_ll = (LinearLayout) findViewById(R.id.phone_ll);
        phonenumber_et = (TextView) findViewById(R.id.phonenumber_et);
        email_ll = (LinearLayout) findViewById(R.id.email_ll);
        email_et = (TextView) findViewById(R.id.email_et);
        address_ll = (LinearLayout) findViewById(R.id.address_ll);
        address_et = (TextView) findViewById(R.id.address_et);
        one_tv = (TextView) findViewById(R.id.one_tv);
        two_tv = (TextView) findViewById(R.id.two_tv);
        three_tv = (TextView) findViewById(R.id.three_tv);
        horizontalListView_goods = (HorizontalListView) findViewById(R.id.four_tv);
        horizontalListView_goods.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tempSelcetGoodsList.remove(position);
                        horizontalListViewAdapter_goods.notifyDataSetChanged();
                    }
                });
        one_ll = (LinearLayout) findViewById(R.id.one_ll);
        two_ll = (LinearLayout) findViewById(R.id.two_ll);
        three_ll = (LinearLayout) findViewById(R.id.three_ll);
        four_ll = (RelativeLayout) findViewById(R.id.four_ll);
        edit_phone_ll = (LinearLayout) findViewById(R.id.edit_phone_ll);
        edit_email_ll = (LinearLayout) findViewById(R.id.edit_email_ll);
        edit_address_ll = (LinearLayout) findViewById(R.id.edit_address_ll);
        call_phone_ll = (LinearLayout) findViewById(R.id.call_phone_ll);

        edit_fax_ll = (LinearLayout) findViewById(R.id.edit_fax_ll);
        fax_tv = (TextView) findViewById(R.id.fax_tv);
        edit_web_ll = (LinearLayout) findViewById(R.id.edit_web_ll);
        web_tv = (TextView) findViewById(R.id.web_tv);
        edit_accounts_ll = (LinearLayout) findViewById(R.id.edit_accounts_ll);
        accounts_tv = (TextView) findViewById(R.id.accounts_tv);
        edit_remarks_ll = (LinearLayout) findViewById(R.id.edit_remarks_ll);
        remarks_tv = (TextView) findViewById(R.id.remarks_tv);
        edit_postcode_ll = (LinearLayout) findViewById(R.id.edit_postcode_ll);
        postcode_tv = (TextView) findViewById(R.id.postcode_tv);
        edit_fax_ll.setOnClickListener(this);
        edit_web_ll.setOnClickListener(this);
        edit_accounts_ll.setOnClickListener(this);
        edit_remarks_ll.setOnClickListener(this);
        edit_postcode_ll.setOnClickListener(this);

        background_card_iv.setOnClickListener(this);
        fanhui_rl.setOnClickListener(this);

        logo.setOnClickListener(this);
        name.setOnClickListener(this);
        job_et.setOnClickListener(this);
        commpany_et.setOnClickListener(this);

        edit_phone_ll.setOnClickListener(this);
        edit_email_ll.setOnClickListener(this);
        edit_address_ll.setOnClickListener(this);
        call_phone_ll.setOnClickListener(this);
        one_ll.setOnClickListener(this);
        two_ll.setOnClickListener(this);
        three_ll.setOnClickListener(this);
        four_ll.setOnClickListener(this);
    }

    private void initDatas() {
        if (myCustomer.getBackground_pic() != null) {
            byte[] background_pic = myCustomer.getBackground_pic();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(background_pic);
            background_card_iv.setImageBitmap(bitmap);
        }

        if (myCustomer.getCompany_logo() != null) {
            byte[] company_logo = myCustomer.getCompany_logo();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(company_logo);
            logo.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
            logo.setImageBitmap(bitmap);
        }

        if (myCustomer.getName() != null) {
            name.setText(myCustomer.getName());
        }
        if (myCustomer.getJob_position() != null) {
            job_et.setText(myCustomer.getJob_position());
        }
        if (myCustomer.getCompany_name() != null) {
            commpany_et.setText(myCustomer.getCompany_name());
        }
        if (myCustomer.getPhone() != null) {
            phonenumber_et.setText(myCustomer.getPhone());
        }

        email_et.setText(myCustomer.getEmail());

        if (myCustomer.getCompany_address() != null) {
            address_et.setText(myCustomer.getCompany_address());
        }

        if (myCustomer.getCustomer_ohter() != null) {
            remarks_tv.setText(myCustomer.getCustomer_ohter());
        }
        if (myCustomer.getFax() != null) {
            fax_tv.setText(myCustomer.getFax());
        }

        if (myCustomer.getWeb() != null) {
            web_tv.setText(myCustomer.getWeb());
        }
        if (myCustomer.getIcq() != null) {
            accounts_tv.setText(myCustomer.getIcq());
        }
        if (myCustomer.getPostcode() != null) {
            postcode_tv.setText(myCustomer.getPostcode());
        }
        String industry = myCustomer.getIndustry_type();
        String[] split = industry.split("\\|");
        if (split.length == 1) {
            String s1 = split[0];
            one_tv.setText(s1);
        } else if (split.length == 2) {
            String s1 = split[0];
            String s2 = split[1];
            one_tv.setText(s1);
            two_tv.setText(s2);
        } else if (split.length == 3) {
            String s1 = split[0];
            String s2 = split[1];
            String s3 = split[2];
            one_tv.setText(s1);
            two_tv.setText(s2);
            three_tv.setText(s3);
        } else if (split.length == 4) {
            String s1 = split[0];
            String s2 = split[1];
            String s3 = split[2];
            String s4 = split[3];
            one_tv.setText(s1);
            two_tv.setText(s2);
            three_tv.setText(s3);
            String[] split1 = s4.split("\\，");
            for (String s : split1) {
                tempSelcetGoodsList.add(s);
                horizontalListViewAdapter_goods = new HorizontalListViewAdapter(CustomCardActivity.this, tempSelcetGoodsList);
                horizontalListView_goods.setAdapter(horizontalListViewAdapter_goods);
                horizontalListViewAdapter_goods.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //背景图片
            case R.id.background_card_iv:
                initPopupWindow(1);
                break;
            //公司logo
            case R.id.logo:
                initPopupWindow(2);
                break;
            //姓名
            case R.id.name:
                showpopupWindow(1);
                break;
            //职位
            case R.id.job_et:
                showpopupWindow(2);
                break;
            //公司名
            case R.id.commpany_et:
                showpopupWindow(3);
                break;
            //电话的监听
            case R.id.edit_phone_ll:
                showpopupWindow(4);
                break;
            //email的监听
            case R.id.edit_email_ll:
                showpopupWindow(5);
                break;
            //公司地址的监听
            case R.id.edit_address_ll:
                showpopupWindow(6);
                break;
            //跳转到拨号盘
            case R.id.call_phone_ll:
                dialPhoneNumber(phonenumber_et.getText().toString().trim());
                break;
            //行业一
            case R.id.one_ll:
                popupwindow(1);
                break;
            //行业二
            case R.id.two_ll:
                if (isTwo) {
                    popupwindow(2);
                }
                break;
            //行业三
            case R.id.three_ll:
                if (isThree) {
                    popupwindow(3);
                }
                break;
            //行业四
            case R.id.four_ll:
                popupwindow2();
                break;
            //编辑传真
            case R.id.edit_fax_ll:
                showpopupWindow(7);
                break;
            //编辑网址
            case R.id.edit_web_ll:
                showpopupWindow(8);
                break;
            //编辑个人社交账号
            case R.id.edit_accounts_ll:
                showpopupWindow(9);
                break;
            //编辑备注
            case R.id.edit_remarks_ll:
                showpopupWindow(10);
                break;
            case R.id.edit_postcode_ll:
                showpopupWindow(11);
                break;
        }
    }

    //根据id来查询
    public ArrayList<MyCustomer> SelectById() {
        li.clear();
        //创建一个dao层对象
        MyCustomerDao myCustomerDao = new MyCustomerDao(getApplicationContext());
        Cursor mycustomer = myCustomerDao.selectforId(id);
        while (mycustomer.moveToNext()) {
            int id = mycustomer.getInt(mycustomer.getColumnIndex("_id"));
            String name = mycustomer.getString(mycustomer.getColumnIndex("name"));
            String company_name = mycustomer.getString(mycustomer.getColumnIndex("company_name"));
            String phone = mycustomer.getString(mycustomer.getColumnIndex("phone"));
            String email = mycustomer.getString(mycustomer.getColumnIndex("email"));
            String company_address = mycustomer.getString(mycustomer.getColumnIndex("company_address"));
            String industry = mycustomer.getString(mycustomer.getColumnIndex("industry_type"));
            String job_position = mycustomer.getString(mycustomer.getColumnIndex("job_position"));
            byte[] company_logos = mycustomer.getBlob(mycustomer.getColumnIndex("company_logo"));
            String customer_ohter = mycustomer.getString(mycustomer.getColumnIndex("customer_ohter"));
            String create_time = mycustomer.getString(mycustomer.getColumnIndex("create_time"));
            int customer_type = mycustomer.getInt(mycustomer.getColumnIndex("customer_type"));
            String department = mycustomer.getString(mycustomer.getColumnIndex("department"));

            String fax = mycustomer.getString(mycustomer.getColumnIndex("fax"));
            String pager = mycustomer.getString(mycustomer.getColumnIndex("pager"));
            String web = mycustomer.getString(mycustomer.getColumnIndex("web"));
            String postcode = mycustomer.getString(mycustomer.getColumnIndex("postcode"));
            String icq = mycustomer.getString(mycustomer.getColumnIndex("icq"));
            String unique_id = mycustomer.getString(mycustomer.getColumnIndex("unique_id"));
            byte[] background_pics = mycustomer.getBlob(mycustomer.getColumnIndex("background_pic"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logos, customer_ohter,
                    create_time, customer_type, department, fax, pager, web, postcode, icq, unique_id, background_pics);
            li.add(myCustomer);
        }
        mycustomer.close();
        return li;
    }

    //上传图片的PopupWindow弹出的方向
    public enum Location {
        BOTTOM
    }

    //上传图片弹出的popupWindow
    private void initPopupWindow(final int tag) {
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
        backgroundAlpha(CustomCardActivity.this, 0.5f);
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
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (tag == 1) {
                    //调用系统相册
                    startActivityForResult(intent, REQ_1);
                } else if (tag == 2) {
                    //调用系统相册
                    startActivityForResult(intent, REQ_2);
                }
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
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(CustomCardActivity.this, 1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片路径
        if (requestCode == REQ_2 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            assert c != null;
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath, 2);
            c.close();
        } else if (requestCode == REQ_1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            assert c != null;
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath, 1);
            c.close();
        }

    }

    // 加载图片
    private void showImage(String imaePath, int type) {
        if (type == 1) {
            bm1 = ImageFactoryandOther.ratio(imaePath, 300, 400);
            background_card_iv.setImageBitmap(bm1);


        } else if (type == 2) {
            bm = ImageFactoryandOther.ratio(imaePath, 200, 260);
            logo.setImageBitmap(bm);
        }

    }

    private void updata() {
        if (myCustomer.getBackground_pic() != null) {
            byte[] background_pic = myCustomer.getBackground_pic();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(background_pic);
            background_card_iv.setImageBitmap(bitmap);
        }
        int id = myCustomer.get_id();
        String create_time = myCustomer.getCreate_time();
        int customer_type = myCustomer.getCustomer_type();

        String name_data = name.getText().toString().trim();
        String job_data = job_et.getText().toString().trim();
        String companyname_data = commpany_et.getText().toString().trim();
        String phone_data = phonenumber_et.getText().toString().trim();
        String email_data = email_et.getText().toString().trim();
        String address_data = address_et.getText().toString().trim();
        String fax_data = null;
        if (!fax_tv.getText().toString().trim().equals("")) {
            fax_data = fax_tv.getText().toString().trim();
        } else {
            fax_data = " ";
        }
        String web_data = null;
        if (!web_tv.getText().toString().trim().equals("")) {
            web_data = web_tv.getText().toString().trim();
        } else {
            web_data = " ";
        }
        String accounts_data = null;
        if (!accounts_tv.getText().toString().trim().equals("")) {
            accounts_data = accounts_tv.getText().toString().trim();
        } else {
            accounts_data = " ";
        }
        String remarks_data = null;
        if (!remarks_tv.getText().toString().trim().equals("")) {
            remarks_data = remarks_tv.getText().toString().trim();
        } else {
            remarks_data = " ";
        }
        String postcode_data = null;
        if (!postcode_tv.getText().toString().trim().equals("")) {
            postcode_data = postcode_tv.getText().toString().trim();
        } else {
            postcode_data = " ";
        }

        String industry_one_data = one_tv.getText().toString().trim();
        String industry_two_data = two_tv.getText().toString().trim();
        String industry_three_data = three_tv.getText().toString().trim();
        String s = ListToString(tempSelcetGoodsList);
        String industry_data = null;
        if (s != null) {
            industry_data = industry_one_data + "|" + industry_two_data + "|" + industry_three_data + "|" + s;
        } else {
            industry_data = industry_one_data + "|" + industry_two_data + "|" + industry_three_data;
        }


        byte[] company_logo;
        if (bm != null) {
            company_logo = BitmapUtils.compressBmpFromBmp(bm);
        } else {
            company_logo = myCustomer.getCompany_logo();
        }
        //背景图片
        byte[] background_pic;
        if (bm1 != null) {
            background_pic = BitmapUtils.compressBmpFromBmp(bm1);
        } else {
            background_pic = myCustomer.getBackground_pic();
        }


        //构建需要修改的数据
        myCustomer = new MyCustomer(id, name_data, companyname_data, phone_data, email_data, address_data, industry_data, job_data, company_logo, remarks_data, fax_data, web_data, accounts_data, create_time, customer_type, postcode_data, myCustomer.getUnique_id(), background_pic);
        MyCustomerDao myCustomerDao = new MyCustomerDao(this);
        int rows = myCustomerDao.updatedata2(myCustomer);
        if (rows == 0) {
            ToastUtil.showToast2(CustomCardActivity.this, R.string.modify_failed);
        } else {
            ToastUtil.showToast2(CustomCardActivity.this, R.string.modify_success);
        }
    }

    private void showpopupWindow(final int type) {
        WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = systemService.getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();

        ml = LayoutInflater.from(CustomCardActivity.this);
        View view = ml.inflate(R.layout.updata_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);

        WindowManager wm = this.getWindowManager();
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(width * 95 / 100);
//        mPopupWindow.setHeight(height/4);

        final EditText inputcontent = (EditText) view.findViewById(R.id.et_pup);
        if (type == 4 || type == 11) {
            inputcontent.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        Button cancel = (Button) view.findViewById(R.id.cancel_bt);
        Button ok = (Button) view.findViewById(R.id.ok_bt);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    name.setText(inputcontent.getText().toString().trim());
                } else if (type == 2) {
                    job_et.setText(inputcontent.getText().toString().trim());
                } else if (type == 3) {
                    commpany_et.setText(inputcontent.getText().toString().trim());
                } else if (type == 4) {
                    phonenumber_et.setText(inputcontent.getText().toString().trim());
                } else if (type == 5) {
                    email_et.setText(inputcontent.getText().toString().trim());
                } else if (type == 6) {
                    address_et.setText(inputcontent.getText().toString().trim());
                } else if (type == 7) {
                    fax_tv.setText(inputcontent.getText().toString().trim());
                } else if (type == 8) {
                    web_tv.setText(inputcontent.getText().toString().trim());
                } else if (type == 9) {
                    accounts_tv.setText(inputcontent.getText().toString().trim());
                } else if (type == 10) {
                    remarks_tv.setText(inputcontent.getText().toString().trim());
                } else if (type == 11) {
                    postcode_tv.setText(inputcontent.getText().toString().trim());
                }
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(CustomCardActivity.this, 0.5f);//0.0-1.0

        switch (type) {
            case 1:
                mPopupWindow.showAtLocation(name, Gravity.CENTER, 0, 0);
                break;
            case 2:
                mPopupWindow.showAtLocation(job_et, Gravity.CENTER, 0, 0);
                break;
            case 3:
                mPopupWindow.showAtLocation(commpany_et, Gravity.CENTER, 0, 0);
                break;
            case 4:
                mPopupWindow.showAtLocation(phonenumber_et, Gravity.CENTER, 0, 0);
                break;
            case 5:
                mPopupWindow.showAtLocation(email_et, Gravity.CENTER, 0, 0);
                break;
            case 6:
                mPopupWindow.showAtLocation(address_et, Gravity.CENTER, 0, 0);
                break;
            case 7:
                mPopupWindow.showAtLocation(fax_tv, Gravity.CENTER, 0, 0);
                break;
            case 8:
                mPopupWindow.showAtLocation(web_tv, Gravity.CENTER, 0, 0);
                break;
            case 9:
                mPopupWindow.showAtLocation(accounts_tv, Gravity.CENTER, 0, 0);
                break;
            case 10:
                mPopupWindow.showAtLocation(remarks_tv, Gravity.CENTER, 0, 0);
                break;
            case 11:
                mPopupWindow.showAtLocation(postcode_tv, Gravity.CENTER, 0, 0);
                break;
        }
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(CustomCardActivity.this, 1f);
            }
        });
    }

    //跳转到拨号盘界面的方法
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void initDataforIndustry() {
        IndustryXML = getRawIndustry().toString();
        try {
            analysisXMLforIndustry(IndustryXML);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        // 初始化Button数据
//        one_tv.setText(oneList.get(0).getOne());
//        two_tv.setText(oneList.get(0).getTwo_list().get(0).getTwo());
//        three_tv.setText(oneList.get(0).getTwo_list().get(0).getThree_list().get(0).getThree());

        // 初始化下标
        onePosition = 0;
        twoPosition = 0;
        threePosition = 0;
    }

    // 获取地区raw里面的地址xml内容
    private StringBuffer getRawIndustry() {
        InputStream in = null;
        if (language.getTag().equals("ch")) {
            in = getResources().openRawResource(R.raw.industry_ch);
        } else if (language.getTag().equals("en")) {
            in = getResources().openRawResource(R.raw.industry_en);
        }
        assert in != null;
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
            isr.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public void analysisXMLforIndustry(String data) throws XmlPullParserException {
        try {
            OneModel oneModel = null;
            TwoModel twoModel = null;
            ThreeModel threeModel = null;
            FourModel fourModel = null;

            List<TwoModel> twoList = null;
            List<ThreeModel> threeList = null;
            List<FourModel> fourList = null;


            InputStream xmlData = new ByteArrayInputStream(data.getBytes("UTF-8"));
            XmlPullParserFactory factory = null;
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser;
            parser = factory.newPullParser();
            parser.setInput(xmlData, "utf-8");
            String currentTag = null;

            String one;
            String two;
            String three;
            String four;

            int type = parser.getEventType();

            while (type != XmlPullParser.END_DOCUMENT) {
                String typeName = parser.getName();

                if (type == XmlPullParser.START_TAG) {
                    if ("Industry".equals(typeName)) {
                        oneList = new ArrayList<>();

                    } else if ("One".equals(typeName)) {
                        one = parser.getAttributeValue(0);
                        oneModel = new OneModel();
                        oneModel.setOne(one);
                        twoList = new ArrayList<>();
                    } else if ("Two".equals(typeName)) {
                        two = parser.getAttributeValue(0);
                        twoModel = new TwoModel();
                        twoModel.setTwo(two);
                        threeList = new ArrayList<>();

                    } else if ("Three".equals(typeName)) {
                        three = parser.getAttributeValue(0);
                        threeModel = new ThreeModel();
                        threeModel.setThree(three);
                        fourList = new ArrayList<>();

                    } else if ("Four".equals(typeName)) {
                        four = parser.getAttributeValue(0);
                        fourModel = new FourModel();
                        fourModel.setFour(four);

                    }
                    currentTag = typeName;
                } else if (type == XmlPullParser.END_TAG) {
                    if ("Industry".equals(typeName)) {

                    } else if ("One".equals(typeName)) {
                        oneModel.setTwo_list(twoList);
                        oneList.add(oneModel);
                    } else if ("Two".equals(typeName)) {
                        twoModel.setThree_list(threeList);
                        twoList.add(twoModel);
                    } else if ("Three".equals(typeName)) {
                        threeModel.setFour_list(fourList);
                        threeList.add(threeModel);

                    } else if ("Four".equals(typeName)) {
                        fourList.add(fourModel);
                    }

                } else if (type == XmlPullParser.TEXT) {
                    currentTag = null;
                }

                type = parser.next();
            }


        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }

    //行业联动的popupWindow
    private void popupwindow(final int type) {
        ml = LayoutInflater.from(CustomCardActivity.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        ListView lv = (ListView) view.findViewById(R.id.popup_lv);
        if (type == 1) {
            OneAdapter countryAdapter = new OneAdapter(oneList);
            lv.setAdapter(countryAdapter);
        } else if (type == 2) {
            TwoAdapter twoAdapter = new TwoAdapter(oneList.get(onePosition).getTwo_list());
            lv.setAdapter(twoAdapter);
        } else if (type == 3) {
            ThreeAdapter threeAdapter = new ThreeAdapter(oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list());
            lv.setAdapter(threeAdapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1) {
                    onePosition = position;
                    one_tv.setText(oneList.get(position).getOne());
                    if (oneList.get(position).getTwo_list().size() < 1) {
                        two_tv.setText("");
                        three_tv.setText("");
                        isTwo = false;
                        isThree = false;
                        isFour = false;
                    } else {
                        isTwo = oneList.get(position).getTwo_list().size() != 1;

                        two_tv.setText(oneList.get(position).getTwo_list().get(0).getTwo());
                        twoPosition = 0;
                        if (oneList.get(position).getTwo_list().get(0).getThree_list().size() < 1) {
                            three_tv.setText("");
                            isThree = false;
                            isFour = false;
                        } else {
                            isThree = true;
                            three_tv.setText(oneList.get(position).getTwo_list().get(0).getThree_list().get(0).getThree());
                            threePosition = 0;
                            if (oneList.get(position).getTwo_list().get(0).getThree_list().get(0).getFour_list().size() < 1) {
                                isFour = false;
                            }
                        }
                    }

                } else if (type == 2) {
                    twoPosition = position;
                    two_tv.setText(oneList.get(onePosition).getTwo_list().get(position).getTwo());

                    if (oneList.get(onePosition).getTwo_list().get(position).getThree_list().size() < 1) {
                        three_tv.setText("");
                        isThree = false;
                        isFour = false;
                    } else {
                        isThree = true;
                        three_tv.setText(oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(0).getThree());
                        threePosition = 0;
                        if (oneList.get(onePosition).getTwo_list().get(position).getThree_list().get(0).getFour_list().size() < 1) {
                            isFour = false;
                        }
                    }
                } else if (type == 3) {
                    threePosition = position;
                    three_tv.setText(oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(position).getThree());
                    isFour = oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(position).getFour_list().size() >= 1;
                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(CustomCardActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(CustomCardActivity.this, 1f);
            }
        });
    }

    class OneAdapter extends BaseAdapter {
        public List<OneModel> adapter_list;

        public OneAdapter(List<OneModel> list) {
            adapter_list = list;
        }

        @Override
        public int getCount() {
            return adapter_list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(CustomCardActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getOne());
            return tv;
        }

    }

    class TwoAdapter extends BaseAdapter {
        public List<TwoModel> adapter_list;

        public TwoAdapter(List<TwoModel> list) {
            adapter_list = list;
        }

        @Override
        public int getCount() {
            return adapter_list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup arg2) {
            TextView tv = new TextView(CustomCardActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getTwo());
            return tv;
        }

    }

    class ThreeAdapter extends BaseAdapter {
        public List<ThreeModel> adapter_list;

        public ThreeAdapter(List<ThreeModel> list) {
            adapter_list = list;
        }

        @Override
        public int getCount() {
            return adapter_list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup arg2) {
            TextView tv = new TextView(CustomCardActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getThree());
            return tv;
        }

    }

    class FourAdapter extends BaseAdapter {
        public List<FourModel> adapter_list;

        public FourAdapter(List<FourModel> list) {
            adapter_list = list;
        }

        @Override
        public int getCount() {
            return adapter_list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup arg2) {
            TextView tv = new TextView(CustomCardActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getFour());
            return tv;
        }

    }

    private void popupwindow2() {
        ml = LayoutInflater.from(CustomCardActivity.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        ListView lv = (ListView) view.findViewById(R.id.popup_lv);

        four_list = oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(threePosition).getFour_list();
        FourAdapter fourAdapter = new FourAdapter(four_list);
        lv.setAdapter(fourAdapter);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FourModel fourModel = four_list.get(position);
                        if (!tempSelcetGoodsList.contains(fourModel.getFour())) {
                            tempSelcetGoodsList.add(fourModel.getFour());
                        }
                        horizontalListViewAdapter_goods = new HorizontalListViewAdapter(CustomCardActivity.this, tempSelcetGoodsList);
                        horizontalListView_goods.setAdapter(horizontalListViewAdapter_goods);
                        for (int i = 0; i < tempSelcetGoodsList.size(); i++) {
                            //   Log.d("tempSelcetGoodsList", "onItemClick: " + tempSelcetGoodsList.get(i));
                        }
                        mPopupWindow.dismiss();
                    }
                }
        );
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(CustomCardActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(CustomCardActivity.this, 1f);
                    }
                }
        );
    }

    //将从list集合中获得的数据以逗号拼接字符串
    public String ListToString(List<String> tempSelcetGoodsList) {
        if (tempSelcetGoodsList == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (String string : tempSelcetGoodsList) {
            if (flag) {
                sb.append("，");
            } else {
                flag = true;
            }
            sb.append(string);
        }
        return sb.toString();
    }


}
