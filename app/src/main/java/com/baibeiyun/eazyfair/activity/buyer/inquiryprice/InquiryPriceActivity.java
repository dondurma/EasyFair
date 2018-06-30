package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.activity.myaccount.BaseDataActivity;
import com.baibeiyun.eazyfair.adapter.XunJiaTypeAdapter;
import com.baibeiyun.eazyfair.dao.MyInquiryDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.entities.MyInquiry;
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

public class InquiryPriceActivity extends BaseActivityforBuyer implements View.OnClickListener {

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private RelativeLayout fanhui_rl;
    private RelativeLayout send_rl;
    private ListView xunjia_lv;

    private RelativeLayout all_rl;
    private RelativeLayout yixunjia_rl;
    private RelativeLayout yuliuxunjia_rl;
    private RelativeLayout liuyangxunjia_rl;
    private TextView all_tv;
    private TextView yixunjia_tv;
    private TextView yuliuxunjia_tv;
    private TextView liuyangxunjia_tv;

    private EditText input_goods_name_et;


    private int from = 0;
    private PopupWindow mPopupWindow;

    private String companyname, name, unique_id;//接收到上个页面传递过来的数据

    private XunJiaTypeAdapter xunjiatypeAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();

    private ArrayList<Integer> commidityforid;//得到传递过来的商品id的集合
    private String time, type;

    private ArrayList<MyCommodity> myCommodities = new ArrayList<>();//存放商品的集合

    //全部类型
    private ArrayList<MyInquiry> myInquiriesforAll = new ArrayList<>();

    //已询价
    private ArrayList<MyInquiry> myInquiriesforYiXunJia = new ArrayList<>();

    //预留询价
    private ArrayList<MyInquiry> myInquiriesforYuLiu = new ArrayList<>();

    //留样询价
    private ArrayList<MyInquiry> myInquiriesforLiuYang = new ArrayList<>();

    private int color_green, color_black;

    private List<CheckBox> mlist = new ArrayList<>();//存放CheckBox
    private ArrayList<String> listForBuyer = new ArrayList<>();//将该map集合加到list集合中
    private Map<Integer, String> map = new HashMap<>();

    public static InquiryPriceActivity activity;

    //模糊查询
    private ArrayList<MyInquiry> myInquiries = new ArrayList<>();
    private MyInquiry myInquiry;
    private String search;

    private TextView current_page_tv;//当前页
    private TextView total_page_tv;//总页数


    //查询全部类型的数量
    private ArrayList<MyInquiry> myInquiriesAllforNumber = new ArrayList<>();


    private int k = 0;//第几条开始查询
    private int i = 0;//当前页数
    private int tag = 1;//tag标签 用来标记当前处于那种类型
    private double page;//总的页数

    private int size;//总条数


    //已询价类型的数量
    private ArrayList<MyInquiry> myInquiriesforYiNumbers = new ArrayList<>();

    //预留询价类型的数量
    private ArrayList<MyInquiry> myInquiriesforYuLiuNumbers = new ArrayList<>();

    //留样询价的数量
    private ArrayList<MyInquiry> myInquiriesforLiuYangNumbers = new ArrayList<>();

    private LinearLayout show_page_ll;
    private Handler mHandler;
    private MyUser myUser;
    private MyCustomer myCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_price);
        activity = this;
        initYuyan();
        initView();
        initData();
        initFloat();
    }

    private void initFloat() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jump(InquiryPriceActivity.this);
