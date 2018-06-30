package com.baibeiyun.eazyfair.activity.supplier.mycustomer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.HorizontalListView;

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

public class EditCardForOCRActivity extends BaseActivity implements View.OnClickListener {
    private TextView again_photo_rl, save_rl;//重新拍摄，保存
    private ImageView custom_card_iv;//名片图片
    private EditText custom_name_et;//名字
    private EditText custom_job_et;//职位
    private EditText phonenumber_et;//电话
    private EditText companyname_et;//公司名称
    private EditText email_et;//邮箱
    private EditText company_address_et;//公司地址

    private EditText department_et;//部分
    private EditText fax_et;//传真
    private EditText pager_et;//传呼机
    private EditText web_et;//网址
    private EditText postcode_et;//邮编
    private EditText icq_et;//社交账号
    private EditText unique_id_et;

    private EditText remark_et;//其他

    private String url;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    private ArrayList<MyCustomer> myCustomers = new ArrayList<>();
    private MyCustomer myCustomer;

    private String name, job, phone, companyname, eamil, companyaddress, industry_type, remark, department, fax, pager, web, postcode, icq, unique_id;
    private Bitmap bitmap;
    private byte[] bytes_bg;

    //------------2017-2-22---有关行业联动----------------
    private LinearLayout one_ll, two_ll, three_ll;
    private RelativeLayout four_ll;
    private TextView one_tv, two_tv, three_tv;
    private HorizontalListView four_tv;

    private HorizontalListViewAdapter horizontalListViewAdapter_goods;

    private String IndustryXML;
    private List<OneModel> oneList;
    private int onePosition;
    private int twoPosition;
    private int threePosition;

    private List<String> tempSelcetGoodsList = new ArrayList<>();//横向ListView中Item的信息

    private LayoutInflater ml;
    private PopupWindow mPopupWindow;
    private boolean isTwo = true;
    private boolean isThree = true;
    private boolean isFour = true;

    private List<FourModel> four_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        initYuyan();
        initview();
        initData();
        selectInfo();
        initDataforIndustry();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
        if (myCustomers != null) {
            myCustomers.clear();
            myCustomers = null;
        }
        if (oneList != null) {
            oneList.clear();
            oneList = null;
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


    private void initData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String company = intent.getStringExtra("company");
        String department = intent.getStringExtra("department");//部门
        String jobtitle = intent.getStringExtra("jobtitle");
        String tel_main = intent.getStringExtra("tel_main");//电话
        String tel_mobile = intent.getStringExtra("tel_mobile");
        String tel_home = intent.getStringExtra("tel_home");//家庭电话
        String tel_inter = intent.getStringExtra("tel_inter");//直拨电话
        String fax = intent.getStringExtra("fax");//传真
        String pager = intent.getStringExtra("pager");//传呼机
        String web = intent.getStringExtra("web");//网址
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String postcode = intent.getStringExtra("postcode");//邮编
        String icq = intent.getStringExtra("icq");//QQ或其他的社交软件

        url = intent.getStringExtra("url");

        custom_name_et.setText(name);
        custom_job_et.setText(jobtitle);
        phonenumber_et.setText(tel_mobile);
        companyname_et.setText(company);
        email_et.setText(email);
        company_address_et.setText(address);
        bitmap = BitmapUtils.compressImageFromFile(url);
        custom_card_iv.setImageBitmap(bitmap);

        department_et.setText(department);
        fax_et.setText(fax);
        pager_et.setText(pager);
        web_et.setText(web);
        postcode_et.setText(postcode);
        icq_et.setText(icq);
    }

