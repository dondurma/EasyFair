package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.activity.buyer.mygoods.NewGoodsOneForBuyerActivity;
import com.baibeiyun.eazyfair.adapter.InquiryGoodsAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
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

public class GoodsListForBuyerActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout add_rl;//确认添加
    private EditText input_goods_name_et;//输入商品名称
    private TextView select_time_tv;//选择时间
    private ListView goods_list_lv;
    private InquiryGoodsAdapter offerBillAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private ArrayList<MyCommodity> myCommodities = new ArrayList<>();//存放商品
    private PopupWindow mPopupWindow;
    private String time;
    private List<CheckBox> mlist = new ArrayList<>();//存放CheckBox
    private ArrayList<Integer> listForSupplier = new ArrayList<>();//将该map集合加到list集合中
    private Map<Integer, Integer> map = new HashMap<>();
    private String name, companyname;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private String inquiry_type;
    private int from = 0;
    private SharedPreferences pre2;

    private TextView current_page_tv;//当前页
    private TextView total_page_tv;//总页数

    private int k = 0;//默认从第0条开始查询
    private int i = 0;//当前页
    private int size;//总的条数

    private ArrayList<MyCommodity> myCommoditiesForNumbers = new ArrayList<>();

    private double page;//总的页数
    private LinearLayout show_page_ll;
    private Handler mHandler;
    private String tag;
    private RelativeLayout buildgoods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list_for_buyer);
        initYuyan();
        initView();
        initData();
        initFloat();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
        initData();
    }

    private void initFloat() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jump(GoodsListForBuyerActivity.this);
