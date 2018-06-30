package com.baibeiyun.eazyfair.activity.buyer.mysupplier;

import android.app.ActionBar;
import android.app.Activity;
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
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.adapter.HorizontalListViewAdapter;
import com.baibeiyun.eazyfair.adapter.MyDiyAdapterForNewCustomerforBuyer;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.dao.MyCustomerDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.model.AreaModel;
import com.baibeiyun.eazyfair.model.CityModel;
import com.baibeiyun.eazyfair.model.CountryModel;
import com.baibeiyun.eazyfair.model.FourModel;
import com.baibeiyun.eazyfair.model.OneModel;
import com.baibeiyun.eazyfair.model.ProvinceModel;
import com.baibeiyun.eazyfair.model.ThreeModel;
import com.baibeiyun.eazyfair.model.TwoModel;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.HorizontalListView;
import com.baibeiyun.eazyfair.view.MyListView;

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
import java.util.Map;

//新建供应商
public class NewSupplierActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private EditText input_custom_xin_et;//姓
    private EditText input_custom_ming_et;//名
    private EditText input_company_name_et;//公司名
    private LinearLayout job_ll;//职位
    private EditText job_other_et;//自定义职位
    private EditText input_phonenumber_et;//联系电话
    private EditText input_postcode_et;//邮编
    private EditText input_email_et;//邮箱地址
    private ImageView add_email_iv;//自定义添加一条email的item
    private MyListView diy_add_lv;//自定义的listview控件
    private LinearLayout country_ll;//国家
    private LinearLayout province_ll;//省
    private LinearLayout city_ll;//城市
    private LinearLayout area_ll;//区县
    private ImageView company_logo_iv;//上传公司logo
    private TextView country_tv;//国家
    private TextView province_tv;//省
    private TextView city_tv;//城市
    private TextView area_tv;//区县
    private EditText detail_address_et;//详细地址
    private EditText unique_id_et;

    private EditText input_other_et;//备注其他
    private Button save_bt;//保存


    //公司地址三级联动相关部分
    private String AddressXML;// xml格式的全球城市信息
    private List<CountryModel> countryList;// 地址列表
    private int countryPosition;
    private int provincePosition;
    private int cityPosition;
    private boolean isProvince = true;
    private boolean isCity = true;
    private boolean isArea = true;


    //职位部分
    private LayoutInflater ml;
    private PopupWindow mPopupWindow;
    private String job = null;
    private TextView job_tv;
    //PopupWindow定义的五种常见货币
    private String[] name_job = {"董事长", "经理", "采购", "销售", "秘书", "其他"};
    private String[] name_job_en = {"Chairman", "manager", "Purchase", "Sale", "secretary", "other"};
    private MyDiyAdapterForNewCustomerforBuyer adapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();

    //得到用户当前输入的数据
    private String name, companyname, jobforcustome, phonenumber, email, address, industry_type, other, unique_id, postcode;
    byte[] bytes_bg;
    private int from = 0;
    private static final int REQ_2 = 2;//从相册选择图片
    private Bitmap bitmap;


    //行业类别多级联动部分
    private LinearLayout one_ll;
    private LinearLayout two_ll;
    private LinearLayout three_ll;
    private RelativeLayout four_ll;
    private TextView one_tv;
    private TextView two_tv;
    private TextView three_tv;

    private String IndustryXML;// xml格式的全球城市信息
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

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    private ArrayList<MyCustomer> myCustomers = new ArrayList<>();
    private MyCustomer myCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_supplier);
        initYuyan();
        initView();
        initData();
        initDataforIndustry();
        selectInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countryList != null) {
            countryList.clear();
            countryList = null;
        }
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (oneList != null) {
            oneList.clear();
            oneList = null;
        }
        if (tempSelcetGoodsList != null) {
            tempSelcetGoodsList.clear();
            tempSelcetGoodsList = null;
        }
        if (four_list != null) {
            four_list.clear();
            four_list = null;
        }
        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
        if (myCustomers != null) {
            myCustomers.clear();
            myCustomers = null;
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

    private void initView() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_custom_xin_et = (EditText) findViewById(R.id.input_custom_xin_et);
        input_custom_ming_et = (EditText) findViewById(R.id.input_custom_ming_et);
        input_company_name_et = (EditText) findViewById(R.id.input_company_name_et);
        job_ll = (LinearLayout) findViewById(R.id.job_ll);
        job_other_et = (EditText) findViewById(R.id.job_other_et);
        input_phonenumber_et = (EditText) findViewById(R.id.input_phonenumber_et);
        input_postcode_et = (EditText) findViewById(R.id.input_postcode_et);
        input_email_et = (EditText) findViewById(R.id.input_email_et);
        add_email_iv = (ImageView) findViewById(R.id.add_email_iv);
        diy_add_lv = (MyListView) findViewById(R.id.diy_add_lv);
        country_ll = (LinearLayout) findViewById(R.id.country_ll);
        province_ll = (LinearLayout) findViewById(R.id.province_ll);
        city_ll = (LinearLayout) findViewById(R.id.city_ll);
        area_ll = (LinearLayout) findViewById(R.id.area_ll);
        country_tv = (TextView) findViewById(R.id.country_tv);
        province_tv = (TextView) findViewById(R.id.province_tv);
        city_tv = (TextView) findViewById(R.id.city_tv);
        area_tv = (TextView) findViewById(R.id.area_tv);
        detail_address_et = (EditText) findViewById(R.id.detail_address_et);
        input_other_et = (EditText) findViewById(R.id.input_other_et);
        save_bt = (Button) findViewById(R.id.save_bt);
        job_tv = (TextView) findViewById(R.id.job_tv);
        company_logo_iv = (ImageView) findViewById(R.id.company_logo_iv);
        unique_id_et = (EditText) findViewById(R.id.unique_id_et);


        one_ll = (LinearLayout) findViewById(R.id.one_ll);
        two_ll = (LinearLayout) findViewById(R.id.two_ll);
        three_ll = (LinearLayout) findViewById(R.id.three_ll);
        four_ll = (RelativeLayout) findViewById(R.id.four_ll);
        one_tv = (TextView) findViewById(R.id.one_tv);
        two_tv = (TextView) findViewById(R.id.two_tv);
        three_tv = (TextView) findViewById(R.id.three_tv);
        horizontalListView_goods = (HorizontalListView) findViewById(R.id.four_tv);
        horizontalListView_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempSelcetGoodsList.remove(position);
                horizontalListViewAdapter_goods.notifyDataSetChanged();
            }
        });
        one_ll.setOnClickListener(this);
        two_ll.setOnClickListener(this);
        three_ll.setOnClickListener(this);
        four_ll.setOnClickListener(this);


        company_logo_iv.setOnClickListener(NewSupplierActivity.this);
        fanhui_rl.setOnClickListener(NewSupplierActivity.this);
        job_ll.setOnClickListener(NewSupplierActivity.this);
        add_email_iv.setOnClickListener(NewSupplierActivity.this);
        country_ll.setOnClickListener(NewSupplierActivity.this);
        province_ll.setOnClickListener(NewSupplierActivity.this);
        city_ll.setOnClickListener(NewSupplierActivity.this);
        area_ll.setOnClickListener(NewSupplierActivity.this);
        save_bt.setOnClickListener(NewSupplierActivity.this);
        adapter = new MyDiyAdapterForNewCustomerforBuyer(datas, NewSupplierActivity.this);
        diy_add_lv.setAdapter(adapter);
    }

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

    // 获取地区raw里面的地址xml内容
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //选择职位
            case R.id.job_ll:
                showpopupWindow();
                break;
            //自定义添加一条email的item
            case R.id.add_email_iv:
                Map<String, Object> map1 = new ArrayMap<>();
                map1.put("man", "");
                datas.add(map1);
                adapter.notifyDataSetChanged();
                break;
            //国家
            case R.id.country_ll:
                popupwindow3(1);
                break;
            //省
            case R.id.province_ll:
                if (isProvince) {
                    popupwindow3(2);
                }
                break;
            //城市
            case R.id.city_ll:
                if (isCity) {
                    popupwindow3(3);
                }
                break;
            //区县
            case R.id.area_ll:
                if (isArea) {
                    popupwindow3(4);
                }
                break;
            //保存
            case R.id.save_bt:
                getData();
                if (myCustomer != null) {
                    if (myCustomer.getUnique_id().equals(unique_id)) {
                        Toast.makeText(getApplicationContext(), R.string.not_add, Toast.LENGTH_SHORT).show();
                    } else {
                        saved();
                    }
                } else {
                    saved();
                }
                break;
            //上传公司logo
            case R.id.company_logo_iv:
                from = Location.BOTTOM.ordinal();
                initPopupWindow();
                break;
            //一级行业分类
            case R.id.one_ll:
                popupwindow(1);
                break;
            //二级行业分类
            case R.id.two_ll:
                if (isTwo) {
                    popupwindow(2);
                }
                break;
            //三级行业分类
            case R.id.three_ll:
                if (isThree) {
                    popupwindow(3);
                }
                break;
            case R.id.four_ll:
                popupwindow2();
        }
    }

    private void initData() {
        AddressXML = getRawAddress().toString();
        try {
            analysisXML(AddressXML);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        // 初始化Button数据
        country_tv.setText(countryList.get(0).getCountry());
        province_tv.setText(countryList.get(0).getProvince_list().get(0).getProvince());
        city_tv.setText(countryList.get(0).getProvince_list().get(0).getCity_list().get(0).getCity());

        // 初始化下标
        countryPosition = 0;
        provincePosition = 0;
        cityPosition = 0;
    }

    // 获取地区raw里面的地址xml内容
    private StringBuffer getRawAddress() {
        InputStream in = null;
        if (language.getTag().equals("ch")) {
            in = getResources().openRawResource(R.raw.address_ch);
        } else if (language.getTag().equals("en")) {
            in = getResources().openRawResource(R.raw.address_en);
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

    // 解析国家 省份 城市 地区
    public void analysisXML(String data) throws XmlPullParserException {
        try {
            CountryModel countryModel = null;
            ProvinceModel provinceModel = null;
            CityModel cityModel = null;
            AreaModel areaModel = null;

            List<ProvinceModel> provinceList = null;
            List<CityModel> cityList = null;
            List<AreaModel> areaList = null;


            InputStream xmlData = new ByteArrayInputStream(data.getBytes("UTF-8"));
            XmlPullParserFactory factory = null;
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser;
            parser = factory.newPullParser();
            parser.setInput(xmlData, "utf-8");
            String currentTag = null;

            String country;
            String province;
            String city;
            String area;

            int type = parser.getEventType();

            while (type != XmlPullParser.END_DOCUMENT) {
                String typeName = parser.getName();

                if (type == XmlPullParser.START_TAG) {
                    if ("Location".equals(typeName)) {
                        countryList = new ArrayList<>();

                    } else if ("CountryRegion".equals(typeName)) {
                        country = parser.getAttributeValue(0);
                        countryModel = new CountryModel();
                        countryModel.setCountry(country);
                        provinceList = new ArrayList<>();
                    } else if ("State".equals(typeName)) {
                        province = parser.getAttributeValue(0);
                        provinceModel = new ProvinceModel();
                        provinceModel.setProvince(province);
                        cityList = new ArrayList<>();

                    } else if ("City".equals(typeName)) {
                        city = parser.getAttributeValue(0);
                        cityModel = new CityModel();
                        cityModel.setCity(city);
                        areaList = new ArrayList<>();

                    } else if ("Region".equals(typeName)) {
                        area = parser.getAttributeValue(0);
                        areaModel = new AreaModel();
                        areaModel.setArea(area);

                    }
                    currentTag = typeName;
                } else if (type == XmlPullParser.END_TAG) {
                    if ("Location".equals(typeName)) {

                    } else if ("CountryRegion".equals(typeName)) {
                        countryModel.setProvince_list(provinceList);
                        countryList.add(countryModel);
                    } else if ("State".equals(typeName)) {
                        provinceModel.setCity_list(cityList);
                        provinceList.add(provinceModel);
                    } else if ("City".equals(typeName)) {
                        cityModel.setArea_list(areaList);
                        cityList.add(cityModel);

                    } else if ("Region".equals(typeName)) {
                        areaList.add(areaModel);
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


    // type 1--国家 2--省份 3--市 4--区县
    private void popupwindow3(final int type) {
        ml = LayoutInflater.from(NewSupplierActivity.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);

        WindowManager wm = this.getWindowManager();
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        ListView lv = (ListView) view.findViewById(R.id.popup_lv);
        if (type == 1) {
            CountryAdapter countryAdapter = new CountryAdapter(countryList);
            lv.setAdapter(countryAdapter);
        } else if (type == 2) {
            ProvinceAdapter provinceAdapter = new ProvinceAdapter(countryList.get(countryPosition).getProvince_list());
            lv.setAdapter(provinceAdapter);

        } else if (type == 3) {
            CityAdapter cityAdapter = new CityAdapter(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list());
            lv.setAdapter(cityAdapter);

        } else if (type == 4) {
            AreaAdapter areaAdapter = new AreaAdapter(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(cityPosition).getArea_list());

            lv.setAdapter(areaAdapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1) {
                    countryPosition = position;
                    country_tv.setText(countryList.get(position).getCountry());
                    // 判断国家下面有没有省份
                    if (countryList.get(position).getProvince_list().size() < 1) {
                        province_tv.setText("");
                        city_tv.setText("");
                        area_tv.setText("");
                        isProvince = false;
                        isCity = false;
                        isArea = false;
                    } else {
                        isProvince = countryList.get(position).getProvince_list().size() != 1;

                        province_tv.setText(countryList.get(position).getProvince_list().get(0).getProvince());
                        provincePosition = 0;
                        // 判断该省份以下是否有市
                        if (countryList.get(position).getProvince_list().get(0).getCity_list().size() < 1) {
                            city_tv.setText("");
                            area_tv.setText("");
                            isCity = false;
                            isArea = false;
                        } else {
                            isCity = true;
                            city_tv.setText(countryList.get(position).getProvince_list().get(0).getCity_list().get(0).getCity());

                            cityPosition = 0;
                            // 判断市以下是否有县区
                            if (countryList.get(position).getProvince_list().get(0).getCity_list().get(0).getArea_list().size() < 1) {

                                area_tv.setText("");
                                isArea = false;

                            } else {
                                isArea = true;
                                area_tv.setText(countryList.get(position).getProvince_list().get(0).getCity_list().get(0).getArea_list().get(0).getArea());

                            }
                        }
                    }

                } else if (type == 2) {
                    provincePosition = position;
                    province_tv.setText(countryList.get(countryPosition).getProvince_list().get(position).getProvince());

                    if (countryList.get(countryPosition).getProvince_list().get(position).getCity_list().size() < 1) {
                        city_tv.setText("");
                        area_tv.setText("");
                        isCity = false;
                        isArea = false;
                    } else {
                        isCity = true;
                        city_tv.setText(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(0).getCity());

                        cityPosition = 0;
                        // 如果该市下没有区
                        if (countryList.get(countryPosition).getProvince_list().get(position).getCity_list().get(0).getArea_list().size() < 1) {

                            area_tv.setText("");
                            isArea = false;

                        } else {
                            isArea = true;
                            area_tv.setText(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(0).getArea_list().get(0).getArea());


                        }
                    }
                } else if (type == 3) {
                    cityPosition = position;
                    city_tv.setText(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(position).getCity());

                    if (countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(position).getArea_list().size() < 1) {

                        area_tv.setText("");
                        isArea = false;

                    } else {
                        isArea = true;
                        area_tv.setText(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(position).getArea_list().get(0).getArea());

                    }
                } else if (type == 4) {
                    area_tv.setText(countryList.get(countryPosition).getProvince_list().get(provincePosition).getCity_list().get(cityPosition).getArea_list().get(position).getArea());

                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(NewSupplierActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(country_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(NewSupplierActivity.this, 1f);
            }
        });


    }


    // 国家的适配器
    class CountryAdapter extends BaseAdapter {
        public List<CountryModel> adapter_list;

        public CountryAdapter(List<CountryModel> list) {
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
            TextView tv = new TextView(NewSupplierActivity.this);

            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getCountry());
            return tv;
        }

    }

    // 省份的适配器
    class ProvinceAdapter extends BaseAdapter {
        public List<ProvinceModel> adapter_list;

        public ProvinceAdapter(List<ProvinceModel> list) {
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
            TextView tv = new TextView(NewSupplierActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getProvince());
            return tv;
        }

    }

    // 市的适配器
    class CityAdapter extends BaseAdapter {
        public List<CityModel> adapter_list;

        public CityAdapter(List<CityModel> list) {
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
            TextView tv = new TextView(NewSupplierActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getCity());
            return tv;
        }

    }

    // 县区的适配器
    class AreaAdapter extends BaseAdapter {
        public List<AreaModel> adapter_list;

        public AreaAdapter(List<AreaModel> list) {
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
            TextView tv = new TextView(NewSupplierActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getArea());
            return tv;
        }

    }


    //职位选择的popupWindow
    private void showpopupWindow() {
        ml = LayoutInflater.from(NewSupplierActivity.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        view.setBackgroundColor(Color.WHITE);

        WindowManager wm = this.getWindowManager();
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        ListView listview = (ListView) view.findViewById(R.id.popup_lv);
        ArrayAdapter<String> adapter = null;
        if (language.getTag().equals("ch")) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_job);
        } else if (language.getTag().equals("en")) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_job_en);
        }
        listview.setAdapter(adapter);
        mPopupWindow.setFocusable(true);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                job = adapterView.getItemAtPosition(i).toString();
                job_tv.setText(job);
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(NewSupplierActivity.this, 0.5f);//0.0-1.0
        mPopupWindow.showAtLocation(job_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(NewSupplierActivity.this, 1f);
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

    //得到用户输入的数据
    private void getData() {
        String xin = input_custom_xin_et.getText().toString().trim();//姓
        String ming = input_custom_ming_et.getText().toString().trim();//名
        name = xin + ming;//姓名
        companyname = input_company_name_et.getText().toString().trim();//公司名
        jobforcustome = null;//得到客户当前选择的职位
        if (language.getTag().equals("ch")) {
            if (job == null) {
                jobforcustome = job_other_et.getText().toString().trim();//自定义其他职位
            } else if (job.equals("其他") || job.equals("请选择")) {
                if (job_other_et.getText().toString().trim().equals("")) {
                    jobforcustome = "其他";
                } else {
                    jobforcustome = job_other_et.getText().toString().trim();//自定义其他职位
                }
            } else if (!job.equals("其他") || !job.equals("请选择")) {
                jobforcustome = job;
            }
        } else if (language.getTag().equals("en")) {
            if (job == null) {
                jobforcustome = job_other_et.getText().toString().trim();//自定义其他职位
            } else if (job.equals("other") || job.equals("Select")) {
                if (job_other_et.getText().toString().trim().equals("")) {
                    jobforcustome = "other";
                } else {
                    jobforcustome = job_other_et.getText().toString().trim();//自定义其他职位
                }
            } else if (!job.equals("other") || !job.equals("Select")) {
                jobforcustome = job;
            }
        }

        phonenumber = input_phonenumber_et.getText().toString().trim();//电话
        postcode = input_postcode_et.getText().toString().trim();

        StringBuilder stringBuilder = new StringBuilder();
        //得到自定义的listview的item上用户输入的数据
        String substring = null;
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> stringObjectMap = datas.get(i);

            if (stringObjectMap.get("man") == null) continue;
            String man = (String) stringObjectMap.get("man");
            stringBuilder.append(man).append("|");
            substring = stringBuilder.toString().substring(0, (stringBuilder.toString().lastIndexOf("|")));
        }
        String email_address = input_email_et.getText().toString().trim();
        email = email_address + "|" + substring;//邮箱地址
        String country = country_tv.getText().toString().trim();//国家
        String province = province_tv.getText().toString().trim();//省
        String city = city_tv.getText().toString().trim();//市
        String area = area_tv.getText().toString().trim();//区县
        String detailaddress = detail_address_et.getText().toString().trim();//详细地址
        if (!province.equals("") && !city.equals("") && !area.equals("")) {
            address = country + "|" + province + "|" + city + "|" + area + "|" + detailaddress;//地址
        } else if (!province.equals("") && !city.equals("") && area.equals("")) {
            address = country + "|" + province + "|" + city + "|" + detailaddress;//地址
        } else if (province.equals("") && !city.equals("") && area.equals("")) {
            address = country + "|" + city + "|" + detailaddress;//地址
        } else if (province.equals("") && city.equals("") && area.equals("")) {
            address = country + "|" + detailaddress;//地址
        }
        //行业
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
        //备注其他
        other = input_other_et.getText().toString().trim();//备注其他
        if (!unique_id_et.getText().toString().trim().equals("")) {
            unique_id = unique_id_et.getText().toString().trim();
        } else {
            String stringRandom = ImageFactoryandOther.getStringRandom(6);
            unique_id = stringRandom;
        }

        Bitmap bitmap_bg = BitmapFactory.decodeResource(getResources(), R.drawable.a3);
        bytes_bg = BitmapUtils.compressBmpFromBmp(bitmap_bg);
    }

    //保存用户输入的所有数据 并存入数据库
    private void saved() {
        if (!name.equals("") && !companyname.equals("") && !email.equals("")) {
            byte[] bytes;
            if (bitmap != null) {
                bytes = BitmapUtils.compressBmpFromBmp(bitmap);
            } else {
                Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                bytes = BitmapUtils.compressBmpFromBmp(bitmap_no);
            }
            String date = DateUtil.getDate();
            //创建一个实体类
            MyCustomer myCustomer = new MyCustomer();
            //将用户输入的所有数据加载myCustomer对象中
            myCustomer.setName(name);
            myCustomer.setCompany_name(companyname);
            myCustomer.setPhone(phonenumber);
            myCustomer.setEmail(email);
            myCustomer.setCompany_address(address);
            myCustomer.setPostcode(postcode);
            myCustomer.setIndustry_type(industry_type);
            myCustomer.setJob_position(jobforcustome);
            myCustomer.setCompany_logo(bytes);
            myCustomer.setCustomer_ohter(other);
            myCustomer.setBackground_pic(bytes_bg);
            myCustomer.setCreate_time(date);
            myCustomer.setCustomer_type(1);
            myCustomer.setUnique_id(unique_id);
            //创建一个方法层对象
            MyCustomerDao myCustomerDao = new MyCustomerDao(NewSupplierActivity.this);
            //执行该方法层中添加的方法
            myCustomerDao.insertdata(myCustomer);
            finish();
        } else {
            ToastUtil.showToast2(NewSupplierActivity.this, R.string.check_info);
        }
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
        backgroundAlpha(NewSupplierActivity.this, 0.5f);
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
                //调用系统相册
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_2);
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

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(NewSupplierActivity.this, 1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_2 && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                showImage(imagePath);
                c.close();
            }

        }
    }

    // 加载图片
    private void showImage(String imaePath) {
        //图片压缩的方法
        bitmap = ImageFactoryandOther.ratio(imaePath, 200, 260);
        company_logo_iv.setImageBitmap(bitmap);
    }


    private void popupwindow(final int type) {
        ml = LayoutInflater.from(NewSupplierActivity.this);
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
        backgroundAlpha(NewSupplierActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(NewSupplierActivity.this, 1f);
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
            TextView tv = new TextView(NewSupplierActivity.this);
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
            TextView tv = new TextView(NewSupplierActivity.this);
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
            TextView tv = new TextView(NewSupplierActivity.this);
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
            TextView tv = new TextView(NewSupplierActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getFour());
            return tv;
        }

    }

    private void popupwindow2() {
        ml = LayoutInflater.from(NewSupplierActivity.this);
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
                                          horizontalListViewAdapter_goods = new HorizontalListViewAdapter(NewSupplierActivity.this, tempSelcetGoodsList);
                                          horizontalListView_goods.setAdapter(horizontalListViewAdapter_goods);
//                                          for (int i = 0; i < tempSelcetGoodsList.size(); i++) {
//                                              Log.d("tempSelcetGoodsList", "onItemClick: " + tempSelcetGoodsList.get(i));
//                                          }
                                          mPopupWindow.dismiss();
                                      }
                                  }
        );
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(NewSupplierActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_ll, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                              @Override
                                              public void onDismiss() {
                                                  backgroundAlpha(NewSupplierActivity.this, 1f);
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


}