//            }
//        });

        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InquiryPriceActivity.this, GoodsListForBuyerActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("companyname", companyname);
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
                        i--;
                        if (i >= 0) {
                            k = 5 * i;//从第k条开始查询
                            selectTypeInquiry();
                            showList();
                            int p = i + 1;
                            current_page_tv.setText(p + "");
                        } else {
                            ToastUtil.showToast2(InquiryPriceActivity.this, R.string.one_page);
                            i = 0;
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
                        i++;
                        if (i < page) {
                            k = 5 * i;//从第k条往后查
                            selectTypeInquiry();
                            showList();
                            int p = i + 1;
                            current_page_tv.setText(p + "");
                        } else {
                            ToastUtil.showToast2(InquiryPriceActivity.this, R.string.last_page);
                            i = (int) page;
                        }
                    }
                }, 1000);
                show_page_ll.setVisibility(View.VISIBLE);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (type == null) {
            all_tv.setTextColor(color_green);
            yixunjia_tv.setTextColor(color_black);
            yuliuxunjia_tv.setTextColor(color_black);
            liuyangxunjia_tv.setTextColor(color_black);

            //全部类型
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myInquiriesAllforNumber = CursorUtils.selectInquiryforAllNumbers(InquiryPriceActivity.this, unique_id);
                    size = myInquiriesAllforNumber.size();
                    myInquiriesforAll = CursorUtils.selectInquiryAll(InquiryPriceActivity.this, unique_id, k);
                    getDataforAll();
                    showList();
                }
            }, 1000);
        } else if (type.equals("1")) {
            //已询价
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myInquiriesforYiNumbers = CursorUtils.selectInquiryforYiNumbers(InquiryPriceActivity.this, unique_id);
                    size = myInquiriesforYiNumbers.size();
                    myInquiriesforYiXunJia = CursorUtils.selectInquiryYiXunJia(InquiryPriceActivity.this, unique_id, k);
                    getDataforYiXunJia();
                    showList();
                }
            }, 1000);

        } else if (type.equals("2")) {
            //预留询价
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myInquiriesforYuLiuNumbers = CursorUtils.selectInquiryforYuLiuNumbers(InquiryPriceActivity.this, unique_id);
                    size = myInquiriesforYuLiuNumbers.size();
                    myInquiriesforYuLiu = CursorUtils.selectInquiryYuLiu(InquiryPriceActivity.this, unique_id, k);
                    getDataforYuLiu();
                    showList();
                }
            }, 1000);

        } else if (type.equals("3")) {
            //留样询价
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myInquiriesforLiuYangNumbers = CursorUtils.selectInquiryforLiuYangNumbers(InquiryPriceActivity.this, unique_id);
                    size = myInquiriesforLiuYangNumbers.size();
                    myInquiriesforLiuYang = CursorUtils.selectInquiryLiuYang(InquiryPriceActivity.this, unique_id, k);
                    getDataforLiuYang();
                    showList();
                }
            }, 1000);

        }
        page = Math.ceil((double) size / (double) 5);
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
        xunjiatypeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (myCommodities != null) {
            myCommodities.clear();
            myCommodities = null;
        }
        if (myInquiriesforAll != null) {
            myInquiriesforAll.clear();
            myInquiriesforAll = null;
        }
        if (myInquiriesforYiXunJia != null) {
            myInquiriesforYiXunJia.clear();
            myInquiriesforYiXunJia = null;
        }
        if (myInquiriesforYuLiu != null) {
            myInquiriesforYuLiu.clear();
            myInquiriesforYuLiu = null;
        }
        if (myInquiriesforLiuYang != null) {
            myInquiriesforLiuYang.clear();
            myInquiriesforLiuYang = null;
        }
        if (mlist != null) {
            mlist.clear();
            mlist = null;
        }
        if (listForBuyer != null) {
            listForBuyer.clear();
            listForBuyer = null;
        }
        if (map != null) {
            map.clear();
            map = null;
        }
        if (myInquiries != null) {
            myInquiries.clear();
            myInquiries = null;
        }
        if (myInquiriesAllforNumber != null) {
            myInquiriesAllforNumber.clear();
            myInquiriesAllforNumber = null;
        }
        if (myInquiriesforYiNumbers != null) {
            myInquiriesforYiNumbers.clear();
            myInquiriesAllforNumber = null;
        }
        if (myInquiriesforYuLiuNumbers != null) {
            myInquiriesforYuLiuNumbers.clear();
            myInquiriesforYuLiuNumbers = null;
        }
        if (myInquiriesforLiuYangNumbers != null) {
            myInquiriesforLiuYangNumbers.clear();
            myInquiriesforLiuYangNumbers = null;
        }
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(InquiryPriceActivity.this);
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
        //绿色
        color_green = getResources().getColor(R.color.green_color);
        //黑色
        color_black = getResources().getColor(R.color.black);
        show_page_ll = (LinearLayout) findViewById(R.id.show_page_ll);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        send_rl = (RelativeLayout) findViewById(R.id.send_rl);
        xunjia_lv = (ListView) findViewById(R.id.xunjia_lv);
        mHandler = new Handler();
        all_rl = (RelativeLayout) findViewById(R.id.all_rl);
        yixunjia_rl = (RelativeLayout) findViewById(R.id.yixunjia_rl);
        yuliuxunjia_rl = (RelativeLayout) findViewById(R.id.yuliuxunjia_rl);
        liuyangxunjia_rl = (RelativeLayout) findViewById(R.id.liuyangxunjia_rl);

        all_tv = (TextView) findViewById(R.id.all_tv);
        yuliuxunjia_tv = (TextView) findViewById(R.id.yuliuxunjia_tv);
        yixunjia_tv = (TextView) findViewById(R.id.yixunjia_tv);
        liuyangxunjia_tv = (TextView) findViewById(R.id.liuyangxunjia_tv);
        input_goods_name_et = (EditText) findViewById(R.id.input_goods_name_et);

        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        fanhui_rl.setOnClickListener(InquiryPriceActivity.this);
        send_rl.setOnClickListener(InquiryPriceActivity.this);

        all_rl.setOnClickListener(InquiryPriceActivity.this);
        yixunjia_rl.setOnClickListener(InquiryPriceActivity.this);
        yuliuxunjia_rl.setOnClickListener(InquiryPriceActivity.this);
        liuyangxunjia_rl.setOnClickListener(InquiryPriceActivity.this);
        input_goods_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = charSequence.toString().trim();
                int yixunjia_color = yixunjia_tv.getCurrentTextColor();
                int yuliuxunjia_color = yuliuxunjia_tv.getCurrentTextColor();
                int liuyangxunjia_color = liuyangxunjia_tv.getCurrentTextColor();
                if (yixunjia_color == color_green) {
                    myInquiriesforYiXunJia.clear();
                    myInquiries.clear();
                    selectByLike();

                    getData();
                    xunjiatypeAdapter.notifyDataSetChanged();
                } else if (yuliuxunjia_color == color_green) {
                    myInquiriesforYuLiu.clear();
                    myInquiries.clear();
                    selectByLike();

                    getData();
                    xunjiatypeAdapter.notifyDataSetChanged();

                } else if (liuyangxunjia_color == color_green) {
                    myInquiriesforLiuYang.clear();
                    myInquiries.clear();
                    selectByLike();
                    getData();
                    xunjiatypeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        int customer_id = intent.getIntExtra("customer_id", 0);
        myCustomer = CursorUtils.selectCustomerById(InquiryPriceActivity.this, customer_id);
        unique_id = myCustomer.getUnique_id();
        companyname = myCustomer.getCompany_name();
        name = myCustomer.getName();

        myInquiriesAllforNumber = CursorUtils.selectInquiryforAllNumbers(InquiryPriceActivity.this, this.unique_id);

        size = myInquiriesAllforNumber.size();//总条数
        page = Math.ceil((double) size / (double) 5);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
        i = 0;
        current_page_tv.setText("1");

        myInquiriesforAll = CursorUtils.selectInquiryAll(InquiryPriceActivity.this, this.unique_id, 0);
        getDataforAll();
        showList();

        myUser = CursorUtils.selectOurInfo(this);
    }

    @Override
    public void onClick(View view) {
        int size;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;

            //发送
            case R.id.send_rl:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                if (myUser != null) {
                    listForBuyer.clear();
                    getGoodsData();
                    if (listForBuyer.isEmpty()) {
                        Alerter.create(InquiryPriceActivity.this)
                                .setText(R.string.select_send_goods)
                                .setDuration(800)
                                .show();
                    } else {
                        from = LocationforBottom.BOTTOM.ordinal();
                        //调用此方法，menu不会顶置
                        initPopupWindowforBottom();
                    }
                } else {
                    showDialog(InquiryPriceActivity.this, R.string.save_info);
                }

                break;
            //全部类型
            case R.id.all_rl:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                tag = 1;
                all_tv.setTextColor(color_green);
                yixunjia_tv.setTextColor(color_black);
                yuliuxunjia_tv.setTextColor(color_black);
                liuyangxunjia_tv.setTextColor(color_black);

                clear();
                myInquiriesAllforNumber = CursorUtils.selectInquiryforAllNumbers(InquiryPriceActivity.this, unique_id);
                size = myInquiriesAllforNumber.size();
                setTotalPage(size);
                i = 0;
                current_page_tv.setText("1");
                clearData();
                myInquiriesforAll = CursorUtils.selectInquiryAll(InquiryPriceActivity.this, unique_id, 0);
                getDataforAll();
                showList();
                break;
            //已询价
            case R.id.yixunjia_rl:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                tag = 2;
                yixunjia_tv.setTextColor(color_green);
                yuliuxunjia_tv.setTextColor(color_black);
                liuyangxunjia_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);

                clear();
                myInquiriesforYiNumbers = CursorUtils.selectInquiryforYiNumbers(InquiryPriceActivity.this, unique_id);
                size = myInquiriesforYiNumbers.size();
                setTotalPage(size);
                i = 0;
                current_page_tv.setText("1");

                clearData();
                myInquiriesforYiXunJia = CursorUtils.selectInquiryYiXunJia(InquiryPriceActivity.this, unique_id, 0);
                getDataforYiXunJia();
                showList();

                break;
            //预留询价
            case R.id.yuliuxunjia_rl:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                tag = 3;
                yuliuxunjia_tv.setTextColor(color_green);
                yixunjia_tv.setTextColor(color_black);
                liuyangxunjia_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);

                clear();
                myInquiriesforYuLiuNumbers = CursorUtils.selectInquiryforYuLiuNumbers(InquiryPriceActivity.this, unique_id);
                size = myInquiriesforYuLiuNumbers.size();
                setTotalPage(size);
                i = 0;
                current_page_tv.setText("1");
                clearData();
                myInquiriesforYuLiu = CursorUtils.selectInquiryYuLiu(InquiryPriceActivity.this, unique_id, 0);
                getDataforYuLiu();
                showList();

                break;
            //留样询价
            case R.id.liuyangxunjia_rl:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                tag = 4;
                liuyangxunjia_tv.setTextColor(color_green);
                yixunjia_tv.setTextColor(color_black);
                yuliuxunjia_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);

                clear();
                myInquiriesforLiuYangNumbers = CursorUtils.selectInquiryforLiuYangNumbers(InquiryPriceActivity.this, unique_id);
                size = myInquiriesforLiuYangNumbers.size();
                setTotalPage(size);
                i = 0;
                current_page_tv.setText("1");
                clearData();
                myInquiriesforLiuYang = CursorUtils.selectInquiryLiuYang(InquiryPriceActivity.this, unique_id, 0);
                getDataforLiuYang();
                showList();
                break;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myUser = CursorUtils.selectOurInfo(this);
    }

    private void showDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.click_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(InquiryPriceActivity.this, BaseDataActivity.class);
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


    private void jump(final Context context) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.jump_to_buyer_layout_dialog, null);
        dialog.setContentView(view);
        final EditText editText = (EditText) view.findViewById(R.id.jump_et);
        Button jump_dialog_sure = (Button) view.findViewById(R.id.jump_dialog_sure);
        Button jump_dialog_cancel = (Button) view.findViewById(R.id.jump_dialog_cancel);
        //解决不能弹出输入法的问题
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        jump_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                Integer integer = Integer.valueOf(trim);
                i = integer - 1;
                if (integer <= page && integer != 0) {
                    k = (integer - 1) * 5;
                    selectTypeInquiry();
                    showList();
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

    private void selectTypeInquiry() {
        switch (tag) {
            case 1:
                clearData();
                myInquiriesforAll = CursorUtils.selectInquiryAll(InquiryPriceActivity.this, unique_id, k);
                getDataforAll();
                break;
            case 2:
                clearData();
                myInquiriesforYiXunJia = CursorUtils.selectInquiryYiXunJia(InquiryPriceActivity.this, unique_id, k);
                getDataforYiXunJia();
                break;
            case 3:
                clearData();
                myInquiriesforYuLiu = CursorUtils.selectInquiryYuLiu(InquiryPriceActivity.this, unique_id, k);
                getDataforYuLiu();
                break;
            case 4:
                clearData();
                myInquiriesforLiuYang = CursorUtils.selectInquiryLiuYang(InquiryPriceActivity.this, unique_id, k);
                getDataforLiuYang();
                break;
        }
    }

    private void clearData() {
        if (myInquiriesforAll != null) {
            myInquiriesforAll.clear();
        }
        if (myInquiriesforYiXunJia != null) {
            myInquiriesforYiXunJia.clear();
        }
        if (myInquiriesforYuLiu != null) {
            myInquiriesforYuLiu.clear();
        }
        if (myInquiriesforLiuYang != null) {
            myInquiriesforLiuYang.clear();
        }
    }

    //清除的方法
    private void clear() {
        if (myInquiriesAllforNumber != null) {
            myInquiriesAllforNumber.clear();
        }
        if (myInquiriesforYiNumbers != null) {
            myInquiriesforYiNumbers.clear();
        }
        if (myInquiriesforYuLiuNumbers != null) {
            myInquiriesforYuLiuNumbers.clear();
        }
        if (myInquiriesforLiuYangNumbers != null) {
            myInquiriesforLiuYangNumbers.clear();
        }
    }

    private void showList() {
        xunjiatypeAdapter = new XunJiaTypeAdapter(datas, InquiryPriceActivity.this, map, mlist);
        xunjia_lv.setAdapter(xunjiatypeAdapter);
        xunjiatypeAdapter.notifyDataSetChanged();
    }

    //设置总页数的方法
    private void setTotalPage(double size) {
        page = Math.ceil(size / (double) 5);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
    }


    //上传图片的PopupWindow弹出的方向
    public enum Location {
        TOP
    }

    public enum LocationforBottom {
        BOTTOM
    }

    //设置添加屏幕的背景透明度
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
            backgroundAlpha(InquiryPriceActivity.this, 1f);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            time = data.getStringExtra("time");
            commidityforid = (ArrayList<Integer>) data.getSerializableExtra("commidityforid");
            type = data.getStringExtra("type");

            myCommodities = CursorUtils.selectCommodityIds(InquiryPriceActivity.this, commidityforid);
            addtoInquiry();
            if (type == null) {
                all_tv.setTextColor(color_green);
                yixunjia_tv.setTextColor(color_black);
                yuliuxunjia_tv.setTextColor(color_black);
                liuyangxunjia_tv.setTextColor(color_black);
                myInquiriesforAll = CursorUtils.selectInquiryAll(InquiryPriceActivity.this, unique_id, 0);
                getDataforAll();
                showList();
                //已询价
            } else if (type.equals("1")) {
                yixunjia_tv.setTextColor(color_green);
                yuliuxunjia_tv.setTextColor(color_black);
                liuyangxunjia_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);
                myInquiriesforYiXunJia = CursorUtils.selectInquiryYiXunJia(InquiryPriceActivity.this, unique_id, 0);
                getDataforYiXunJia();
                showList();
                //预留询价
            } else if (type.equals("2")) {
                yuliuxunjia_tv.setTextColor(color_green);
                yixunjia_tv.setTextColor(color_black);
                liuyangxunjia_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);
                myInquiriesforYuLiu = CursorUtils.selectInquiryYuLiu(InquiryPriceActivity.this, unique_id, 0);
                getDataforYuLiu();
                showList();

                //留样询价
            } else if (type.equals("3")) {
                liuyangxunjia_tv.setTextColor(color_green);
                yixunjia_tv.setTextColor(color_black);
                yuliuxunjia_tv.setTextColor(color_black);
                all_tv.setTextColor(color_black);
                myInquiriesforLiuYang = CursorUtils.selectInquiryLiuYang(InquiryPriceActivity.this, unique_id, 0);
                getDataforLiuYang();
                showList();
            }
        }
    }


    //将多个商品加到询价记录表中
    private void addtoInquiry() {
        for (MyCommodity myCommodity : myCommodities) {
            MyInquiry myInquiry = new MyInquiry();
            myInquiry.set_id(ImageFactoryandOther.getStringRandom(3));
            myInquiry.setCommodity_id(myCommodity.get_id());
            myInquiry.setSerial_number(myCommodity.getSerial_number());
            myInquiry.setGoods_name(myCommodity.getName());
            myInquiry.setGoods_unit(myCommodity.getUnit());
            myInquiry.setMaterial(myCommodity.getMaterial());
            myInquiry.setColor(myCommodity.getColor());
            myInquiry.setPrice(myCommodity.getPrice());
            myInquiry.setCurrency_varitety(myCommodity.getCurrency_variety());
            myInquiry.setPrice_clause(myCommodity.getPrice_clause());
            myInquiry.setPrice_clause_diy(myCommodity.getPrice_clause_diy());
            myInquiry.setRemark(myCommodity.getRemark());
            myInquiry.setIntroduction(myCommodity.getIntroduction());
            myInquiry.setSelf_defined(myCommodity.getDiy());
            myInquiry.setProduct_imgs1(myCommodity.getProduct_imgs1());
            myInquiry.setProduct_imgs2(myCommodity.getProduct_imgs2());
            myInquiry.setProduct_imgs3(myCommodity.getProduct_imgs3());
            myInquiry.setProduct_imgs4(myCommodity.getProduct_imgs4());
            myInquiry.setSupplier_name(name);
            myInquiry.setSupplier_companyname(companyname);
            myInquiry.setSupplier_phone("");

            myInquiry.setInquiry_type(type);
            myInquiry.setInquiry_time(time);
            myInquiry.setUniqued_id(unique_id);
            myInquiry.setMoq(myCommodity.getMoq());
            myInquiry.setGoods_weight(myCommodity.getGoods_weight());
            myInquiry.setGoods_weight_unit(myCommodity.getGoods_weight_unit());

            myInquiry.setOutbox_number(myCommodity.getOutbox_number());
            myInquiry.setOutbox_size(myCommodity.getOutbox_size());
            myInquiry.setOutbox_weight(myCommodity.getOutbox_weight());
            myInquiry.setOutbox_weight_unit(myCommodity.getOutbox_weight_unit());
            myInquiry.setCenterbox_number(myCommodity.getCenterbox_number());
            myInquiry.setCenterbox_size(myCommodity.getCenterbox_size());
            myInquiry.setCenterbox_weight(myCommodity.getCenterbox_weight());
            myInquiry.setCenterbox_weight_unit(myCommodity.getCenterbox_weight_unit());

            MyInquiryDao myInquiryDao = new MyInquiryDao(InquiryPriceActivity.this);
            myInquiryDao.insertData(myInquiry);
        }

    }


    //全部类型的数据源
    private void getDataforAll() {
        if (datas != null) {
            datas.clear();
        }
        for (MyInquiry myInquiry : myInquiriesforAll) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myInquiry.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_img_iv", bitmap);
            data.put("goods_name_tv", myInquiry.getGoods_name());
            data.put("goods_price_tv", myInquiry.getPrice());
            data.put("_id", myInquiry.get_id());
            data.put("currency_variety", myInquiry.getCurrency_varitety());
            data.put("inquiry_type", myInquiry.getInquiry_type());
            datas.add(data);
        }

    }

    //已询价的数据源
    private void getDataforYiXunJia() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiriesforYiXunJia) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myInquiry.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_img_iv", bitmap);
            data.put("goods_name_tv", myInquiry.getGoods_name());
            data.put("goods_price_tv", myInquiry.getPrice());
            data.put("_id", myInquiry.get_id());
            data.put("currency_variety", myInquiry.getCurrency_varitety());
            data.put("inquiry_type", myInquiry.getInquiry_type());
            datas.add(data);
        }

    }

    //预留询价的数据源
    private void getDataforYuLiu() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiriesforYuLiu) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myInquiry.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_img_iv", bitmap);
            data.put("goods_name_tv", myInquiry.getGoods_name());
            data.put("goods_price_tv", myInquiry.getPrice());
            data.put("_id", myInquiry.get_id());
            data.put("currency_variety", myInquiry.getCurrency_varitety());
            data.put("inquiry_type", myInquiry.getInquiry_type());
            datas.add(data);
        }


    }

    //留样询价的数据源
    private void getDataforLiuYang() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiriesforLiuYang) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myInquiry.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_img_iv", bitmap);
            data.put("goods_name_tv", myInquiry.getGoods_name());
            data.put("goods_price_tv", myInquiry.getPrice());
            data.put("_id", myInquiry.get_id());
            data.put("currency_variety", myInquiry.getCurrency_varitety());
            data.put("inquiry_type", myInquiry.getInquiry_type());
            datas.add(data);
        }

    }

    //构建数据源
    private void getData() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiries) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myInquiry.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_img_iv", bitmap);
            data.put("goods_name_tv", myInquiry.getGoods_name());
            data.put("goods_price_tv", myInquiry.getPrice());
            data.put("_id", myInquiry.get_id());
            data.put("currency_variety", myInquiry.getCurrency_varitety());
            data.put("inquiry_type", myInquiry.getInquiry_type());
            datas.add(data);
        }

    }

    //将map集合加到list集合中的方法
    public void getGoodsData() {
        if (map == null) return;
        Set<Integer> integers = map.keySet();
        for (Integer key : integers) {
            String s = map.get(key);
            listForBuyer.add(s);
        }
    }

    //发送的popupwindow
    private void initPopupWindowforBottom() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_share, null);
        if (LocationforBottom.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }

        //动画效果
        if (LocationforBottom.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (LocationforBottom.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_goods_detail, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(InquiryPriceActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        LinearLayout msg_ch_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_ch_ll);
        LinearLayout msg_en_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_en_ll);
        LinearLayout cancel_ll = (LinearLayout) popupWindowView.findViewById(R.id.cancel_ll);
        //发送
        //中文版
        msg_ch_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendInquiryGoodsDataActivity.class);
                if (listForBuyer.size()!=0) {
                    intent.putExtra("listForBuyer", listForBuyer);
                    intent.putExtra("name", name);
                    intent.putExtra("companyname", companyname);
                    intent.putExtra("lang", "ch");
                    startActivity(intent);
                }else{
                    Alerter.create(InquiryPriceActivity.this)
                            .setText(R.string.please_xzyfsdsp)
                            .setDuration(800)
                            .show();
                }

                mPopupWindow.dismiss();
            }
        });
        //英文版
        msg_en_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SendInquiryGoodsDataActivity.class);
                if (listForBuyer.size()!=0) {
                    intent.putExtra("listForBuyer", listForBuyer);
                    intent.putExtra("name", name);
                    intent.putExtra("companyname", companyname);
                    intent.putExtra("lang", "en");
                    startActivity(intent);
                }else{
                    Alerter.create(InquiryPriceActivity.this)
                            .setText(R.string.please_xzyfsdsp)
                            .setDuration(800)
                            .show();
                }
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


    //模糊查询的方法
    private ArrayList<MyInquiry> selectByLike() {
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(InquiryPriceActivity.this);
        Cursor cursor = myInquiryDao.queryByLike(search);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));
            myInquiry = new MyInquiry(id, goods_name, price, currency_varitety, product_imgs1, inquiry_type);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //上一页
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                i--;
//                if (i >= 0) {
//                    k = 5 * i;//从第k条开始查询
//                    selectTypeInquiry();
//                    showList();
//                    int p = i + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(InquiryPriceActivity.this, R.string.one_page);
//                    i = 0;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//    }

    //下一页
//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                i++;
//                if (i < page) {
//                    k = 5 * i;//从第k条往后查
//                    selectTypeInquiry();
//                    showList();
//                    int p = i + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(InquiryPriceActivity.this, R.string.last_page);
//                    i = (int) page;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//    }

    //停止刷新
//    private void onLoad() {
//        xunjia_lv.stopRefresh();
//        xunjia_lv.stopLoadMore();
//    }


}
