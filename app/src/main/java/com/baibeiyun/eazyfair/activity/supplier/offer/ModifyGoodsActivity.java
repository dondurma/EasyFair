package com.baibeiyun.eazyfair.activity.supplier.offer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.MyDiyAdapter;
import com.baibeiyun.eazyfair.dao.MyQuoteDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CreameBitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.hzw.graffiti.GraffitiActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 商品信息修改Activity
 *
 * @author RuanWei
 * @date 2016/12/20
 **/
public class ModifyGoodsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private EditText input_goods_number;//公司货号
    private EditText input_goods_name;//商品名称
    private EditText input_goods_size;//商品尺寸
    private EditText input_goods_material;//商品材质
    private EditText input_goods_color;//商品颜色
    private EditText input_goods_price;//商品价格
    private LinearLayout huobi_ll;//货币
    private TextView huobi_tv;//货币
    private LinearLayout tiaokuan_ll;//条款
    private TextView tiaokuan_tv;//条款
    private EditText input_address;//码头地址
    private EditText input_goods_beizhu;//商品备注
    private EditText input_goods_introduction;//商品简介
    private LinearLayout add;//自定义
    private MyListView diy_add_lv;//自定义的listview
    private Button upload_image_iv;//上传图片
    private ImageView image1_iv, image2_iv, image3_iv, image4_iv;//四张图片的id
    private Button keep_bt;//保存按钮

    //PopupWindow定义的五种常见货币
    private String[] name_huobi = {"美元", "欧元", "英镑", "日元", "人民币"};
    private String[] name_huobi_en = {"Dollar", "Euro", "Pound", "Yen", "RMB"};
    //PopupWindow定义的常见条款
    private String[] name_tiaokuan = {"EXW", "FOB", "CFR", "CIF"};
    private LayoutInflater ml;
    private PopupWindow mPopupWindow;
    //得到当前用户修改后的数据
    private String huobi, tiaokuan;
    private MyDiyAdapter adapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private int from = 0;
    private String id;//得到上一个页面传递过来的商品id
    private String self_defined;//从数据库中获得自定义数据的字符串

    private ArrayList<MyQuote> myQuotes = new ArrayList<>();
    private MyQuote myQuote;
    private MyQuoteDao myQuoteDao;

    private List<ImageView> imageViewList = new ArrayList<>();//存放显示图片的四个ImageView
    private final int REQUEST_IMAGE = 2;
    private final int maxNum = 4;//最多显示四张图片
    private ArrayList<String> mSelectPath;//所选的图片的地址
    private List<Bitmap> bitmapList = new ArrayList<>();//存放所选的图片
    private List<String> finalSelectPaths = new ArrayList<>();//存放最终所选择的图片的地址
    private String allPictureUrl;//所有图片的URL拼接

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private String time;
    private String quote_type;

    public static final int REQ_CODE_GRAFFITI = 101;
    public static final int REQ_CODE_GRAFFITI_EDIT_ONE = 102;
    public static final int REQ_CODE_GRAFFITI_EDIT_TWO = 103;
    public static final int REQ_CODE_GRAFFITI_EDIT_THREE = 104;
    public static final int REQ_CODE_GRAFFITI_EDIT_FOUR = 105;


    private EditText input_moq_et;//最少起订量
    private EditText input_weight_et;//商品重量
    private TextView unit_tv;//商品重量单位
    private LinearLayout goods_unit_ll;

    private List<String> dropList = new ArrayList<>();//popuWindow中list
    private EditText outbox_amout_et;
    private EditText outbox_size_et;
    private EditText outbox_weight_et;
    private LinearLayout outbox_weight_unit_ll;
    private TextView outbox_weight_unit_tv;
    private EditText centerbox_amout_et;
    private EditText centerbox_size_et;
    private EditText centerbox_weight_et;
    private LinearLayout centerbox_weight_unit_ll;
    private TextView centerbox_weight_unit_tv;
    private int tag = 0;

    private TextView more_tv;
    private LinearLayout other_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_goods);
        initYuyan();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (myQuotes != null) {
            myQuotes.clear();
            myQuotes = null;
        }
        if (imageViewList != null) {
            imageViewList.clear();
            imageViewList = null;
        }
        if (mSelectPath != null) {
            mSelectPath.clear();
            mSelectPath = null;
        }
        if (bitmapList != null) {
            bitmapList.clear();
            bitmapList = null;
        }
        if (finalSelectPaths != null) {
            finalSelectPaths.clear();
            finalSelectPaths = null;
        }

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
        more_tv= (TextView) findViewById(R.id.more_tv);
        other_ll= (LinearLayout) findViewById(R.id.other_ll);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_goods_number = (EditText) findViewById(R.id.input_goods_number);
        input_goods_name = (EditText) findViewById(R.id.input_goods_name);
        input_goods_size = (EditText) findViewById(R.id.input_goods_size);
        input_goods_material = (EditText) findViewById(R.id.input_goods_material);
        input_goods_color = (EditText) findViewById(R.id.input_goods_color);
        input_goods_price = (EditText) findViewById(R.id.input_goods_price);
        huobi_ll = (LinearLayout) findViewById(R.id.huobi_ll);
        huobi_tv = (TextView) findViewById(R.id.huobi_tv);
        tiaokuan_ll = (LinearLayout) findViewById(R.id.tiaokuan_ll);
        tiaokuan_tv = (TextView) findViewById(R.id.tiaokuan_tv);
        input_address = (EditText) findViewById(R.id.input_address);
        input_goods_beizhu = (EditText) findViewById(R.id.input_goods_beizhu);
        input_goods_introduction = (EditText) findViewById(R.id.input_goods_introduction);
        add = (LinearLayout) findViewById(R.id.add);
        diy_add_lv = (MyListView) findViewById(R.id.diy_add_lv);
        upload_image_iv = (Button) findViewById(R.id.upload_image_iv);
        image1_iv = (ImageView) findViewById(R.id.image1_iv);
        image2_iv = (ImageView) findViewById(R.id.image2_iv);
        image3_iv = (ImageView) findViewById(R.id.image3_iv);
        image4_iv = (ImageView) findViewById(R.id.image4_iv);
        imageViewList.add(image1_iv);
        imageViewList.add(image2_iv);
        imageViewList.add(image3_iv);
        imageViewList.add(image4_iv);

        input_moq_et = (EditText) findViewById(R.id.input_moq_et);
        input_weight_et = (EditText) findViewById(R.id.input_weight_et);
        unit_tv = (TextView) findViewById(R.id.unit_tv);
        goods_unit_ll = (LinearLayout) findViewById(R.id.goods_unit_ll);
        outbox_amout_et = (EditText) findViewById(R.id.outbox_amout_et);
        outbox_size_et = (EditText) findViewById(R.id.outbox_size_et);
        outbox_weight_et = (EditText) findViewById(R.id.outbox_weight_et);
        outbox_weight_unit_ll = (LinearLayout) findViewById(R.id.outbox_weight_unit_ll);
        outbox_weight_unit_tv = (TextView) findViewById(R.id.outbox_weight_unit_tv);
        centerbox_amout_et = (EditText) findViewById(R.id.centerbox_amout_et);
        centerbox_size_et = (EditText) findViewById(R.id.centerbox_size_et);
        centerbox_weight_et = (EditText) findViewById(R.id.centerbox_weight_et);
        centerbox_weight_unit_ll = (LinearLayout) findViewById(R.id.centerbox_weight_unit_ll);
        centerbox_weight_unit_tv = (TextView) findViewById(R.id.centerbox_weight_unit_tv);
        keep_bt = (Button) findViewById(R.id.keep_bt);
        image1_iv.setOnClickListener(ModifyGoodsActivity.this);
        image2_iv.setOnClickListener(ModifyGoodsActivity.this);
        image3_iv.setOnClickListener(ModifyGoodsActivity.this);
        image4_iv.setOnClickListener(ModifyGoodsActivity.this);
        outbox_weight_unit_ll.setOnClickListener(ModifyGoodsActivity.this);
        centerbox_weight_unit_ll.setOnClickListener(ModifyGoodsActivity.this);
        fanhui_rl.setOnClickListener(ModifyGoodsActivity.this);
        huobi_ll.setOnClickListener(ModifyGoodsActivity.this);
        tiaokuan_ll.setOnClickListener(ModifyGoodsActivity.this);
        add.setOnClickListener(ModifyGoodsActivity.this);
        upload_image_iv.setOnClickListener(ModifyGoodsActivity.this);
        keep_bt.setOnClickListener(ModifyGoodsActivity.this);
        goods_unit_ll.setOnClickListener(ModifyGoodsActivity.this);
        more_tv.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");//获取从报价清单传递过来的id
        myQuote = CursorUtils.selectQuoteById(ModifyGoodsActivity.this, id);
        adapter = new MyDiyAdapter(datas, ModifyGoodsActivity.this);
        diy_add_lv.setAdapter(adapter);
        //将myInquiry对象中的数据依次设置到对应的控件上
        input_goods_number.setText(myQuote.getSerial_number());//公司货号
        input_goods_name.setText(myQuote.getGoods_name());//商品名称
        input_goods_size.setText(myQuote.getGoods_unit());//商品单位
        input_goods_material.setText(myQuote.getMaterial());//商品材质
        input_goods_color.setText(myQuote.getColor());//商品颜色
        input_goods_price.setText(String.valueOf(myQuote.getPrice()));//商品价格
        huobi_tv.setText(myQuote.getCurrency_varitety());//货币类型
        if (myQuote.getPrice_clause().equals("FOB") || myQuote.getPrice_clause().equals("CFR") || myQuote.getPrice_clause().equals("CIF")) {
            input_address.setVisibility(View.VISIBLE);
            input_address.setText(myQuote.getPrice_clause_diy());//价格条款自定义部分
        } else if (myQuote.getPrice_clause().equals("EXW")) {
            input_address.setVisibility(View.GONE);
        }
        tiaokuan_tv.setText(myQuote.getPrice_clause());//价格条款
        input_goods_beizhu.setText(myQuote.getRemark());//商品备注
        input_goods_introduction.setText(myQuote.getIntroduction());//商品简介
        //得到自定义的item
        //如果为空 则跳过 反之则设置
        if (myQuote.getSelf_defined() != null) {
            self_defined = myQuote.getSelf_defined();
            String[] split1 = self_defined.split("\\|");
            for (String aSplit1 : split1) {
                Map<String, Object> map1 = new ArrayMap<>();
                int a = aSplit1.indexOf(":");
                map1.put("man", aSplit1.substring(0, a));
                map1.put("minus", aSplit1.substring(a + 1, aSplit1.length()));
                datas.add(map1);
            }
            adapter.notifyDataSetChanged();
        }
        byte[] product_imgs1 = myQuote.getProduct_imgs1();
        byte[] product_imgs2 = myQuote.getProduct_imgs2();
        byte[] product_imgs3 = myQuote.getProduct_imgs3();
        byte[] product_imgs4 = myQuote.getProduct_imgs4();
        Bitmap bitmap = BitmapUtils.Bytes2Bimap(product_imgs1);
        Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs2);
        Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs3);
        Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs4);
        image1_iv.setImageBitmap(bitmap);
        image2_iv.setImageBitmap(bitmap1);
        image3_iv.setImageBitmap(bitmap2);
        image4_iv.setImageBitmap(bitmap3);

        input_moq_et.setText(myQuote.getMoq());
        input_weight_et.setText(myQuote.getGoods_weight());
        unit_tv.setText(myQuote.getGoods_weight_unit());

        outbox_amout_et.setText(myQuote.getOutbox_number());
        outbox_size_et.setText(myQuote.getOutbox_size());
        outbox_weight_et.setText(myQuote.getOutbox_weight());
        outbox_weight_unit_tv.setText(myQuote.getOutbox_weight_unit());
        centerbox_amout_et.setText(myQuote.getCenterbox_number());
        centerbox_size_et.setText(myQuote.getCenterbox_size());
        centerbox_weight_et.setText(myQuote.getCenterbox_weight());
        centerbox_weight_unit_tv.setText(myQuote.getCenterbox_weight_unit());

    }


    @Override
    public void onClick(View view) {
        myQuote = CursorUtils.selectQuoteById(ModifyGoodsActivity.this, id);
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //货币
            case R.id.huobi_ll:
                showPopuWindow(huobi_ll, 1);
                break;
            //条款
            case R.id.tiaokuan_ll:
                showPopuWindow(tiaokuan_ll, 2);
                break;
            //自定义添加item
            case R.id.add:
                Map<String, Object> map1 = new ArrayMap<>();
                map1.put("man", "");
                map1.put("minus", "");
                datas.add(map1);
                adapter.notifyDataSetChanged();
                break;
            //上传图片
            case R.id.upload_image_iv:
                from = Location.BOTTOM.ordinal();
                initPopupWindow();
                break;
            //保存
            case R.id.keep_bt:
                if (myQuote.getQuote_type() == null) {
                    //点击保存数据执行修改的操作
                    selectQuoteType();
                    tag = 1;
                } else {
                    updata();
                    tag = 2;
                }

                break;
            //商品单位
            case R.id.goods_unit_ll:
                showPopuWindow(goods_unit_ll, 3);
                break;
            //外箱重量单位
            case R.id.outbox_weight_unit_ll:
                showPopuWindow(outbox_weight_unit_ll, 3);
                break;
            //中盒重量单位
            case R.id.centerbox_weight_unit_ll:
                showPopuWindow(centerbox_weight_unit_ll, 3);
                break;
            case R.id.image1_iv:
                byte[] product_imgs1 = myQuote.getProduct_imgs1();
                Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
                String path1 = OtherUtils.saveBitmap(this, bitmap1);
                GraffitiActivity.GraffitiParams params1 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params1.mImagePath = path1;
                GraffitiActivity.startActivityForResult(ModifyGoodsActivity.this, params1, REQ_CODE_GRAFFITI_EDIT_ONE);
                break;
            case R.id.image2_iv:
                byte[] product_imgs2 = myQuote.getProduct_imgs2();
                Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
                String path2 = OtherUtils.saveBitmap(this, bitmap2);
                GraffitiActivity.GraffitiParams params2 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params2.mImagePath = path2;
                GraffitiActivity.startActivityForResult(ModifyGoodsActivity.this, params2, REQ_CODE_GRAFFITI_EDIT_TWO);

                break;
            case R.id.image3_iv:
                byte[] product_imgs3 = myQuote.getProduct_imgs3();
                Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
                String path3 = OtherUtils.saveBitmap(this, bitmap3);
                GraffitiActivity.GraffitiParams params3 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params3.mImagePath = path3;
                GraffitiActivity.startActivityForResult(ModifyGoodsActivity.this, params3, REQ_CODE_GRAFFITI_EDIT_THREE);

                break;
            case R.id.image4_iv:
                byte[] product_imgs4 = myQuote.getProduct_imgs4();
                Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
                String path4 = OtherUtils.saveBitmap(this, bitmap4);
                GraffitiActivity.GraffitiParams params4 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params4.mImagePath = path4;
                GraffitiActivity.startActivityForResult(ModifyGoodsActivity.this, params4, REQ_CODE_GRAFFITI_EDIT_FOUR);

                break;
            //更多
            case R.id.more_tv:
                more_tv.setVisibility(View.GONE);
                other_ll.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showPopuWindow(final View v, int status) {
        dropList.clear();
        initDate(status);
        View mV = LayoutInflater.from(ModifyGoodsActivity.this).inflate(R.layout.listview_popupwindow, null);
        mV.setBackgroundColor(Color.WHITE);
        mPopupWindow = new PopupWindow(mV, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(mV);
        final ListView listview = (ListView) mV.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(ModifyGoodsActivity.this, android.R.layout.simple_list_item_1, dropList);
        listview.setAdapter(adapter);
        mPopupWindow.setFocusable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = dropList.get(position);
                switch (v.getId()) {
                    case R.id.huobi_ll:  //货币单位
                        huobi_tv.setText(s);
                        break;
                    case R.id.tiaokuan_ll://价格条款
                        tiaokuan_tv.setText(s);
                        if (!s.equals("EXW")) {
                            input_address.setVisibility(View.VISIBLE);
                        } else {
                            input_address.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case R.id.goods_unit_ll:
                        unit_tv.setText(s);
                        break;
                    case R.id.outbox_weight_unit_ll:
                        outbox_weight_unit_tv.setText(s);
                        break;
                    case R.id.centerbox_weight_unit_ll:
                        centerbox_weight_unit_tv.setText(s);
                        break;


                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(ModifyGoodsActivity.this, 0.5f);//0.0-1.0
        mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(ModifyGoodsActivity.this, 1f);
            }
        });
    }

    private void initDate(int status) {
        switch (status) {
            case 1:
                dropList.add("美元");
                dropList.add("欧元");
                dropList.add("英镑");
                dropList.add("日元");
                dropList.add("人民币");
                break;
            case 2:
                dropList.add("EXW");
                dropList.add("FOB");
                dropList.add("CFR");
                dropList.add("CIF");
                break;
            case 3:
                dropList.add("mg");
                dropList.add("g");
                dropList.add("kg");
                dropList.add("t");
                break;

        }
    }


    // 设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //上传图片的PopupWindow弹出的方向
    public enum Location {
        BOTTOM
    }

    //上传图片弹出的popupWindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_upload, null);
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
        backgroundAlpha(ModifyGoodsActivity.this, 0.5f);
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
                bitmapList.clear();
                selectGallery();
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
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(ModifyGoodsActivity.this, 1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                String tag = data.getStringExtra("Tag");
                if (mSelectPath.size() == 1 && tag.equals("2")) {
                    // 涂鸦参数
                    GraffitiActivity.GraffitiParams params = new GraffitiActivity.GraffitiParams();
                    // 图片路径
                    params.mImagePath = mSelectPath.get(0);

                    GraffitiActivity.startActivityForResult(ModifyGoodsActivity.this, params, REQ_CODE_GRAFFITI);
                } else if (mSelectPath.size() >= 1 && tag.equals("1")) {
                    if (mSelectPath.size() > 4) {
                        mSelectPath.remove(0);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
                        //加入最后选择的集合
                        finalSelectPaths.add(p);
                        sb.append(p + "|");
                        String picstring = sb.toString();
                        if (!picstring.isEmpty()) {
                            allPictureUrl = picstring.substring(0, picstring.length() - 1);
                        }
                        //加载缩略图
                        Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(p, 200, 240);
                        bitmapList.add(bitmap);
                    }
                    for (int i = 0; i < bitmapList.size(); i++) {
                        imageViewList.get(i).setImageBitmap(bitmapList.get(i));
                    }
                }

            }
        } else if (requestCode == REQ_CODE_GRAFFITI) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                if (mSelectPath == null || mSelectPath.size() == 0) {
                    return;
                } else if (mSelectPath.size() == 1) {
                    Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(path, 200, 240);
                    image1_iv.setImageBitmap(bitmap);
                    allPictureUrl = path;
                } else {
                    Bitmap bitmap = CreameBitmapUtils.decodeSampledBitmapFromSd(path, 200, 240);
                    image1_iv.setImageBitmap(bitmap);
                    allPictureUrl = allPictureUrl + "|" + path;

                }
            } else if (resultCode == GraffitiActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_ONE) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);

                if (allPictureUrl == null) {
                    allPictureUrl = path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //一张图片
                    if (i == 0) {
                        allPictureUrl = path;
                        //两张图片
                    } else if (i == 1) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = path + "|" + split[1];
                        //三张图片
                    } else if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = path + "|" + split[1] + "|" + split[2];
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = path + "|" + split[1] + "|" + split[2] + "|" + split[3];
                    }
                }


                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image1_iv.setImageBitmap(bm);
            }
            //点击第二张图片
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_TWO) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (allPictureUrl == null) {
                    allPictureUrl = "|" + path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //一张图片
                    if (i == 0) {
                        allPictureUrl = allPictureUrl + "|" + path;
                        //两张图片
                    } else if (i == 1) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + path;
                        //三张图片
                    } else if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + path + "|" + split[2];
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + path + "|" + split[2] + "|" + split[3];
                    }
                }

                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image2_iv.setImageBitmap(bm);
            }
            //三张图片
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_THREE) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);

                if (allPictureUrl == null) {
                    allPictureUrl = "|" + "|" + path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //两张图片
                    if (i == 1) {
                        allPictureUrl = allPictureUrl + "|" + path;
                        //三张图片
                    } else if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + path;
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + path + "|" + split[3];
                    }
                }

                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image3_iv.setImageBitmap(bm);
            }
        } else if (requestCode == REQ_CODE_GRAFFITI_EDIT_FOUR) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (allPictureUrl == null) {
                    allPictureUrl = "|" + "|" + "|" + path;
                } else {
                    int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
                    //三张图片
                    if (i == 2) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + split[2] + path;
                        //四张图片
                    } else if (i == 3) {
                        String[] split = allPictureUrl.split("\\|");
                        allPictureUrl = split[0] + "|" + split[1] + "|" + split[2] + "|" + path;
                    }
                }


                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                image4_iv.setImageBitmap(bm);
            }
        }
    }

    //修改数据的方法
    public void updata() {
        if (time == null) {
            time = "";
        }
        //获得修改后的数据
        String id = myQuote.get_id();
        int commodity_id = myQuote.getCommodity_id();
        String serial_number = input_goods_number.getText().toString().trim();//商品货号
        String goodsname = input_goods_name.getText().toString().trim();//商品名称
        String goodssize = input_goods_size.getText().toString().trim();//商品单位
        String goodsmaterial = input_goods_material.getText().toString().trim();//商品材质
        String goodscolor = input_goods_color.getText().toString().trim();//商品颜色
        Double price = 0.0;
        price = Double.valueOf(input_goods_price.getText().toString().trim());//商品价格

        //货币类型
        String huobi_s = null;
        if (huobi == null) {
            huobi_s = huobi_tv.getText().toString().trim();//货币类型
        } else {
            huobi_s = huobi;
        }
        //价格条款
        String tiaokuan_s = null;
        if (tiaokuan == null) {
            tiaokuan_s = tiaokuan_tv.getText().toString().trim();//价格条款
        } else {
            tiaokuan_s = tiaokuan;
        }
        //价格条款自定义输入部分
        String address_s = null;
        address_s = input_address.getText().toString().trim();

        String beizhu = input_goods_beizhu.getText().toString().trim();//商品备注

        String introduction = input_goods_introduction.getText().toString().trim();//商品介绍

        //获得自定义item的字符串
        StringBuilder stringBuilder = new StringBuilder();
        //得到自定义的listview的item上用户输入的数据
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> stringObjectMap = datas.get(i);
            if (stringObjectMap.get("man") == null | stringObjectMap.get("minus") == null)
                continue;
            String man = (String) stringObjectMap.get("man");
            String minus = (String) stringObjectMap.get("minus");
            stringBuilder.append(man).append(":").append(minus).append("|");
            self_defined = stringBuilder.toString().substring(0, (stringBuilder.toString().lastIndexOf("|")));
        }

        //商品图片
        byte[] product_imgs1 = new byte[0];
        byte[] product_imgs2 = new byte[0];
        byte[] product_imgs3 = new byte[0];
        byte[] product_imgs4 = new byte[0];

        if (allPictureUrl == null) {
            product_imgs1 = myQuote.getProduct_imgs1();
            product_imgs2 = myQuote.getProduct_imgs2();
            product_imgs3 = myQuote.getProduct_imgs3();
            product_imgs4 = myQuote.getProduct_imgs4();
        } else {
            // 2016/12/7 计算出有几个“|”线
            int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
            //一张图片
            if (i == 0) {
                //将路径转换为bitmap
                Bitmap bitmap = BitmapUtils.compressImageFromFile(allPictureUrl);
                //将bitmap转换为二进制流
                product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap);
                product_imgs2 = myQuote.getProduct_imgs2();
                product_imgs3 = myQuote.getProduct_imgs3();
                product_imgs4 = myQuote.getProduct_imgs4();
                //两张图片
            } else if (i == 1) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = myQuote.getProduct_imgs1();
                }
                Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                product_imgs3 = myQuote.getProduct_imgs3();
                product_imgs4 = myQuote.getProduct_imgs4();

            } else if (i == 2) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = myQuote.getProduct_imgs1();
                }
                if (!split[1].equals("")) {
                    Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                    product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                } else {
                    product_imgs2 = myQuote.getProduct_imgs2();
                }
                Bitmap bitmap3 = BitmapUtils.compressImageFromFile(split[2]);
                product_imgs3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                product_imgs4 = myQuote.getProduct_imgs4();
            } else if (i == 3) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = myQuote.getProduct_imgs1();
                }
                if (!split[1].equals("")) {
                    Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                    product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                } else {
                    product_imgs2 = myQuote.getProduct_imgs2();
                }
                if (!split[2].equals("")) {
                    Bitmap bitmap3 = BitmapUtils.compressImageFromFile(split[2]);
                    product_imgs3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                } else {
                    product_imgs3 = myQuote.getProduct_imgs3();
                }
                Bitmap bitmap4 = BitmapUtils.compressImageFromFile(split[3]);
                product_imgs4 = BitmapUtils.compressBmpFromBmp(bitmap4);

            }
        }
        String buyer_name = myQuote.getBuyer_name();
        String buyer_companyname = myQuote.getBuyer_companyname();
        String buyer_phone = myQuote.getBuyer_phone();
        String uniqued_id = myQuote.getUniqued_id();

        String moq = input_moq_et.getText().toString().trim();
        String goodsweight = input_weight_et.getText().toString().trim();
        String goodsunit = unit_tv.getText().toString().trim();

        String outbox_number = outbox_amout_et.getText().toString().trim();
        String outbox_size = outbox_size_et.getText().toString().trim();
        String outbox_weight = outbox_weight_et.getText().toString().trim();
        String outbox_weight_unit = outbox_weight_unit_tv.getText().toString().trim();
        String centerbox_number = centerbox_amout_et.getText().toString().trim();
        String centerbox_size = centerbox_size_et.getText().toString().trim();
        String centerbox_weight = centerbox_weight_et.getText().toString().trim();
        String centerbox_weight_unit = centerbox_weight_unit_tv.getText().toString().trim();
        if (tag == 1) {
            myQuote = new MyQuote(id, commodity_id, serial_number, goodsname, goodssize, goodsmaterial, goodscolor, price, huobi_s,
                    tiaokuan_s, address_s, beizhu, introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                    buyer_name, buyer_companyname, buyer_phone, quote_type, time, uniqued_id, moq, goodsweight, goodsunit, outbox_number,
                    outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
        } else if (tag == 2) {
            myQuote = new MyQuote(id, commodity_id, serial_number, goodsname, goodssize, goodsmaterial, goodscolor, price, huobi_s,
                    tiaokuan_s, address_s, beizhu, introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                    buyer_name, buyer_companyname, buyer_phone, myQuote.getQuote_type(), myQuote.getQuote_time(), uniqued_id, moq, goodsweight, goodsunit, outbox_number,
                    outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
        }
        myQuoteDao = new MyQuoteDao(ModifyGoodsActivity.this);
        int rows = myQuoteDao.updata(myQuote);
        if (rows == 0) {
            Toast.makeText(ModifyGoodsActivity.this, R.string.modify_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ModifyGoodsActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    //拍照和选择相册的方法
    private void selectGallery() {
        Intent intent = new Intent(ModifyGoodsActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片,显示
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量 30张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式,选取多张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //选择报价类型及报价时间的popupWindow
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
        backgroundAlpha(ModifyGoodsActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(
                new View.OnTouchListener() {
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

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(ModifyGoodsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if (time != null && quote_type != null) {
                    updata();
                    mPopupWindow.dismiss();
                } else {
                    ToastUtil.showToast2(ModifyGoodsActivity.this, R.string.please_xzbjlxjbjsj);
                }
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
}
