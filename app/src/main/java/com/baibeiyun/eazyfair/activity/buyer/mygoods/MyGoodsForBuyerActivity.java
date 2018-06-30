package com.baibeiyun.eazyfair.activity.buyer.mygoods;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.adapter.MyGoodsListForBuyerAdapter;
import com.baibeiyun.eazyfair.dao.MyCommodityDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.XListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MyGoodsForBuyerActivity extends BaseActivityforBuyer implements View.OnClickListener, XListView.IXListViewListener {
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout buildgoods;//新建商品
    private EditText goods_name_et;//输入 商品名称
    private XListView goods_list_lv;//商品列表
    private TextView goods_number_tv;//列表条数
    private MyGoodsListForBuyerAdapter myGoodsListForBuyerAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private ArrayList<MyCommodity> myCommodities = new ArrayList<>();
    private MyCommodity myCommodity;
    private int flag;
    private int id;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;

    private Language language;

    private TextView current_page_tv;//当前页数
    private TextView total_page_tv;//总页数

    private ArrayList<MyCommodity> myCommoditiesforNumbers = new ArrayList<>();
    private int size;//总的条数

    private int k = 0;//从第几条开始查询
    private int t = 0;//当前页数
    private double page;//总页数
    private Handler mHandler;
    private RelativeLayout show_page_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods_for_buyer);
        aboutYuyan();
        initview();
        initData();
        initFloat();
    }

    private void initFloat() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(MyGoodsForBuyerActivity.this);
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

    }

    //构建数据源
    private void getData() {
        datas.clear();
        int size = myCommodities.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> data = new ArrayMap<>();
            myCommodity = myCommodities.get(i);
            //得到四张图片的路径
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

    private void aboutYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MyGoodsForBuyerActivity.this);
        if (language != null) {
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


    private void initview() {
        show_page_ll = (RelativeLayout) findViewById(R.id.show_page_ll);
        goods_number_tv = (TextView) findViewById(R.id.goods_number_tv);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        buildgoods = (RelativeLayout) findViewById(R.id.buildgoods);
        goods_name_et = (EditText) findViewById(R.id.goods_name_et);
        goods_list_lv = (XListView) findViewById(R.id.goods_list_lv);
        goods_list_lv.setPullLoadEnable(true);//允许上拉加载
        goods_list_lv.setPullRefreshEnable(true);
        goods_list_lv.setXListViewListener(this);
        mHandler = new Handler();
        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        fanhui_rl.setOnClickListener(MyGoodsForBuyerActivity.this);
        buildgoods.setOnClickListener(MyGoodsForBuyerActivity.this);

        //listview的长按监听 删除
        goods_list_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                id = (int) map.get("_id");//得到当前点击的item的表的id
                flag = i;
                showAlertDialog(MyGoodsForBuyerActivity.this, R.string.determine_sc);
                return true;
            }
        });
        //listview的点击监听 进入详情
        goods_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                if (map != null) {
                    Intent intent = new Intent(MyGoodsForBuyerActivity.this, GoodsDetailForBuyerActivity.class);
                    int id = (int) map.get("_id");//得到当前点击的item的表的id
                    intent.putExtra("id", id);
                    intent.putExtra("tag",1);
                    startActivity(intent);
                }


            }
        });
        goods_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //得到用户输入的数据
                String data = charSequence.toString().trim();
                myCommodities.clear();
                myCommodities = CursorUtils.findDataByBuyer(MyGoodsForBuyerActivity.this, data);
                getData();
                myGoodsListForBuyerAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        selectNumbers();
        current_page_tv.setText("1");//设置当前页
        page = Math.ceil((double) size / (double) 10);
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (page != 0) {
            total_page_tv.setText(decimalFormat.format(page));
        } else {
            total_page_tv.setText("1");
        }
        myCommodities = CursorUtils.selectTypeforBuyer(MyGoodsForBuyerActivity.this, 0);
        getData();
        myGoodsListForBuyerAdapter = new MyGoodsListForBuyerAdapter(datas, MyGoodsForBuyerActivity.this);
        goods_list_lv.setAdapter(myGoodsListForBuyerAdapter);
        myGoodsListForBuyerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectNumbers();
        myCommodities = CursorUtils.selectTypeforBuyer(MyGoodsForBuyerActivity.this, k);
        getData();
        myGoodsListForBuyerAdapter.notifyDataSetChanged();
        goods_number_tv.setText(size + "");
        page = Math.ceil((double) size / (double) 10);
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        total_page_tv.setText(decimalFormat.format(page));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //新建商品
            case R.id.buildgoods:
                intent = new Intent(MyGoodsForBuyerActivity.this, NewGoodsOneForBuyerActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showData() {
        myCommodities.clear();
        myCommodities = CursorUtils.selectTypeforBuyer(MyGoodsForBuyerActivity.this, k);
        getData();
        myGoodsListForBuyerAdapter.notifyDataSetChanged();
    }


    private void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                datas.remove(flag - 1);
                //创建一个方法层的对象
                MyCommodityDao myCommodityDao = new MyCommodityDao(MyGoodsForBuyerActivity.this);
                myCommodity.set_id(id);
                myCommodityDao.deletedata(myCommodity);
                myGoodsListForBuyerAdapter.notifyDataSetChanged();
                selectNumbers();
                page = Math.ceil((double) size / (double) 10);
                //去掉末尾的0
                DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                total_page_tv.setText(decimalFormat.format(page));
                goods_number_tv.setText(String.valueOf(size));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void selectNumbers() {
        myCommoditiesforNumbers = CursorUtils.selectGoodsforBuyerNumbers(MyGoodsForBuyerActivity.this);
        size = myCommoditiesforNumbers.size();
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
                t = integer - 1;
                if (integer <= page && integer != 0) {
                    int k = (integer - 1) * 10;
                    myCommodities.clear();
                    myCommodities = CursorUtils.selectTypeforSup(MyGoodsForBuyerActivity.this, k);
                    getData();
                    myGoodsListForBuyerAdapter.notifyDataSetChanged();

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

    //下拉刷新
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                show_page_ll.setVisibility(View.GONE);
                t--;
                if (t >= 0) {
                    k = 10 * t;
                    showData();
                    int p = t + 1;
                    current_page_tv.setText(p + "");
                    onLoad();
                } else {
                    ToastUtil.showToast2(MyGoodsForBuyerActivity.this, R.string.one_page);
                    t = 0;
                    onLoad();
                }
            }
        }, 1000);
        show_page_ll.setVisibility(View.VISIBLE);


    }

    //上拉加载
    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                show_page_ll.setVisibility(View.GONE);
                t++;
                if (t < page) {
                    k = 10 * t;
                    showData();
                    int p = t + 1;
                    current_page_tv.setText(p + "");
                    onLoad();
                } else {
                    ToastUtil.showToast2(MyGoodsForBuyerActivity.this, R.string.last_page);
                    t = (int) page;
                    onLoad();
                }

            }
        }, 1000);
        show_page_ll.setVisibility(View.VISIBLE);


    }

    /**
     * 停止刷新
     */
    private void onLoad() {
        goods_list_lv.stopRefresh();
        goods_list_lv.stopLoadMore();
    }
}
