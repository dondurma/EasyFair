package com.baibeiyun.eazyfair.activity.supplier.offer;

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
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.supplier.mygoods.NewGoodsActivityone;
import com.baibeiyun.eazyfair.adapter.OfferBillAdapter2;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 商品清单页面
 *
 * @author RuanWei
 * @date 2016/12/21
 **/
public class GoodsListForSupplierActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private EditText input_goods_name_et;
    private ListView goods_lv;
    private TextView add_tv;
    private OfferBillAdapter2 offerBillAdapter2;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private Map<Integer, Integer> map = new HashMap<>();
    private ArrayList<Integer> listForSupplier = new ArrayList<>();//将该map集合加到list集合中
    private List<CheckBox> mlist = new ArrayList<>();//存放CheckBox
    private ArrayList<MyCommodity> myCommodites = new ArrayList<>();

    private String search;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private RelativeLayout new_goods_rl;


    private PopupWindow mPopupWindow;
    private int from = 0;
    private String time;
    private String quote_type;
    private SharedPreferences pre;


    private TextView current_page_tv;//当前第几页
    private TextView total_page_tv;//总页数

    private int k = 0;//默认一开始从第一条开始查询

    private int i = 0;//当前页数

    //计算总的条数
    private ArrayList<MyCommodity> myCommoditiesforNumber = new ArrayList<>();

    private double page;
    private int size;//总的条数
    private Handler mHandler;
    private LinearLayout show_page_ll;
    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list_for_supplier);
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
//                jump(GoodsListForSupplierActivity.this);
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
                            myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, k);
                            getData();
                            showList();
                            int p = i + 1;
                            current_page_tv.setText(p + "");
                        } else {
                            ToastUtil.showToast2(GoodsListForSupplierActivity.this, R.string.one_page);
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
                            myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, k);
                            getData();
                            showList();
                            int p = i + 1;
                            current_page_tv.setText(p + "");
                        } else {
                            ToastUtil.showToast2(GoodsListForSupplierActivity.this, R.string.last_page);
                            i = (int) page;
                        }
                    }
                }, 1000);
                show_page_ll.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getData() {
        datas.clear();
        for (MyCommodity myCommodity : myCommodites) {
            Map<String, Object> data = new ArrayMap<>();
            byte[] product_imgs1 = myCommodity.getProduct_imgs1();
            Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
            data.put("goods_picture_iv", bitmap);
            data.put("goods_name_tv", myCommodity.getName());
            data.put("goods_price_tv", myCommodity.getPrice());
            data.put("_id", myCommodity.get_id());
            data.put("currency_variety", myCommodity.getCurrency_variety());
            datas.add(data);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (listForSupplier != null) {
            listForSupplier.clear();
            listForSupplier = null;
        }
        if (mlist != null) {
            mlist.clear();
            mlist = null;
        }
        if (myCommodites != null) {
            myCommodites.clear();
            myCommodites = null;
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(GoodsListForSupplierActivity.this);
        if (language != null) {
            tag = this.language.getTag();
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
        input_goods_name_et = (EditText) findViewById(R.id.input_goods_name_et);
        goods_lv = (ListView) findViewById(R.id.goods_lv);
//        goods_lv.setPullLoadEnable(true);//允许上拉加载
//        goods_lv.setPullRefreshEnable(true);
//        goods_lv.setXListViewListener(this);
        mHandler = new Handler();

        add_tv = (TextView) findViewById(R.id.add_tv);
        new_goods_rl = (RelativeLayout) findViewById(R.id.new_goods_rl);

        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        fanhui_rl.setOnClickListener(GoodsListForSupplierActivity.this);
        add_tv.setOnClickListener(GoodsListForSupplierActivity.this);
        new_goods_rl.setOnClickListener(GoodsListForSupplierActivity.this);

        input_goods_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = charSequence.toString().trim();
                myCommodites.clear();
                myCommodites = CursorUtils.selectMoHuBySup(GoodsListForSupplierActivity.this, search);
                getData();
                offerBillAdapter2.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, 0);
        getData();
        offerBillAdapter2 = new OfferBillAdapter2(datas, GoodsListForSupplierActivity.this, map, mlist);
        goods_lv.setAdapter(offerBillAdapter2);
        pre = getSharedPreferences("mypre", MODE_PRIVATE);

        myCommoditiesforNumber = CursorUtils.selectNumbersBySup(GoodsListForSupplierActivity.this);
        size = myCommoditiesforNumber.size();
        page = Math.ceil((double) size / (double) 10);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (page != 0) {
            total_page_tv.setText(decimalFormat.format(page));
        } else {
            total_page_tv.setText("1");
        }
        current_page_tv.setText("1");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                OfferListActivity.activity.finish();
                finish();
                break;
            //确认添加
            case R.id.add_tv:
                //得到选中的商品id的集合
                getGoodsData();
                if (listForSupplier.size() != 0 && listForSupplier.size() != 1) {
                    int toast = pre.getInt("toast", 0);
                    if (toast == 1) {
                        getIntent().putExtra("idforGoods", listForSupplier);
                        getIntent().putExtra("quote_type", quote_type);
                        getIntent().putExtra("quote_time", time);
                        setResult(1, getIntent());
                        finish();
                    } else {
                        showAlertDialog(GoodsListForSupplierActivity.this, R.string.confirm_tjh);
                    }
                } else if (listForSupplier.size() != 0 && listForSupplier.size() == 1) {
                    selectQuoteType();
                } else {
                    Alerter.create(GoodsListForSupplierActivity.this)
                            .setText(R.string.please_add_goods)
                            .setDuration(800)
                            .show();
                }
                break;
            //新建商品
            case R.id.new_goods_rl:
                Intent intent = new Intent(GoodsListForSupplierActivity.this, NewGoodsActivityone.class);
                startActivity(intent);
                break;

        }
    }

    private void showList() {
        offerBillAdapter2 = new OfferBillAdapter2(datas, GoodsListForSupplierActivity.this, map, mlist);
        goods_lv.setAdapter(offerBillAdapter2);
        offerBillAdapter2.notifyDataSetChanged();
    }


    //将map集合加到list集合中的方法
    public void getGoodsData() {
        if (map == null) return;
        Set<Integer> integers = map.keySet();
        for (Integer key : integers) {
            Integer id = map.get(key);
            listForSupplier.add(id);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myCommoditiesforNumber = CursorUtils.selectNumbersBySup(GoodsListForSupplierActivity.this);
        size = myCommoditiesforNumber.size();
        page = Math.ceil((double) size / (double) 10);//总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
        myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, k);
    }

    //返回按钮的监听事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            OfferListActivity.activity.finish();//finish前面一个页面
            finish();
            return false;
        }
        return false;
    }


    //上传图片的PopupWindow弹出的方向
    private enum Location {
        BOTTOM
    }

    // 设置添加屏幕的背景透明度
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
            backgroundAlpha(GoodsListForSupplierActivity.this, 1f);
        }
    }


    private void selectQuoteType() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_selectquotetype, null);
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
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_goods_activitythree, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(GoodsListForSupplierActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        //选择报价类型
        RadioGroup radioGroup = (RadioGroup) popupWindowView.findViewById(R.id.quote_type_rg);
        final RadioButton readly_quote_rb = (RadioButton) popupWindowView.findViewById(R.id.readly_quote_rb);
        final RadioButton yuliu_quote_rb = (RadioButton) popupWindowView.findViewById(R.id.yuliu_quote_rb);
        final RadioButton liuyang_quote_rb = (RadioButton) popupWindowView.findViewById(R.id.liuyang_quote_rb);
        LinearLayout select_time_ll = (LinearLayout) popupWindowView.findViewById(R.id.select_time_ll);
        final TextView select_time_tv = (TextView) popupWindowView.findViewById(R.id.select_time_tv);
        Button cancel = (Button) popupWindowView.findViewById(R.id.cancel_bt);
        Button confirm_bt = (Button) popupWindowView.findViewById(R.id.confirm_bt);
        final View view = popupWindowView.findViewById(R.id.view);
        final LinearLayout quote_time_ll = (LinearLayout) popupWindowView.findViewById(R.id.quote_time_ll);
        final String date = DateUtil.getDate();
        String[] split = date.split("\\-");
        final String year = split[0];
        final String month = split[1];
        final String day = split[2];
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == readly_quote_rb.getId()) {
                    quote_type = "1";
                    view.setVisibility(View.GONE);
                    quote_time_ll.setVisibility(View.GONE);
                    time = date;
                } else if (i == yuliu_quote_rb.getId()) {
                    quote_type = "2";
                    view.setVisibility(View.VISIBLE);
                    quote_time_ll.setVisibility(View.VISIBLE);
                } else if (i == liuyang_quote_rb.getId()) {
                    quote_type = "3";
                    view.setVisibility(View.VISIBLE);
                    quote_time_ll.setVisibility(View.VISIBLE);
                }
            }
        });

        //选择报价时间
        select_time_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_time_tv.setText(time);
                DatePickerDialog dpd = new DatePickerDialog(GoodsListForSupplierActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                int toast = pre.getInt("toast", 0);
                if (toast == 1) {
                    getIntent().putExtra("idforGoods", listForSupplier);
                    getIntent().putExtra("quote_type", quote_type);
                    getIntent().putExtra("quote_time", time);
                    setResult(1, getIntent());
                    finish();
                } else {
                    showAlertDialog(GoodsListForSupplierActivity.this, R.string.confirm_tjh);
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

    private void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getIntent().putExtra("idforGoods", listForSupplier);
                getIntent().putExtra("quote_type", quote_type);
                getIntent().putExtra("quote_time", time);
                setResult(1, getIntent());

                finish();
                dialog.dismiss();
            }
        });
        String prompt = null;
        if (tag.equals("ch")) {
            prompt = "不再提示!";
        } else if (tag.equals("en")) {
            prompt = "Do not remind again!";
        }
        builder.setNegativeButton(prompt, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor edit = pre.edit();
                edit.putInt("toast", 1);
                edit.commit();
                dialog.dismiss();
                getIntent().putExtra("idforGoods", listForSupplier);
                getIntent().putExtra("quote_type", quote_type);
                getIntent().putExtra("quote_time", time);
                setResult(1, getIntent());
                finish();

            }
        });
        builder.create().show();
    }


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
        jump_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                Integer integer = Integer.valueOf(trim);
                i = integer - 1;
                if (integer <= page && integer != 0) {
                    int r = (integer - 1) * 10;
                    myCommodites.clear();
                    myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, r);
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

//    //上一页
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show_page_ll.setVisibility(View.GONE);
//                i--;
//                if (i >= 0) {
//                    k = 10 * i;
//                    myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, k);
//                    getData();
//                    showList();
//                    int p = i + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(GoodsListForSupplierActivity.this, R.string.one_page);
//                    i = 0;
//                    onLoad();
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
//                i++;
//                if (i < page) {
//                    k = 10 * i;
//                    myCommodites = CursorUtils.selectTypeforSup(GoodsListForSupplierActivity.this, k);
//                    getData();
//                    showList();
//                    int p = i + 1;
//                    current_page_tv.setText(p + "");
//                    onLoad();
//                } else {
//                    ToastUtil.showToast2(GoodsListForSupplierActivity.this, R.string.last_page);
//                    i = (int) page;
//                    onLoad();
//                }
//            }
//        }, 1000);
//        show_page_ll.setVisibility(View.VISIBLE);
//
//    }

//    /**
//     * 停止刷新，
//     */
//    private void onLoad() {
//        goods_lv.stopRefresh();
//        goods_lv.stopLoadMore();
//    }

}
