package com.baibeiyun.eazyfair.activity.supplier.offer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.myaccount.BaseDataActivity;
import com.baibeiyun.eazyfair.adapter.OfferBillAdapter;
import com.baibeiyun.eazyfair.dao.MyQuoteDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.XListView;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class OfferListActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout more_rl;//更多
    private TextView custom_name_tv;//客户名
    private ListView offer_list_lv;
    private OfferBillAdapter offerBillAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();

    private int from = 0;
    private PopupWindow mPopupWindow;
    private int idForCustomer;//当前客户的id
    private ArrayList<MyCustomer> myCustomers = new ArrayList<>();//存放客户
    private MyCustomer myCustomer;
    private ArrayList<MyUser> liforUser = new ArrayList<>();//存放个人信息
    private MyUser myUser;
    private List<CheckBox> mlist = new ArrayList<>();//存放CheckBox
    private ArrayList<String> listForSupplier = new ArrayList<>();//将该map集合加到list集合中
    private Map<Integer, String> map = new HashMap<>();
    private String name, company_name;
    private ArrayList<Integer> idforGoods;//得到返回的商品的id的集合

    public static OfferListActivity activity;
    private ArrayList<MyCommodity> myCommodites = new ArrayList<>();//存放供应商我的商品的集合


    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;


    private String unique_id;
    private String arr[];

    private ArrayList<MyQuote> myQuotes = new ArrayList<>();
    private MyQuote myQuote;

    private TextView all_tv;
    private TextView readly_tv;
    private TextView liuyang_tv;
    private TextView yuliu_tv;


    //已报价
    private ArrayList<MyQuote> myQuotesforReadly = new ArrayList<>();
    //预留报价
    private ArrayList<MyQuote> myQuotesforYuLiu = new ArrayList<>();
    //留样报价
    private ArrayList<MyQuote> myQuotesforLiuYang = new ArrayList<>();

    private String quote_type, quote_time;


    private TextView current_page_tv, total_page_tv;

    private int tag = 1;//tag标签 用来表明当前是那种报价类型

    //全部报价
    private ArrayList<MyQuote> myQuotesforAllNumbers = new ArrayList<>();

    //已报价
    private ArrayList<MyQuote> myQuotesforYiBaoJiaNumbers = new ArrayList<>();

    //留样报价
    private ArrayList<MyQuote> myQuotesforLiuYangNumbers = new ArrayList<>();

    //预留报价
    private ArrayList<MyQuote> myQuotesforYuLiuNumbers = new ArrayList<>();


    private int size;//总条数
    private double page;//总的页数
    private int t = 0;//当前页数
    private int k = 0;//从第几条开始查询

    private int color_blue, color_black;
    private Handler mHandler;
    private RelativeLayout show_page_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        activity = this;
        initYuyan();
        initview();
        initData();
        initFloat();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(OfferListActivity.this);
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

    private void initFloat() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        //跳转
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jump(OfferListActivity.this);
//            }
//        });
        //添加商品
        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_tv.setTextColor(color_blue);
                readly_tv.setTextColor(color_black);
                liuyang_tv.setTextColor(color_black);
                yuliu_tv.setTextColor(color_black);
                Intent intent = new Intent(OfferListActivity.this, GoodsListForSupplierActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //上一页
        FloatingActionButton last_page_fb = (FloatingActionButton) findViewById(R.id.last_page_fb);
        last_page_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_page_ll.setVisibility(View.GONE);
                        t--;
                        if (t >= 0) {
                            k = 5 * t;
                            SelectTypeQuote();
                            offerBillAdapter.notifyDataSetChanged();
                            int p = t + 1;
                            current_page_tv.setText(p + "");
                        } else {
                            ToastUtil.showToast2(OfferListActivity.this, R.string.one_page);
                            t = 0;
                        }
                    }
                }, 1000);
                show_page_ll.setVisibility(View.VISIBLE);
            }
        });
        //下一页
        FloatingActionButton next_page_fb = (FloatingActionButton) findViewById(R.id.next_page_fb);
        next_page_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_page_ll.setVisibility(View.GONE);
                        t++;
                        if (t < page) {
                            k = 5 * t;
                            SelectTypeQuote();
                            offerBillAdapter.notifyDataSetChanged();
                            int p = t + 1;
                            current_page_tv.setText(p + "");
//                    onLoad();
                        } else {
                            ToastUtil.showToast2(OfferListActivity.this, R.string.last_page);
                            t = (int) page;
//                    onLoad();
                        }
                    }
                }, 1000);
                show_page_ll.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (myCustomers != null) {
            myCustomers.clear();
            myCustomers = null;
        }
        if (liforUser != null) {
            liforUser.clear();
            liforUser = null;
        }
        if (mlist != null) {
            mlist.clear();
            mlist = null;
        }
        if (listForSupplier != null) {
            listForSupplier.clear();
            listForSupplier = null;
        }
        if (map != null) {
            map.clear();
            map = null;
        }
        if (idforGoods != null) {
            idforGoods.clear();
            idforGoods = null;
        }
        if (myCommodites != null) {
            myCommodites.clear();
            myCommodites = null;
        }

        if (myQuotes != null) {
            myQuotes.clear();
            myQuotes = null;
        }
        if (myQuotesforReadly != null) {
            myQuotesforReadly.clear();
            myQuotesforReadly = null;
        }
        if (myQuotesforYuLiu != null) {
            myQuotesforYuLiu.clear();
            myQuotesforYuLiu = null;
        }
        if (myQuotesforLiuYang != null) {
            myQuotesforLiuYang.clear();
            myQuotesforLiuYang = null;
        }
        if (myQuotesforAllNumbers != null) {
            myQuotesforAllNumbers.clear();
            myQuotesforAllNumbers = null;
        }
        if (myQuotesforYiBaoJiaNumbers != null) {
            myQuotesforYiBaoJiaNumbers.clear();
            myQuotesforYiBaoJiaNumbers = null;
        }
        if (myQuotesforLiuYangNumbers != null) {
            myQuotesforLiuYangNumbers.clear();
            myQuotesforLiuYangNumbers = null;
        }
        if (myQuotesforYuLiuNumbers != null) {
            myQuotesforYuLiuNumbers.clear();
            myQuotesforYuLiuNumbers = null;
        }

    }

    private void initview() {
        //蓝色
        color_blue = getResources().getColor(R.color.blue);
        //黑色
        color_black = getResources().getColor(R.color.black);
        show_page_ll = (RelativeLayout) findViewById(R.id.show_page_ll);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        more_rl = (RelativeLayout) findViewById(R.id.more_rl);
        custom_name_tv = (TextView) findViewById(R.id.custom_name_tv);
        offer_list_lv = (ListView) findViewById(R.id.offer_list_lv);
//        offer_list_lv.setPullLoadEnable(true);//允许上拉加载
//        offer_list_lv.setPullRefreshEnable(true);
//        offer_list_lv.setXListViewListener(this);
        mHandler = new Handler();

        all_tv = (TextView) findViewById(R.id.all_tv);
        readly_tv = (TextView) findViewById(R.id.readly_tv);
        liuyang_tv = (TextView) findViewById(R.id.liuyang_tv);
        yuliu_tv = (TextView) findViewById(R.id.yuliu_tv);
        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        more_rl.setOnClickListener(OfferListActivity.this);
        fanhui_rl.setOnClickListener(OfferListActivity.this);
        all_tv.setOnClickListener(OfferListActivity.this);
        readly_tv.setOnClickListener(OfferListActivity.this);
        liuyang_tv.setOnClickListener(OfferListActivity.this);
        yuliu_tv.setOnClickListener(OfferListActivity.this);
    }

    private void initData() {
        Intent intent = getIntent();
        //得到上一个页面传递过来的客户id
        idForCustomer = intent.getIntExtra("id", 0);
        myCustomer = CursorUtils.selectCustomerById(OfferListActivity.this, idForCustomer);
        myUser = CursorUtils.selectOurInfo(OfferListActivity.this);
        name = myCustomer.getName();//客户名
        company_name = myCustomer.getCompany_name();//客户的公司名
        unique_id = myCustomer.getUnique_id();
        arr = new String[]{name, company_name};
        myQuotesforAllNumbers = CursorUtils.selectQuoteNumberforAll(OfferListActivity.this, unique_id);
        size = myQuotesforAllNumbers.size();//总条数
        page = Math.ceil((double) size / (double) 5);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (page != 0) {
            total_page_tv.setText(decimalFormat.format(page));
        } else {
            total_page_tv.setText("1");
        }
        current_page_tv.setText("1");
        myQuotes = CursorUtils.selectQuoteforAll(OfferListActivity.this, unique_id, 0);
        custom_name_tv.setText(company_name);
        getData();
        offerBillAdapter = new OfferBillAdapter(datas, OfferListActivity.this, map, mlist);
        offer_list_lv.setAdapter(offerBillAdapter);
        offerBillAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //全部类型
        if (quote_type == null) {
            all_tv.setTextColor(color_blue);
            readly_tv.setTextColor(color_black);
            liuyang_tv.setTextColor(color_black);
            yuliu_tv.setTextColor(color_black);
            mHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            myQuotesforAllNumbers = CursorUtils.selectQuoteNumberforAll(getApplicationContext(), unique_id);
                            size = myQuotesforAllNumbers.size();
                            myQuotes = CursorUtils.selectQuoteforAll(OfferListActivity.this, unique_id, k);
                            getData();
                            offerBillAdapter.notifyDataSetChanged();
                        }

                    }, 1000);

        } else if (quote_type.equals("1")) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //已报价
                    myQuotesforYiBaoJiaNumbers = CursorUtils.selectQuoteNumberforYiBaoJia(getApplicationContext(), unique_id);
                    size = myQuotesforYiBaoJiaNumbers.size();

                    myQuotesforReadly = CursorUtils.selectQuoteAllReadly(OfferListActivity.this, unique_id, k);
                    getReadlyData();
                    offerBillAdapter.notifyDataSetChanged();
                }
            }, 1000);

        } else if (quote_type.equals("2")) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //预留报价
                    myQuotesforYuLiuNumbers = CursorUtils.selectQuoteNumberforYuLiu(getApplicationContext(), unique_id);
                    size = myQuotesforYuLiuNumbers.size();

                    myQuotesforYuLiu = CursorUtils.selectQuoteYuLiu(OfferListActivity.this, unique_id, k);
                    getYuLiuData();
                    offerBillAdapter.notifyDataSetChanged();
                }
            }, 1000);

        } else if (quote_type.equals("3")) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //留样报价
                    myQuotesforLiuYangNumbers = CursorUtils.selectQuoteNumberforLiuYang(getApplicationContext(), unique_id);
                    size = myQuotesforLiuYangNumbers.size();
                    myQuotesforLiuYang = CursorUtils.selectQuoteLiuYang(OfferListActivity.this, unique_id, k);
                    getLiuYangData();
                    offerBillAdapter.notifyDataSetChanged();
                }
            }, 1000);

        }
        page = Math.ceil((double) size / (double) 5);
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
        offerBillAdapter.notifyDataSetChanged();
    }


    //全部类型的数据源
    private void getData() {
        if (datas != null) {
            datas.clear();
        }
        for (MyQuote myQuote : myQuotes) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myQuote.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("_id", myQuote.get_id());
            data.put("currency_variety", myQuote.getCurrency_varitety());
            data.put("quote_type", myQuote.getQuote_type());
            datas.add(data);
        }

    }


    //已报价的数据源
    private void getReadlyData() {
        datas.clear();
        for (MyQuote myQuote : myQuotesforReadly) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myQuote.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("_id", myQuote.get_id());
            data.put("currency_variety", myQuote.getCurrency_varitety());
            data.put("quote_type", myQuote.getQuote_type());
            datas.add(data);
        }
    }

    //预留报价的数据源
    private void getYuLiuData() {
        datas.clear();
        for (MyQuote myQuote : myQuotesforYuLiu) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myQuote.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("_id", myQuote.get_id());
            data.put("currency_variety", myQuote.getCurrency_varitety());
            data.put("quote_type", myQuote.getQuote_type());
            datas.add(data);
        }
    }

    //留样报价的数据源
    private void getLiuYangData() {
        datas.clear();
        for (MyQuote myQuote : myQuotesforLiuYang) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myQuote.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("_id", myQuote.get_id());
            data.put("currency_variety", myQuote.getCurrency_varitety());
            data.put("quote_type", myQuote.getQuote_type());
            datas.add(data);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //更多 弹出popupWindow
            case R.id.more_rl:
                if (myUser != null) {
                    from = Location.BOTTOM.ordinal();
                    initPopupWindow();
                } else {
                    showDialog(OfferListActivity.this, R.string.save_info);
                }

                break;

            //全部报价类型
            case R.id.all_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }

                clearData();
                tag = 1;
                all_tv.setTextColor(color_blue);
                readly_tv.setTextColor(color_black);
                liuyang_tv.setTextColor(color_black);
                yuliu_tv.setTextColor(color_black);

                clear();
                myQuotesforAllNumbers = CursorUtils.selectQuoteNumberforAll(OfferListActivity.this, unique_id);
                size = myQuotesforAllNumbers.size();//总条数
                setTotalPage(size);
                t = 0;
                current_page_tv.setText("1");
                //从第一条开始查询
                myQuotes = CursorUtils.selectQuoteforAll(OfferListActivity.this, unique_id, 0);
                getData();
                offerBillAdapter = new OfferBillAdapter(datas, OfferListActivity.this, map, mlist);
                offer_list_lv.setAdapter(offerBillAdapter);
                offerBillAdapter.notifyDataSetChanged();

                break;
            //已报价
            case R.id.readly_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                clearData();
                tag = 2;
                readly_tv.setTextColor(color_blue);
                all_tv.setTextColor(color_black);
                liuyang_tv.setTextColor(color_black);
                yuliu_tv.setTextColor(color_black);

                clear();
                myQuotesforYiBaoJiaNumbers = CursorUtils.selectQuoteNumberforYiBaoJia(OfferListActivity.this, unique_id);
                size = myQuotesforYiBaoJiaNumbers.size();//总条数
                setTotalPage(size);
                t = 0;
                current_page_tv.setText("1");

                datas.clear();
                //从第一条开始查询
                myQuotesforReadly = CursorUtils.selectQuoteAllReadly(OfferListActivity.this, unique_id, 0);
                getReadlyData();
                offerBillAdapter = new OfferBillAdapter(datas, OfferListActivity.this, map, mlist);
                offer_list_lv.setAdapter(offerBillAdapter);
                offerBillAdapter.notifyDataSetChanged();
                break;
            //预留报价
            case R.id.yuliu_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                clearData();
                tag = 3;
                yuliu_tv.setTextColor(color_blue);
                readly_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);
                liuyang_tv.setTextColor(color_black);

                clear();
                myQuotesforYuLiuNumbers = CursorUtils.selectQuoteNumberforYuLiu(OfferListActivity.this, unique_id);
                size = myQuotesforYuLiuNumbers.size();
                setTotalPage(size);
                t = 0;
                current_page_tv.setText("1");

                datas.clear();
                //从第一条开始查询
                myQuotesforYuLiu = CursorUtils.selectQuoteYuLiu(OfferListActivity.this, unique_id, 0);

                getYuLiuData();
                offerBillAdapter = new OfferBillAdapter(datas, OfferListActivity.this, map, mlist);
                offer_list_lv.setAdapter(offerBillAdapter);
                offerBillAdapter.notifyDataSetChanged();
                break;
            //留样报价
            case R.id.liuyang_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                clearData();
                tag = 4;
                liuyang_tv.setTextColor(color_blue);
                readly_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);
                yuliu_tv.setTextColor(color_black);

                clear();
                myQuotesforLiuYangNumbers = CursorUtils.selectQuoteNumberforLiuYang(OfferListActivity.this, unique_id);
                size = myQuotesforLiuYangNumbers.size();//总条数
                setTotalPage(size);
                t = 0;
                current_page_tv.setText("1");
                datas.clear();
                //从第一条开始查询
                myQuotesforLiuYang = CursorUtils.selectQuoteLiuYang(OfferListActivity.this, unique_id, 0);
                getLiuYangData();
                offerBillAdapter = new OfferBillAdapter(datas, OfferListActivity.this, map, mlist);
                offer_list_lv.setAdapter(offerBillAdapter);
                offerBillAdapter.notifyDataSetChanged();
                break;

        }
    }

    private void showDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.click_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(OfferListActivity.this, BaseDataActivity.class);
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

    private void SelectTypeQuote() {
        switch (tag) {
            //查询全部类型
            case 1:
                clearData();
                myQuotes = CursorUtils.selectQuoteforAll(OfferListActivity.this, unique_id, k);
                getData();
                break;
            //查询已报价类型
            case 2:
                clearData();
                myQuotesforReadly = CursorUtils.selectQuoteAllReadly(OfferListActivity.this, unique_id, k);
                getReadlyData();
                break;
            //查询预留报价
            case 3:
                clearData();
                myQuotesforYuLiu = CursorUtils.selectQuoteYuLiu(OfferListActivity.this, unique_id, k);
                getYuLiuData();
                break;
            //查询留样报价
            case 4:
                clearData();
                myQuotesforLiuYang = CursorUtils.selectQuoteLiuYang(OfferListActivity.this, unique_id, k);
                getLiuYangData();
                break;
        }
    }

    private void clearData() {
        if (myQuotes != null) {
            myQuotes.clear();
        }
        if (myQuotesforReadly != null) {
            myQuotesforReadly.clear();
        }
        if (myQuotesforYuLiu != null) {
            myQuotesforYuLiu.clear();
        }
        if (myQuotesforLiuYang != null) {
            myQuotesforLiuYang.clear();
        }
    }


    //设置总页数的方法
    private void setTotalPage(double size) {
        page = Math.ceil(size / (double) 5);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
    }

    //    清除的方法
    private void clear() {
        if (myQuotesforAllNumbers != null) {
            myQuotesforAllNumbers.clear();
        }
        if (myQuotesforYiBaoJiaNumbers != null) {
            myQuotesforYiBaoJiaNumbers.clear();
        }
        if (myQuotesforYuLiuNumbers != null) {
            myQuotesforYuLiuNumbers.clear();
        }
        if (myQuotesforLiuYangNumbers != null) {
            myQuotesforLiuYangNumbers.clear();
        }
    }


    //点击更多的PopupWindow弹出的方向
    private enum Location {
        BOTTOM
    }

    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_share, null);
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
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_goods_detail, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(OfferListActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });
        LinearLayout msg_ch_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_ch_ll);
        LinearLayout msg_en_ll= (LinearLayout) popupWindowView.findViewById(R.id.msg_en_ll);
        LinearLayout cancel_ll = (LinearLayout) popupWindowView.findViewById(R.id.cancel_ll);
        //中文版发送
        msg_ch_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGoodsData();
                Intent intent = new Intent(OfferListActivity.this, SendOfferGoodsDataActivity.class);
                if (listForSupplier.size() != 0) {
                    intent.putStringArrayListExtra("listForSupplier", listForSupplier);
                    intent.putExtra("company_name", company_name);
                    intent.putExtra("name", name);
                    intent.putExtra("lang", "ch");
                    startActivity(intent);
                } else {
                    Alerter.create(OfferListActivity.this)
                            .setText(R.string.please_xzyfsdsp)
                            .setDuration(800)
                            .show();
                }
                mPopupWindow.dismiss();

            }
        });
        //英文版发送
        msg_en_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsData();
                Intent intent = new Intent(OfferListActivity.this, SendOfferGoodsDataActivity.class);
                if (listForSupplier.size() != 0) {
                    intent.putStringArrayListExtra("listForSupplier", listForSupplier);
                    intent.putExtra("company_name", company_name);
                    intent.putExtra("name", name);
                    intent.putExtra("lang", "en");
                    startActivity(intent);
                } else {
                    Alerter.create(OfferListActivity.this)
                            .setText(R.string.please_xzyfsdsp)
                            .setDuration(800)
                            .show();
                }


                mPopupWindow.dismiss();

            }
        });
        //取消
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }


    //设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(OfferListActivity.this, 1f);
        }

    }


    //将map集合加到list集合中的方法
    public void getGoodsData() {
        if (map == null) return;
        Set<Integer> integers = map.keySet();
        for (Integer key : integers) {
            String s = map.get(key);
            listForSupplier.add(s);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            idforGoods = (ArrayList<Integer>) data.getSerializableExtra("idforGoods");
            quote_type = data.getStringExtra("quote_type");
            quote_time = data.getStringExtra("quote_time");
            myCommodites = CursorUtils.selectCommodityIds(OfferListActivity.this, idforGoods);
            addGoods();
            myQuotes = CursorUtils.selectQuoteforAll(OfferListActivity.this, unique_id, 1);
            getData();
            offerBillAdapter.notifyDataSetChanged();
        }
    }


    //将查询到的供应商我的商品中的数据添加到报价表中
    private void addGoods() {
        for (MyCommodity myCommodity : myCommodites) {
            myQuote = new MyQuote();
            myQuote.set_id(ImageFactoryandOther.getStringRandom(5));//报价ID
            myQuote.setCommodity_id(myCommodity.get_id());
            myQuote.setSerial_number(myCommodity.getSerial_number());
            myQuote.setGoods_name(myCommodity.getName());
            myQuote.setGoods_unit(myCommodity.getUnit());
            myQuote.setMaterial(myCommodity.getMaterial());
            myQuote.setColor(myCommodity.getColor());
            myQuote.setPrice(myCommodity.getPrice());
            myQuote.setCurrency_varitety(myCommodity.getCurrency_variety());
            myQuote.setPrice_clause(myCommodity.getPrice_clause());
            myQuote.setPrice_clause_diy(myCommodity.getPrice_clause_diy());
            myQuote.setRemark(myCommodity.getRemark());
            myQuote.setIntroduction(myCommodity.getIntroduction());
            myQuote.setSelf_defined(myCommodity.getDiy());
            myQuote.setProduct_imgs1(myCommodity.getProduct_imgs1());
            myQuote.setProduct_imgs2(myCommodity.getProduct_imgs2());
            myQuote.setProduct_imgs3(myCommodity.getProduct_imgs3());
            myQuote.setProduct_imgs4(myCommodity.getProduct_imgs4());
            myQuote.setBuyer_name(name);
            myQuote.setBuyer_companyname(company_name);
            myQuote.setBuyer_phone("");
            myQuote.setQuote_type(quote_type);
            myQuote.setQuote_time(quote_time);
            myQuote.setUniqued_id(unique_id);
            myQuote.setMoq(myCommodity.getMoq());
            myQuote.setGoods_weight(myCommodity.getGoods_weight());
            myQuote.setGoods_weight_unit(myCommodity.getGoods_weight_unit());

            myQuote.setOutbox_number(myCommodity.getOutbox_number());
            myQuote.setOutbox_size(myCommodity.getOutbox_size());
            myQuote.setOutbox_weight(myCommodity.getOutbox_weight());
            myQuote.setOutbox_weight_unit(myCommodity.getOutbox_weight_unit());
            myQuote.setCenterbox_number(myCommodity.getCenterbox_number());
            myQuote.setCenterbox_size(myCommodity.getCenterbox_size());
            myQuote.setCenterbox_weight(myCommodity.getCenterbox_weight());
            myQuote.setCenterbox_weight_unit(myCommodity.getCenterbox_weight_unit());
            MyQuoteDao myQuoteDao = new MyQuoteDao(OfferListActivity.this);
            myQuoteDao.insertData(myQuote);
        }

    }

    //跳转至第几页
    private void jump(final Context context) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.jump_to_layout_dialog, null);
        dialog.setContentView(view);
        final EditText editText = (EditText) view.findViewById(R.id.jump_et);
        Button jump_dialog_sure = (Button) view.findViewById(R.id.jump_dialog_sure);
        Button jump_dialog_cancel = (Button) view.findViewById(R.id.jump_dialog_cancel);
        //解决不能弹出输入法的问题
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        jump_dialog_sure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String trim = editText.getText().toString().trim();
                        Integer integer = Integer.valueOf(trim);
                        t = integer - 1;
                        if (integer <= page && integer != 0) {
                            k = (integer - 1) * 5;
                            SelectTypeQuote();
                            offerBillAdapter.notifyDataSetChanged();
                            current_page_tv.setText(trim);
                            dialog.dismiss();
                        } else {
                            ToastUtil.showToast2(context, R.string.input_page_error);
                            editText.setText("");
                        }
                    }
                });
        jump_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }

    //上一页
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                t--;
//                if (t >= 0) {
//                    k = 5 * t;
//                    SelectTypeQuote();
//                    offerBillAdapter.notifyDataSetChanged();
//                    int p = t + 1;
//                    current_page_tv.setText(p + "");
////                    onLoad();
//                } else {
//                    ToastUtil.showToast2(OfferListActivity.this, R.string.one_page);
//                    t = 0;
////                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//
//    }

    //下一页
//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                t++;
//                if (t < page) {
//                    k = 5 * t;
//                    SelectTypeQuote();
//                    offerBillAdapter.notifyDataSetChanged();
//                    int p = t + 1;
//                    current_page_tv.setText(p + "");
////                    onLoad();
//                } else {
//                    ToastUtil.showToast2(OfferListActivity.this, R.string.last_page);
//                    t = (int) page;
////                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//
//    }

//    //停止刷新
//    private void onLoad() {
//        offer_list_lv.stopRefresh();
//        offer_list_lv.stopLoadMore();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myUser = CursorUtils.selectOurInfo(OfferListActivity.this);
    }
}