    private void initview() {
        again_photo_rl = (TextView) findViewById(R.id.again_photo_rl);
        save_rl = (TextView) findViewById(R.id.save_rl);
        custom_card_iv = (ImageView) findViewById(R.id.custom_card_iv);
        custom_name_et = (EditText) findViewById(R.id.custom_name_et);
        custom_job_et = (EditText) findViewById(R.id.custom_job_et);
        phonenumber_et = (EditText) findViewById(R.id.phonenumber_et);
        companyname_et = (EditText) findViewById(R.id.companyname_et);
        email_et = (EditText) findViewById(R.id.email_et);
        company_address_et = (EditText) findViewById(R.id.company_address_et);
        department_et = (EditText) findViewById(R.id.department_et);
        fax_et = (EditText) findViewById(R.id.fax_et);
        pager_et = (EditText) findViewById(R.id.pager_et);
        web_et = (EditText) findViewById(R.id.web_et);
        postcode_et = (EditText) findViewById(R.id.postcode_et);
        icq_et = (EditText) findViewById(R.id.icq_et);
        remark_et = (EditText) findViewById(R.id.remark_et);
        unique_id_et = (EditText) findViewById(R.id.unique_id_et);

        one_ll = (LinearLayout) findViewById(R.id.one_ll);
        two_ll = (LinearLayout) findViewById(R.id.two_ll);
        three_ll = (LinearLayout) findViewById(R.id.three_ll);
        four_ll = (RelativeLayout) findViewById(R.id.four_ll);
        one_tv = (TextView) findViewById(R.id.one_tv);
        two_tv = (TextView) findViewById(R.id.two_tv);
        three_tv = (TextView) findViewById(R.id.three_tv);
        four_tv = (HorizontalListView) findViewById(R.id.four_tv);
        four_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempSelcetGoodsList.remove(position);
                horizontalListViewAdapter_goods.notifyDataSetChanged();
            }
        });
        one_ll.setOnClickListener(EditCardForOCRActivity.this);
        two_ll.setOnClickListener(EditCardForOCRActivity.this);
        three_ll.setOnClickListener(EditCardForOCRActivity.this);
        four_ll.setOnClickListener(EditCardForOCRActivity.this);
        again_photo_rl.setOnClickListener(EditCardForOCRActivity.this);
        save_rl.setOnClickListener(EditCardForOCRActivity.this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.again_photo_rl:
                finish();
                break;
            case R.id.save_rl:
                getData();
                if (myCustomer != null) {
                    if (myCustomer.getName().equals(name) && myCustomer.getCompany_name().equals(companyname) && myCustomer.getUnique_id().equals(unique_id)) {
                        Toast.makeText(EditCardForOCRActivity.this, R.string.not_add, Toast.LENGTH_SHORT).show();
                    } else {
                        saved();
                        Toast.makeText(EditCardForOCRActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                        SelectQRcodeorOCRActivity.activity.finish();
                    }
                } else {
                    saved();
                    SelectQRcodeorOCRActivity.activity.finish();
                }
                break;
            //一级行业类型
            case R.id.one_ll:
                popupwindow(1);
                break;
            //二级行业类型
            case R.id.two_ll:
                if (isTwo) {
                    popupwindow(2);
                }
                break;
            //三级行业类型
            case R.id.three_ll:
                if (isThree) {
                    popupwindow(3);
                }
                break;
            //四级行业类型
            case R.id.four_ll:
                popupwindow2();
                break;
        }

    }

    private void getData() {
        //得到用户输入的数据
        name = custom_name_et.getText().toString().trim();
        job = custom_job_et.getText().toString().trim();
        phone = phonenumber_et.getText().toString().trim();
        companyname = companyname_et.getText().toString().trim();
        eamil = email_et.getText().toString().trim();
        companyaddress = company_address_et.getText().toString().trim();
        remark = remark_et.getText().toString().trim();

        department = department_et.getText().toString().trim();
        fax = fax_et.getText().toString().trim();
        pager = pager_et.getText().toString().trim();
        web = web_et.getText().toString().trim();
        postcode = postcode_et.getText().toString().trim();
        icq = icq_et.getText().toString().trim();
        if (!unique_id_et.getText().toString().trim().equals("")) {
            unique_id = unique_id_et.getText().toString().trim();
        } else {
            String stringRandom = ImageFactoryandOther.getStringRandom(6);
            unique_id = stringRandom;
        }
        String one_industry = one_tv.getText().toString().trim();//行业一级
        String two_industry = two_tv.getText().toString().trim();//行业二级
        String three_industry = three_tv.getText().toString().trim();//行业三级
        String four_industry = ListToString(tempSelcetGoodsList); //行业四级

        if (!one_industry.equals("") && !two_industry.equals("") && !three_industry.equals("") && !four_industry.equals("")) {
            industry_type = one_industry + "|" + two_industry + "|" + three_industry + "|" + four_industry;
        } else if (!one_industry.equals("") && !two_industry.equals("") && !three_industry.equals("") && four_industry.equals("")) {
            industry_type = one_industry + "|" + two_industry + "|" + three_industry;
        } else if (!one_industry.equals("") && !two_industry.equals("") && three_industry.equals("") && four_industry.equals("")) {
            industry_type = one_industry + "|" + two_industry;
        } else if (!one_industry.equals("") && two_industry.equals("") && three_industry.equals("") && four_industry.equals("")) {
            industry_type = one_industry;
        }
        bytes_bg = BitmapUtils.compressBmpFromBmp(bitmap);
    }

    //保存数据到数据库的方法
    private void saved() {
        if (!name.equals("") && !companyname.equals("") && !eamil.equals("")) {
            byte[] bytes;
            if (bitmap != null) {
                bytes = BitmapUtils.compressBmpFromBmp(bitmap);
            } else {
                Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.a3);
                bytes = BitmapUtils.compressBmpFromBmp(bitmap_no);
            }
            //创建一个实体类对象
            MyCustomer myCustomer = new MyCustomer();
            //将得到的数据加载到myCustomer对象中
            String date = DateUtil.getDate();
            myCustomer.setCreate_time(date);
            myCustomer.setName(name);
            myCustomer.setJob_position(job);
            myCustomer.setPhone(phone);
            myCustomer.setEmail(eamil);
            myCustomer.setCompany_name(companyname);
            myCustomer.setCompany_address(companyaddress);
            myCustomer.setIndustry_type(industry_type);
            myCustomer.setCustomer_ohter(remark);
            myCustomer.setCustomer_type(0);//0-供应商的客户
            myCustomer.setDepartment(department);
            myCustomer.setFax(fax);
            myCustomer.setPager(pager);
            myCustomer.setWeb(web);
            myCustomer.setPostcode(postcode);
            myCustomer.setIcq(icq);
            myCustomer.setUnique_id(unique_id);
            myCustomer.setBackground_pic(bytes);
            myCustomer.setCompany_logo(bytes_bg);
            //创建一个方法层对象
            MyCustomerDao myCustomerDao = new MyCustomerDao(this);
            //执行该方法层添加的方法
            myCustomerDao.insertdata(myCustomer);
            finish();
        } else {
            ToastUtil.showToast2(EditCardForOCRActivity.this, R.string.check_info);
        }

    }

    //查询客户表中的数据
    public ArrayList<MyCustomer> selectInfo() {
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(this);
        Cursor cursor = myCustomerDao.selectAll();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }

    //----------------2017-2-22-有关行业多级联动------------------------------------
    private void initDataforIndustry() {
        IndustryXML = getRawIndustry().toString();
        try {
            analysisXMLforIndustry(IndustryXML);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        // 初始化Button数据
        one_tv.setText(oneList.get(0).getOne());
        two_tv.setText(oneList.get(0).getTwo_list().get(0).getTwo());
        three_tv.setText(oneList.get(0).getTwo_list().get(0).getThree_list().get(0).getThree());

        // 初始化下标
        onePosition = 0;
        twoPosition = 0;
        threePosition = 0;
    }

    // 获取行业raw里面的行业xml内容
    private StringBuffer getRawIndustry() {
        InputStream in = null;
        if (language.getTag().equals("ch")) {
            in = getResources().openRawResource(R.raw.industry_ch);
        } else if (language.getTag().equals("en")) {
            in = getResources().openRawResource(R.raw.industry_en);
        }
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
        ml = LayoutInflater.from(EditCardForOCRActivity.this);
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
        backgroundAlpha(EditCardForOCRActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(EditCardForOCRActivity.this, 1f);
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
            TextView tv = new TextView(EditCardForOCRActivity.this);
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
            TextView tv = new TextView(EditCardForOCRActivity.this);
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
            TextView tv = new TextView(EditCardForOCRActivity.this);
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
            TextView tv = new TextView(EditCardForOCRActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getFour());
            return tv;
        }

    }


    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    private void popupwindow2() {
        ml = LayoutInflater.from(EditCardForOCRActivity.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        ListView lv = (ListView) view.findViewById(R.id.popup_lv);

        four_list = oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(threePosition).getFour_list();
        FourAdapter fourAdapter = new FourAdapter(four_list);
        lv.setAdapter(fourAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          FourModel fourModel = four_list.get(position);
                                          if (!tempSelcetGoodsList.contains(fourModel.getFour())) {
                                              tempSelcetGoodsList.add(fourModel.getFour());
                                          }
                                          horizontalListViewAdapter_goods = new HorizontalListViewAdapter(EditCardForOCRActivity.this, tempSelcetGoodsList);
                                          four_tv.setAdapter(horizontalListViewAdapter_goods);
                                          int size = tempSelcetGoodsList.size();
//                                          for (int i = 0; i < size; i++) {
//                                      Log.d("tempSelcetGoodsList", "onItemClick: " + tempSelcetGoodsList.get(i));
//                                          }
                                          mPopupWindow.dismiss();
                                      }
                                  }
        );
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(EditCardForOCRActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                              @Override
                                              public void onDismiss() {
                                                  backgroundAlpha(EditCardForOCRActivity.this, 1f);
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
