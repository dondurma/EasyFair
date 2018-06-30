package com.baibeiyun.eazyfair.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.adapter.HorizontalListViewAdapter;
import com.baibeiyun.eazyfair.dao.MyCommodityDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.model.FourModel;
import com.baibeiyun.eazyfair.model.OneModel;
import com.baibeiyun.eazyfair.model.ThreeModel;
import com.baibeiyun.eazyfair.model.TwoModel;
import com.baibeiyun.eazyfair.utils.BaseUrl;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.HttpUtils;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.SharedprefenceStore;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.utils.VolleyCallBack;
import com.baibeiyun.eazyfair.view.HorizontalListView;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class WoDeKeHuFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText priceMinEd;//价格区间最低价
    private EditText priceMaxEd;//价格区间最高价
    private EditText introduceEd;//产品简介
    //四中不同的行业类型
    private LinearLayout selectGoodsTv;//商品选择
    //四中不同的行业类型
    private ImageView selectGoodsIv;//商品选择
    private TextView addTv;//添加
    private Button confirmSendBtn;//确认发布
    private HorizontalListView horizontalListView_goods;//横向的ListView 商品选择
    private HorizontalListViewAdapter horizontalListViewAdapter_goods;
    private List<String> caiGouShangList = new ArrayList<>();//采购商的信息
    private List<String> horizonList = new ArrayList<>();//横向ListView中Item的信息
    private List<String> tempSelcetGoodsList = new ArrayList<>();//横向ListView中Item的信息 商品选择

    private TextView one_tv;
    private TextView two_tv;
    private TextView three_tv;

    private ImageView one_iv;
    private ImageView two_iv;
    private ImageView three_iv;
    private ImageView four_iv;


    private String IndustryXML;
    private List<OneModel> oneList;
    private int onePosition;
    private int twoPosition;
    private int threePosition;
    private boolean isTwo = true;
    private boolean isThree = true;
    private boolean isFour = true;
    private LayoutInflater ml;
    private PopupWindow mPopupWindow;


    private List<String> tempSelcetIndustry = new ArrayList<>();//横向ListView中Item的信息
    private List<FourModel> four_list;
    private HorizontalListView horizontalListView_industry;


    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private ArrayList<MyCommodity> myCommodities = new ArrayList<>();//存放供应商我的商品的集合
    private MyCommodity myCommodity;
    private MyUser myUser;
    //存放email的集合
    private List<String> emailList = new ArrayList<>();

    private String tag = "WoDeKeHuFragment";
    private String firstIndustryType;
    private String secondIndustryType;
    private String thirdIndustryType;
    private String fourthIndustryType;

    /**
     * Dialog
     */
    private PopupWindow popupWindow = null;
    private int tempStaus = 0;
    private ArrayList<Integer> caiGouShangIDList = new ArrayList<>();//已选择商品的ID的集合

    /**
     * 查询数据库 拿到要在PopuWindow展示的数据,选择采购商
     */
    private List<Integer> IDList = new ArrayList<>();
    private String tag2 = "RequestEmail";
    private MyDialog myDialog;
    private Handler mHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wodekehu_layout, null);
        initYuyan();
        initView(view);
        initEvent();
        initDataforIndustry();
        return view;
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(getContext());
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

    private void initEvent() {
        one_iv.setOnClickListener(this);
        two_iv.setOnClickListener(this);
        three_iv.setOnClickListener(this);
        four_iv.setOnClickListener(this);
        selectGoodsTv.setOnClickListener(this);
        selectGoodsIv.setOnClickListener(this);
        confirmSendBtn.setOnClickListener(this);
    }

    private void initView(View view) {
        myDialog = new MyDialog(getActivity());
        mHandler = new Handler();
        myUser = CursorUtils.selectOurInfo(getContext());
        priceMinEd = (EditText) view.findViewById(R.id.wodekehu_price_ed_min);
        priceMaxEd = (EditText) view.findViewById(R.id.wodekehu_price_ed_max);
        introduceEd = (EditText) view.findViewById(R.id.wodekehu_introduce_ed);
        one_tv = (TextView) view.findViewById(R.id.one_tv);
        two_tv = (TextView) view.findViewById(R.id.two_tv);
        three_tv = (TextView) view.findViewById(R.id.three_tv);
        horizontalListView_industry = (HorizontalListView) view.findViewById(R.id.four_tv);
        horizontalListView_industry.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tempSelcetIndustry.remove(i);
                        horizontalListViewAdapter_goods.notifyDataSetChanged();
                    }
                });
        one_iv = (ImageView) view.findViewById(R.id.one_iv);
        two_iv = (ImageView) view.findViewById(R.id.two_iv);
        three_iv = (ImageView) view.findViewById(R.id.three_iv);
        four_iv = (ImageView) view.findViewById(R.id.four_iv);

        selectGoodsTv = (LinearLayout) view.findViewById(R.id.wodekehu_select_goods_tv);
        selectGoodsIv = (ImageView) view.findViewById(R.id.wodekehu_select_goods_iv);

        confirmSendBtn = (Button) view.findViewById(R.id.wodekehu_confirm_send);
        horizontalListView_goods = (HorizontalListView) view.findViewById(R.id.wodekehu_select_goods_list);
        horizontalListView_goods.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tempSelcetGoodsList.remove(position);
                        caiGouShangIDList.remove(position);
                        horizontalListViewAdapter_goods.notifyDataSetChanged();
                    }
                });
    }


    private void popuWindow(View v, final int status) {
        caiGouShangList.clear();
        searchDate(status);//查询数据库
        View mV = LayoutInflater.from(getContext()).inflate(R.layout.listview_popupwindow, null);
        mV.setBackgroundColor(Color.WHITE);
        popupWindow = new PopupWindow(mV, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(mV);
        final ListView listview = (ListView) mV.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, caiGouShangList);
        listview.setAdapter(adapter);
        popupWindow.setFocusable(true);
        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        String s = caiGouShangList.get(position);
                        int ID = IDList.get(position);
                        //在有横向ListView时，如果当前的状态和上一次的状态不一样的话，就清除掉横向ListView中Item的信息的集合
                        if (tempStaus != status) {
                            horizonList.clear();
                        }
                        switch (status) {
                            case 2://商品选择
                                tempStaus = 2;
                                if (!tempSelcetGoodsList.contains(s)) {
                                    tempSelcetGoodsList.add(s);
                                    caiGouShangIDList.add(ID);
                                }
                                horizontalListViewAdapter_goods = new HorizontalListViewAdapter(getContext(), tempSelcetGoodsList);
                                horizontalListView_goods.setAdapter(horizontalListViewAdapter_goods);
                                break;
                        }
                    }
                });
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(getActivity(), 0.5f);//0.0-1.0
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(getActivity(), 1f);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodekehu_select_goods_tv:
                break;
            //选择商品
            case R.id.wodekehu_select_goods_iv:
                popuWindow(selectGoodsIv, 2);
                break;
            //发布
            case R.id.wodekehu_confirm_send:
                if (!"".equals(one_tv.getText().toString())) {

                    if (caiGouShangIDList.size()!=0) {
                        myDialog.dialogShow();
                        mHandler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //根据多个id来查询商品
                                        myCommodities = CursorUtils.selectCommodityIds(getContext(), caiGouShangIDList);
                                        initEmailData(
                                                new GetEmail() {
                                                    @Override
                                                    public void onsuccess() {
                                                        try {
                                                            writeContentToPDF();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                }, 2000);
                    }else{
                        ToastUtil.showToast2(getActivity(),R.string.please_xzsp);
                    }

                }else{
                    ToastUtil.showToast2(getActivity(),R.string.please_xzhylx);
                }


                break;
            case R.id.one_iv:
                popupwindow(1);
                break;
            case R.id.two_iv:
                if (isTwo) {
                    popupwindow(2);
                }
                break;
            case R.id.three_iv:
                if (isThree) {
                    popupwindow(3);
                }
                break;
            case R.id.four_iv:
                popupwindow2();
                break;
        }
    }


    private void searchDate(int flag) {
        IDList.clear();
        switch (flag) {
            case 2://商品选择
                MyCommodityDao myCommodityDao = new MyCommodityDao(getContext());
                Cursor cursor2 = myCommodityDao.SelectforTypeForSupplier();
                while (cursor2.moveToNext()) {
                    String goods_type = cursor2.getString(cursor2.getColumnIndex("name"));
                    int id = cursor2.getInt(cursor2.getColumnIndex("_id"));
                    caiGouShangList.add(goods_type);
                    IDList.add(id);
                }
                break;
        }
    }

    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
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
        ml = LayoutInflater.from(getContext());
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
        backgroundAlpha(getActivity(), 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_tv, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(
                new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }

    private void popupwindow2() {
        ml = LayoutInflater.from(getContext());
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
                        if (!tempSelcetIndustry.contains(fourModel.getFour())) {
                            tempSelcetIndustry.add(fourModel.getFour());
                        }
                        horizontalListViewAdapter_goods = new HorizontalListViewAdapter(getContext(), tempSelcetIndustry);
                        horizontalListView_industry.setAdapter(horizontalListViewAdapter_goods);
                        for (int i = 0; i < tempSelcetIndustry.size(); i++) {
                        }
                        mPopupWindow.dismiss();
                    }
                }
        );
        mPopupWindow.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(getActivity(), 0.5f);// 0.0-1.0
        mPopupWindow.showAtLocation(one_tv, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(getActivity(), 1f);
                    }
                }
        );
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
            TextView tv = new TextView(getContext());
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
            TextView tv = new TextView(getContext());
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
            TextView tv = new TextView(getContext());
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
            TextView tv = new TextView(getContext());
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize(18);
            tv.setText(adapter_list.get(position).getFour());
            return tv;
        }

    }


    private void initDatePublish() {
//        myDialog.dialogShow();
        String url = BaseUrl.HTTP_URL + "businessOpportunity/addBusinessOpportunity";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        String userType = edit.getString(SharedprefenceStore.USERTYPE, "");
        String priceRange = priceMinEd.getText().toString() + "-" + priceMaxEd.getText().toString();
        String productIntroduction = introduceEd.getText().toString();
        firstIndustryType = one_tv.getText().toString();
        secondIndustryType = two_tv.getText().toString();
        thirdIndustryType = three_tv.getText().toString();
        fourthIndustryType = jointIndustry(tempSelcetIndustry);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", userType);
            jsonObject.put("type", "1");
            jsonObject.put("firstIndustryType", firstIndustryType);
            jsonObject.put("secondIndustryType", secondIndustryType);
            jsonObject.put("thirdIndustryType", thirdIndustryType);
            jsonObject.put("fourthIndustryType", fourthIndustryType);
            jsonObject.put("productName", jointIndustry(tempSelcetGoodsList));
            jsonObject.put("priceRange", priceRange);
            jsonObject.put("productIntroduction", productIntroduction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(getContext(), url, tag, jsonObject, new VolleyCallBack(getActivity().getApplicationContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    ToastUtil.showToast(getContext(), result.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    private String jointIndustry(List<String> mList) {
        StringBuffer stringBuffer = null;
        if (mList != null && mList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mList.size(); i++) {
                String s = mList.get(i) + "|";
                sb.append(s);
            }
            int i = sb.lastIndexOf("|");
            stringBuffer = sb.deleteCharAt(i);
            return stringBuffer.toString();
        }
        return "";
    }


    //将从数据库得到的数据转成pdf格式
    private void writeContentToPDF() throws Exception {
        Document document = new Document();
        File file = new File("sdcard/EasyFair/GoodsDetailsforSupplier.pdf");
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 10, Font.NORMAL);
        for (int i = 0; i < myCommodities.size(); i++) {
            myCommodity = myCommodities.get(i);
            createPdf(document, font);
        }
        document.close();
    }

    private void createPdf(Document document, Font font) {
        try {
            /***** 第一行 ******/
            PdfPTable pdfTable = new PdfPTable(1);
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font1 = new Font(bfChinese, 20, Font.NORMAL);
            Paragraph paragraph = new Paragraph("商品详情单", font1);
            PdfPCell cell = new PdfPCell(paragraph);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setPaddingBottom(5f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidth(0);// 设置表格外线的宽度
            cell.setBorderColor(BaseColor.WHITE);// 设置表格外线的颜色
            cell.setMinimumHeight(60);// 设置表格的最小高度
            pdfTable.addCell(cell);
            pdfTable.completeRow();
            document.add(pdfTable);
            /***** 第二行 ******/
            List<String> mList6 = new ArrayList<>();
            mList6.add("公司名称");
            mList6.add(myUser.getCh_company_name());
            mList6.add("姓名");
            mList6.add(myUser.getCh_contact());
            createTable(mList6, document, font);

            /***** 第四行 ******/
            PdfPTable table3 = new PdfPTable(5);
            table3.setWidths(new float[]{25f, 31.33f, 31.33f, 31.33f, 31.33f});
            // 第一列
            Paragraph paragraph7 = new Paragraph("产品图片", font);
            PdfPCell cell6 = new PdfPCell(paragraph7);
            cell6.setUseBorderPadding(true);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
            cell6.setMinimumHeight(100);// 设置表格的最小高度
            table3.addCell(cell6);

            // 第二列：添加图片
            List<Image> list = new ArrayList<>();
            //图片的路径
            byte[] product_imgs1 = myCommodity.getProduct_imgs1();
            byte[] product_imgs2 = myCommodity.getProduct_imgs2();
            byte[] product_imgs3 = myCommodity.getProduct_imgs3();
            byte[] product_imgs4 = myCommodity.getProduct_imgs4();

            list.add(Image.getInstance(product_imgs1));
            list.add(Image.getInstance(product_imgs2));
            list.add(Image.getInstance(product_imgs3));
            list.add(Image.getInstance(product_imgs4));

            createImageTable(list, document, table3);
            table3.completeRow();
            document.add(table3);
            /***** 第五行 ******/
            List<String> mList1 = new ArrayList<>();
            mList1.add("产品参数详情");
            createTable(mList1, document, font);
            /***** 第六行 ******/
            List<String> mList = new ArrayList<>();
            mList.add("商品名称");
            mList.add(myCommodity.getName());
            mList.add("商品价格");
            mList.add(String.valueOf(myCommodity.getPrice()));
            createTable(mList, document, font);
            /***** 第七行 ******/
            List<String> mList2 = new ArrayList<>();
            mList2.add("公司货号");
            mList2.add(myCommodity.getSerial_number());
            mList2.add("商品材质");
            mList2.add(myCommodity.getMaterial());
            createTable(mList2, document, font);
            /***** 第八行 ******/
            List<String> mList3 = new ArrayList<>();
            mList3.add("商品单位");
            mList3.add(myCommodity.getUnit());
            mList3.add("商品颜色");
            mList3.add(myCommodity.getColor());
            createTable(mList3, document, font);
            /***** 第九行 ******/
            List<String> mList4 = new ArrayList<>();
            mList4.add("价格条款");
            mList4.add(myCommodity.getPrice_clause());
            mList4.add("货币类型");
            mList4.add(myCommodity.getCurrency_variety());
            createTable(mList4, document, font);
            /***** 第十行 ******/
            List<String> mList5 = new ArrayList<>();
            mList5.add("商品介绍");
            mList5.add(myCommodity.getIntroduction());
            mList5.add("商品备注");
            mList5.add(myCommodity.getRemark());
            createTable(mList5, document, font);
            /***** 第十一行 ******/
            List<String> mList8 = new ArrayList<>();
            mList8.add(OtherUtils.OUTPAC);
            mList8.add(OtherUtils.NUMBER);
            mList8.add(myCommodity.getOutbox_number());
            mList8.add(OtherUtils.SIZE);
            mList8.add(myCommodity.getOutbox_size());
            mList8.add(OtherUtils.WEIGHT);
            mList8.add(myCommodity.getOutbox_weight() + "\t" + myCommodity.getOutbox_weight_unit());
            createTable(mList8, document, font);
            /***** 第十二行 ******/
            List<String> mList9 = new ArrayList<>();
            mList9.add(OtherUtils.INNER_PAC);
            mList9.add(OtherUtils.NUMBER);
            mList9.add(myCommodity.getCenterbox_number());
            mList9.add(OtherUtils.SIZE);
            mList9.add(myCommodity.getCenterbox_size());
            mList9.add(OtherUtils.WEIGHT);
            mList9.add(myCommodity.getCenterbox_weight() + "\t" + myCommodity.getCenterbox_weight_unit());
            createTable(mList9, document, font);

            /***** 第十三行开始 ******/
            String self_defined = myCommodity.getDiy();
            if (self_defined != null && !self_defined.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
                String[] split = self_defined.split("\\|");//分行 每行两条自定义的内容
                for (int i = 0; i < split.length; i++) {
                    if (i % 2 == 0) {
                        buffer.append(split[i]).append("*");
                    } else {
                        buffer.append(split[i]).append("|");
                    }
                }
                String[] split7 = buffer.toString().split("\\|");
                for (String s : split7) {
                    //分条 将每行的两条内容分开
                    String[] split4 = s.split("\\*");
                    if (split4.length == 2) {//这一行刚好有两条自定已的内容
                        //得到两条自定已的内容
                        String s1 = split4[0];
                        String s2 = split4[1];
                        //将第一条自定义内容分割
                        String[] split5 = s1.split(":");
                        //将第二条自定义内容分割
                        String[] split6 = s2.split(":");
                        /***** 自定义 ******/
                        List<String> mCustomList1 = new ArrayList<>();
                        mCustomList1.add(split5[0]);
                        mCustomList1.add(split5[1]);
                        mCustomList1.add(split6[0]);
                        mCustomList1.add(split6[1]);
                        createTable(mCustomList1, document, font);
                    } else if (split4.length == 1) {//这一行自定已的内容只有一条
                        String s1 = split4[0];
                        String[] split5 = s1.split(":");
                        List<String> mCustomList2 = new ArrayList<>();
                        mCustomList2.add(split5[0]);
                        mCustomList2.add(split5[1]);
                        mCustomList2.add("");
                        mCustomList2.add("");
                        createTable(mCustomList2, document, font);
                    }
                }
            }
            sendEmail();
        } catch (Exception ignored) {

        }
    }

    private void createTable(List<String> mList, Document document, Font font) throws Exception {
        if (mList != null && mList.size() > 0) {
            int size = mList.size();
            PdfPTable table = new PdfPTable(size);
            if (size == 1) {
                table.setWidths(new float[]{25f});
            } else if (size == 2) {
                table.setWidths(new float[]{25f, 125f});
            } else if (size == 7) {
                table.setWidths(new float[]{20f, 10f, 15f, 10f, 20f, 10f, 15f});
            } else {
                table.setWidths(new float[]{25f, 50f, 25f, 50f});
            }

            for (int i = 0; i < size; i++) {
                Paragraph paragraph = new Paragraph(mList.get(i), font);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setUseBorderPadding(true);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // cell.setBorderWidth(1);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell.setMinimumHeight(25);// 设置表格的最小高度
                table.addCell(cell);
            }
            table.completeRow();
            document.add(table);
        }

    }

    private void createImageTable(List<Image> list, Document document, PdfPTable table) throws Exception {
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                PdfPCell cell = new PdfPCell();
                cell.setImage(list.get(i));
                cell.setUseBorderPadding(true);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // cell.setBorderWidth(1);// 设置表格外线的宽度
                cell.setBorderColor(BaseColor.BLACK);// 设置表格外线的颜色
                cell.setMinimumHeight(50);// 设置表格的最小高度
                table.addCell(cell);
            }

        }
    }


    private void sendEmail() {
        String productIntroduction = introduceEd.getText().toString();
        String priceRange = priceMinEd.getText().toString() + "-" + priceMaxEd.getText().toString();
        String[] tos = new String[emailList.size()];
        for (int i = 0; i < emailList.size(); i++) {
            tos[i] = emailList.get(i);
        }
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        File file = new File("sdcard/EasyFair/GoodsDetailsforSupplier.pdf");
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        intent.putExtra(Intent.EXTRA_TEXT, productIntroduction + priceRange);
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("*/*");


        PackageManager pm = getContext().getPackageManager();
        //得到所有可以以蓝牙方式发送文件的应用
        List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
        String packageName = null;
        String className = null;
        if (appsList.size() > 0) {
            // 选择蓝牙
            boolean found = false;
            //便利所有应用的包名  如何和蓝牙的包名一样的话  就设置蓝牙启动
            for (ResolveInfo info : appsList) {
                packageName = info.activityInfo.packageName;
                if (packageName.contains("mail") || packageName.contains("gm")) {
                    className = info.activityInfo.name;
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(getContext(), R.string.the_phone_not_have_email, Toast.LENGTH_SHORT).show();
            }
        }
        myDialog.dialogDismiss();
        // 设置启动蓝牙intent
        intent.setClassName(packageName, className);
        startActivity(intent);

    }


    //请求网络的email数据
    private void initEmailData(final GetEmail getEmail) {
        String url = BaseUrl.HTTP_URL + "merchantInfo/getAllEmails";
        SharedPreferences edit = SharedprefenceStore.getSp();
        String token = edit.getString(SharedprefenceStore.TOKEN, "");
        JSONObject jsonObject = new JSONObject();
        String one = one_tv.getText().toString();
        String two = two_tv.getText().toString();
        String three = three_tv.getText().toString();
        String four = ListToString(tempSelcetIndustry);
        String industry = null;
        if (two != null && four != null) {
            industry = one + "|" + two + "|" + three + "|" + four;
        } else {
            industry = one + "|" + two + "|" + three;
        }
        try {
            jsonObject.put("token", token);
            jsonObject.put("userType", language.getTag());
            jsonObject.put("industryType", industry);
            jsonObject.put("type", 1);//1供应商，2采购商

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.doPost(getContext(), url, tag2, jsonObject, new VolleyCallBack(getContext(), VolleyCallBack.mListener, VolleyCallBack.mErrorListtener) {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (code.equals("200")) {
                        JSONArray emails = result.getJSONArray("emails");
                        Log.e("emails", emails.toString());
                        for (int i = 0; i < emails.length(); i++) {
                            String email = emails.get(i).toString();
                            emailList.add(email);
                        }
                        getEmail.onsuccess();
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

    interface GetEmail {
        void onsuccess();
    }

    //将从list集合中获得的数据以逗号拼接字符串
    public String ListToString(List<String> tempSelcetIndustry) {
        if (tempSelcetIndustry == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (String string : tempSelcetIndustry) {
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



