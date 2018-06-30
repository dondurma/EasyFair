package com.baibeiyun.eazyfair.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.HorizontalListViewAdapter;
import com.baibeiyun.eazyfair.app.MyAppclication;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.model.FourModel;
import com.baibeiyun.eazyfair.model.OneModel;
import com.baibeiyun.eazyfair.model.ThreeModel;
import com.baibeiyun.eazyfair.model.TwoModel;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.HorizontalListView;

import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    //输入电话或者邮箱 输入验证码  输入密码
    private EditText inputphonenumber_et, input_verification_et, inputpassword_et;
    //获取验证码
    private TextView achieve_verification_tv;
    //服务协议
    private TextView agreement_tv;
    //注册
    private Button register_bt;

    private TextView hangye_one_tv;
    private TextView hangye_two_tv;
    private TextView hangye_three_tv;
    private RelativeLayout four_rl;

    private String IndustryXML;// xml格式的全球城市信息
    private List<OneModel> oneList;
    private int onePosition;
    private int twoPosition;
    private int threePosition;
    private boolean isTwo = true;
    private boolean isThree = true;
    private boolean isFour = true;

    private LinearLayout hangye_one_ll;
    private LinearLayout hangye_two_ll;
    private LinearLayout hangye_three_ll;


    private HorizontalListView horizontalListView_goods;
    private HorizontalListViewAdapter horizontalListViewAdapter_goods;
    private List<String> tempSelcetGoodsList = new ArrayList<>();//横向ListView中Item的信息
    private List<FourModel> four_list;
    private LayoutInflater ml;
    private PopupWindow mPopupWindow;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private ImageView yz_iv;

    private CheckBox register_cb;
    private boolean tag = true;
    private RelativeLayout fanhui_rl;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initYuyan();
        initView();
        initDataforIndustry();
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


    private void initView() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        yz_iv = (ImageView) findViewById(R.id.yz_iv);
        inputphonenumber_et = (EditText) findViewById(R.id.inputphonenumber_et);

        inputphonenumber_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean b = checkEmaile(s.toString());
                if (b == true) {
                    yz_iv.setVisibility(View.VISIBLE);
                    yz_iv.setImageResource(R.mipmap.yz_yes);
                    flag = true;
                } else {
                    yz_iv.setVisibility(View.VISIBLE);
                    yz_iv.setImageResource(R.mipmap.yz_no);
                    flag = false;
                }
            }
        });
        input_verification_et = (EditText) findViewById(R.id.input_verification_et);
        inputpassword_et = (EditText) findViewById(R.id.inputpassword_et);
        inputpassword_et.setTypeface(inputphonenumber_et.getTypeface());
        achieve_verification_tv = (TextView) findViewById(R.id.achieve_verification_tv);
        agreement_tv = (TextView) findViewById(R.id.agreement_tv);
        register_bt = (Button) findViewById(R.id.register_bt);

        hangye_one_tv = (TextView) findViewById(R.id.hangye_one_tv);
        hangye_two_tv = (TextView) findViewById(R.id.hangye_two_tv);
        hangye_three_tv = (TextView) findViewById(R.id.hangye_three_tv);
        horizontalListView_goods = (HorizontalListView) findViewById(R.id.hangye_four_tv);
        horizontalListView_goods.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tempSelcetGoodsList.remove(position);
                        horizontalListViewAdapter_goods.notifyDataSetChanged();
                    }
                });
        hangye_one_ll = (LinearLayout) findViewById(R.id.hangye_one_ll);
        hangye_two_ll = (LinearLayout) findViewById(R.id.hangye_two_ll);
        hangye_three_ll = (LinearLayout) findViewById(R.id.hangye_three_ll);
        four_rl = (RelativeLayout) findViewById(R.id.four_rl);
        register_cb = (CheckBox) findViewById(R.id.register_cb);
        hangye_one_ll.setOnClickListener(this);
        hangye_two_ll.setOnClickListener(this);
        hangye_three_ll.setOnClickListener(this);
        four_rl.setOnClickListener(this);
        fanhui_rl.setOnClickListener(this);

        achieve_verification_tv.setOnClickListener(RegisterActivity.this);
        agreement_tv.setOnClickListener(RegisterActivity.this);
        register_bt.setOnClickListener(RegisterActivity.this);
        //同意使用协议的监听事件
        tag = register_cb.isChecked();

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //获取验证码
            case R.id.achieve_verification_tv:
                if (flag == true) {
                    if (!"".equals(inputphonenumber_et.getText().toString().trim())) {
                        initDateEmail();

                    } else {
                        ToastUtil.showToast2(this, R.string.input_sjhhyx);//请输入邮箱
                    }
                } else {
                    ToastUtil.showToast2(this, R.string.email_false);
                }
                break;
            //一级行业类型
            case R.id.hangye_one_ll:
                popupwindow(1);
                break;
            //二级行业类型
            case R.id.hangye_two_ll:
                if (isTwo) {
                    popupwindow(2);
                }
                break;
            //三级行业类型
            case R.id.hangye_three_ll:
                if (isThree) {
                    popupwindow(3);
                }
                break;
            //四级行业类型
            case R.id.four_rl:
                popupwindow2();
                break;
            //协议
            case R.id.agreement_tv:
                intent = new Intent(RegisterActivity.this, AgreementActivity.class);
                startActivity(intent);
                break;
            //注册
            case R.id.register_bt:
                //同意使用协议的监听事件
                tag = register_cb.isChecked();
                if (!inputphonenumber_et.getText().toString().trim().equals("")) {
                    if (tag) {
                        if (tempSelcetGoodsList.size() != 0) {
                            initDateRegister("-ch");

                        } else {
                            ToastUtil.showToast2(this, R.string.please_fill_zqdhylx);//请填写正确的行业类型
                        }
                    } else {
                        ToastUtil.showToast2(this, R.string.please_jsxy);//请先接受《EasyFair服务协议》
                    }
                } else {
                    ToastUtil.showToast2(this, R.string.input_sjhhyx);//请输入邮箱
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

    //获取验证码的接口调用
    private void initDateEmail() {
        JSONObject jsonObject = new JSONObject();
        String url = BaseUrl.HTTP_URL + "user/verification";
        String email = inputphonenumber_et.getText().toString();
        String tag = "Email";
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(RegisterActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String message = result.getString("message");
                    ToastUtil.showToast(RegisterActivity.this, message);
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

    //-ch  中文
    // -en 英文
    private void initDateRegister(final String registerType) {
        String industryType = "";
        JSONObject jsonObject = new JSONObject();
        String url = BaseUrl.HTTP_URL + "user/register";
        String email = inputphonenumber_et.getText().toString();
        String code = input_verification_et.getText().toString();
        String password = inputpassword_et.getText().toString();
        //行业
        String one = hangye_one_tv.getText().toString().trim();
        String two = hangye_two_tv.getText().toString().trim();
        String three = hangye_three_tv.getText().toString().trim();
        industryType = ListToString(tempSelcetGoodsList);
        String industry = null;
        if (two != null && industryType != null) {
            industry = one + "|" + two + "|" + three + "|" + industryType;
        } else {
            industry = one + "|" + two + "|" + three;
        }
        String tag = "Register";
        try {
            jsonObject.put("email", email);
            jsonObject.put("code", code);
            jsonObject.put("password", password);
            jsonObject.put("industryType", industry);
            jsonObject.put("registerType", registerType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(RegisterActivity.this, url, tag, jsonObject, new VolleyCallBack(getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if ("200".equals(result.getString("code"))) {
                        SharedPreferences.Editor edit = SharedprefenceStore.getSp().edit();
                        edit.putString(SharedprefenceStore.REGISTERTYPE, registerType);
                        edit.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        ToastUtil.showToast2(RegisterActivity.this, R.string.zccg);//注册成功
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


    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyAppclication.getHttpQueues().cancelAll("Email");
        MyAppclication.getHttpQueues().cancelAll("Register");
    }


    private void initDataforIndustry() {
        IndustryXML = getRawIndustry().toString();
        try {
            analysisXMLforIndustry(IndustryXML);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        // 初始化数据
//        hangye_one_tv.setText(oneList.get(0).getOne());
//        hangye_two_tv.setText(oneList.get(0).getTwo_list().get(0).getTwo());
//        hangye_three_tv.setText(oneList.get(0).getTwo_list().get(0).getThree_list().get(0).getThree());

        // 初始化下标
//        onePosition = 0;
//        twoPosition = 0;
//        threePosition = 0;

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

    private void popupwindow(final int type) {
        ml = LayoutInflater.from(RegisterActivity.this);
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
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (type == 1) {
                            onePosition = position;
                            hangye_one_tv.setText(oneList.get(position).getOne());
                            if (oneList.get(position).getTwo_list().size() < 1) {
                                hangye_two_tv.setText("");
                                hangye_three_tv.setText("");
                                isTwo = false;
                                isThree = false;
                                isFour = false;
                            } else {
                                isTwo = oneList.get(position).getTwo_list().size() != 1;

                                hangye_two_tv.setText(oneList.get(position).getTwo_list().get(0).getTwo());
                                twoPosition = 0;
                                if (oneList.get(position).getTwo_list().get(0).getThree_list().size() < 1) {
                                    hangye_three_tv.setText("");
                                    isThree = false;
                                    isFour = false;
                                } else {
                                    isThree = true;
                                    hangye_three_tv.setText(oneList.get(position).getTwo_list().get(0).getThree_list().get(0).getThree());
                                    threePosition = 0;
                                    if (oneList.get(position).getTwo_list().get(0).getThree_list().get(0).getFour_list().size() < 1) {
                                        isFour = false;
                                    }
                                }
                            }

                        } else if (type == 2) {
                            twoPosition = position;
                            hangye_two_tv.setText(oneList.get(onePosition).getTwo_list().get(position).getTwo());

                            if (oneList.get(onePosition).getTwo_list().get(position).getThree_list().size() < 1) {
                                hangye_three_tv.setText("");
                                isThree = false;
                                isFour = false;
                            } else {
                                isThree = true;
                                hangye_three_tv.setText(oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(0).getThree());
                                threePosition = 0;
                                if (oneList.get(onePosition).getTwo_list().get(position).getThree_list().get(0).getFour_list().size() < 1) {
                                    isFour = false;
                                }
                            }
                        } else if (type == 3) {
                            threePosition = position;
                            hangye_three_tv.setText(oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(position).getThree());
                            isFour = oneList.get(onePosition).getTwo_list().get(twoPosition).getThree_list().get(position).getFour_list().size() >= 1;
                        }
                        mPopupWindow.dismiss();
                    }
                });
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(RegisterActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(hangye_two_tv, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(RegisterActivity.this, 1f);
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
            TextView tv = new TextView(RegisterActivity.this);
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
            TextView tv = new TextView(RegisterActivity.this);
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
            TextView tv = new TextView(RegisterActivity.this);
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
            TextView tv = new TextView(RegisterActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getFour());
            return tv;
        }

    }

    private void popupwindow2() {
        ml = LayoutInflater.from(RegisterActivity.this);
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
                        horizontalListViewAdapter_goods = new HorizontalListViewAdapter(RegisterActivity.this, tempSelcetGoodsList);
                        horizontalListView_goods.setAdapter(horizontalListViewAdapter_goods);
                        for (int i = 0; i < tempSelcetGoodsList.size(); i++) {
                        }
                        mPopupWindow.dismiss();
                    }
                }
        );
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(RegisterActivity.this, 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(hangye_one_tv, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(RegisterActivity.this, 1f);
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


    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    //验证邮箱的正则表达式
    private static boolean checkEmaile(String emaile) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }


}