//            }
//        });
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
                            k = 10 * i;
                            myCommodities.clear();
                            myCommodities = CursorUtils.selectTypeforBuyer(GoodsListForBuyerActivity.this, k);
                            getData();
                            showList();
                            int p = i + 1;
                            current_page_tv.setText(p + "");
                        } else if (i < 0) {
                            ToastUtil.showToast2(GoodsListForBuyerActivity.this, R.string.one_page);
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
                            k = 10 * i;
                            myCommodities.clear();
                            myCommodities = CursorUtils.selectTypeforBuyer(GoodsListForBuyerActivity.this, k);
                            getData();
                            showList();
                            int p = i + 1;
                            current_page_tv.setText(p + "");
                        } else {
                            ToastUtil.showToast2(GoodsListForBuyerActivity.this, R.string.last_page);
                            i = (int) page;
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
        if (myCommodities != null) {
            myCommodities.clear();
            myCommodities = null;
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

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(GoodsListForBuyerActivity.this);
        if (language != null) {
            tag = language.getTag();
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
        show_page_ll = (LinearLayout) findViewById(R.id.show_page_ll);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        add_rl = (RelativeLayout) findViewById(R.id.add_rl);
        input_goods_name_et = (EditText) findViewById(R.id.input_goods_name_et);
        goods_list_lv = (ListView) findViewById(R.id.goods_list_lv);
//        goods_list_lv.setPullRefreshEnable(true);
//        goods_list_lv.setPullLoadEnable(true);
//        goods_list_lv.setXListViewListener(this);
        mHandler = new Handler();
        select_time_tv = (TextView) findViewById(R.id.select_time_tv);
        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        buildgoods = (RelativeLayout) findViewById(R.id.buildgoods);
        buildgoods.setOnClickListener(this);
        fanhui_rl.setOnClickListener(GoodsListForBuyerActivity.this);
        add_rl.setOnClickListener(GoodsListForBuyerActivity.this);
        input_goods_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String data = charSequence.toString().trim();
                myCommodities.clear();
                myCommodities = CursorUtils.findDataByBuyer(GoodsListForBuyerActivity.this, data);
                getData();
                offerBillAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initData() {
        pre2 = getSharedPreferences("mypre2", MODE_PRIVATE);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        companyname = intent.getStringExtra("companyname");
        myCommoditiesForNumbers = CursorUtils.selectGoodsforBuyerNumbers(GoodsListForBuyerActivity.this);
        size = myCommoditiesForNumbers.size();
        page = Math.ceil((double) size / (double) 10);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (page == 0) {
            total_page_tv.setText("1");
        } else {
            total_page_tv.setText(decimalFormat.format(page));
        }
        current_page_tv.setText("1");
        i = 0;
        myCommodities = CursorUtils.selectTypeforBuyer(GoodsListForBuyerActivity.this, 0);
        getData();
        showList();

    }

    private void showList() {
        offerBillAdapter = new InquiryGoodsAdapter(datas, GoodsListForBuyerActivity.this, map, mlist);
        goods_list_lv.setAdapter(offerBillAdapter);
        offerBillAdapter.notifyDataSetChanged();
    }

    private void getData() {
        datas.clear();
        for (MyCommodity s : myCommodities) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = s.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("goods_name_tv", s.getName());
            data.put("goods_price_tv", s.getPrice());
            data.put("_id", s.get_id());
            data.put("currency_variety", s.getCurrency_variety());
            datas.add(data);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                InquiryPriceActivity.activity.finish();//finish前面一个页面
                finish();
                break;
            case R.id.add_rl:
                //得到选中商品的集合
                getGoodsData();
                if (listForSupplier.size() != 0 && listForSupplier.size() == 1) {
                    selectQuoteType();
                } else if (listForSupplier.size() != 0 && listForSupplier.size() != 1) {
                    int toast2 = pre2.getInt("toast2", 0);
                    if (toast2 == 1) {
                        getIntent().putExtra("commidityforid", listForSupplier);
                        getIntent().putExtra("time", time);
                        getIntent().putExtra("type", inquiry_type);
                        setResult(1, getIntent());
                        finish();
                    } else {
                        showAlertDialog(GoodsListForBuyerActivity.this, R.string.confirm_tjh);
                    }

                } else {
                    Alerter.create(GoodsListForBuyerActivity.this)
                            .setText(R.string.please_add_goods)
                            .setDuration(800)
                            .show();
                }
                break;
            //新建商品
            case R.id.buildgoods:
                Intent intent = new Intent(GoodsListForBuyerActivity.this, NewGoodsOneForBuyerActivity.class);
                startActivity(intent);
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

    //将map集合加到list集合中的方法
    private void getGoodsData() {
        if (map == null) return;
        Set<Integer> integers = map.keySet();
        for (Integer key : integers) {
            int id = map.get(key);
            listForSupplier.add(id);
        }
    }

    //返回按钮的监听事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            InquiryPriceActivity.activity.finish();//finish前面一个页面
            finish();
            return false;
        }
        return false;
    }


    //选择询价类型及询价时间的popupWindow
    private void selectQuoteType() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_selectinquirytype, null);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_edit, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        //选择报价类型
        RadioGroup radioGroup = (RadioGroup) popupWindowView.findViewById(R.id.inquiry_type_rg);
        final RadioButton readly_inquiry_rb = (RadioButton) popupWindowView.findViewById(R.id.readly_inquiry_rb);
        final RadioButton yuliu_inquiry_rb = (RadioButton) popupWindowView.findViewById(R.id.yuliu_inquiry_rb);
        final RadioButton liuyang_inquiry_rb = (RadioButton) popupWindowView.findViewById(R.id.liuyang_inquiry_rb);
        LinearLayout select_time_ll = (LinearLayout) popupWindowView.findViewById(R.id.select_time_ll);
        final TextView select_time_tv = (TextView) popupWindowView.findViewById(R.id.select_time_tv);
        Button cancel = (Button) popupWindowView.findViewById(R.id.cancel_bt);
        Button confirm_bt = (Button) popupWindowView.findViewById(R.id.confirm_bt);
        final View view = popupWindowView.findViewById(R.id.view);
        final LinearLayout inquiry_ll = (LinearLayout) popupWindowView.findViewById(R.id.inquiry_ll);
        final String date = DateUtil.getDate();
        String[] split = date.split("\\-");
        final String year = split[0];
        final String month = split[1];
        final String day = split[2];
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == readly_inquiry_rb.getId()) {
                    inquiry_type = "1";
                    view.setVisibility(View.GONE);
                    inquiry_ll.setVisibility(View.GONE);
                    time = date;
                } else if (i == yuliu_inquiry_rb.getId()) {
                    inquiry_type = "2";
                    view.setVisibility(View.VISIBLE);
                    inquiry_ll.setVisibility(View.VISIBLE);
                } else if (i == liuyang_inquiry_rb.getId()) {
                    inquiry_type = "3";
                    view.setVisibility(View.VISIBLE);
                    inquiry_ll.setVisibility(View.VISIBLE);
                }
            }
        });
        //选择询价时间
        select_time_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_time_tv.setText(time);
                DatePickerDialog dpd = new DatePickerDialog(GoodsListForBuyerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        time = year + "-" + (month + 1) + "-" + day;
                        select_time_tv.setText(time);
                    }
                }, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                dpd.show();


            }
        });
        //确定
        confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntent().putExtra("commidityforid", listForSupplier);
                getIntent().putExtra("time", time);
                getIntent().putExtra("type", inquiry_type);
                setResult(1, getIntent());
                finish();
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


    //上传图片的PopupWindow弹出的方向
    private enum Location {
        BOTTOM
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(GoodsListForBuyerActivity.this, 1f);
        }
    }


    private void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getIntent().putExtra("commidityforid", listForSupplier);
                getIntent().putExtra("time", time);
                getIntent().putExtra("type", inquiry_type);
                setResult(1, getIntent());
                finish();

                dialog.dismiss();
            }
        });
        String prompt = null;
        if (tag.equals("en")) {
            prompt = "No tips";
        } else if (tag.equals("ch")) {
            prompt = "不再提示";
        }
        builder.setNegativeButton(prompt, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences.Editor edit = pre2.edit();
                edit.putInt("toast2", 1);
                edit.commit();
                dialog.dismiss();
                getIntent().putExtra("commidityforid", listForSupplier);
                getIntent().putExtra("time", time);
                getIntent().putExtra("type", inquiry_type);
                setResult(1, getIntent());
                finish();

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
                    k = (integer - 1) * 10;
                    myCommodities.clear();
                    myCommodities = CursorUtils.selectTypeforBuyer(GoodsListForBuyerActivity.this, k);
                    getData();
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

    //上一页
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                i--;
//                if (i >= 0) {
//                    k = 10 * i;
//                    myCommodities.clear();
//                    myCommodities = CursorUtils.selectTypeforBuyer(GoodsListForBuyerActivity.this, k);
//                    getData();
//                    showList();
//                    int p = i + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else if (i < 0) {
//                    ToastUtil.showToast2(GoodsListForBuyerActivity.this, R.string.one_page);
//                    i = 0;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//
//
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
//                    k = 10 * i;
//                    myCommodities.clear();
//                    myCommodities = CursorUtils.selectTypeforBuyer(GoodsListForBuyerActivity.this, k);
//                    getData();
//                    showList();
//                    int p = i + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(GoodsListForBuyerActivity.this, R.string.last_page);
//                    i = (int) page;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//
//    }

    /**
     * 停止刷新
     */
//    private void onLoad() {
//        goods_list_lv.stopRefresh();
//        goods_list_lv.stopLoadMore();
//    }


}
