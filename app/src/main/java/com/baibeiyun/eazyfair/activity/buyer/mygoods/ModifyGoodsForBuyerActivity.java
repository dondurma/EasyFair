package com.baibeiyun.eazyfair.activity.buyer.mygoods;

import android.app.Activity;
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
import com.baibeiyun.eazyfair.adapter.MyDiyAdapterforBuyer;
import com.baibeiyun.eazyfair.dao.MyCommodityDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CreameBitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.hzw.graffiti.GraffitiActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ModifyGoodsForBuyerActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private EditText input_goods_number;//公司货号
    private EditText input_goods_name;//商品名称
    private EditText input_goods_unit_et;
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
    private LinearLayout business_img;//图张图片路径的父控件id
    private ImageView image1_iv, image2_iv, image3_iv, image4_iv;//四张图片的id
    private Button keep_bt;//保存按钮


    private MyDiyAdapterforBuyer adapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private int from = 0;
    private MyCommodity myCommodity;
    private int id;
    private String self_defined;//从数据库中获得自定义数据的字符串

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

    public static final int REQ_CODE_GRAFFITI = 101;
    public static final int REQ_CODE_GRAFFITI_EDIT_ONE = 102;
    public static final int REQ_CODE_GRAFFITI_EDIT_TWO = 103;
    public static final int REQ_CODE_GRAFFITI_EDIT_THREE = 104;
    public static final int REQ_CODE_GRAFFITI_EDIT_FOUR = 105;

    private List<String> dropList = new ArrayList<>();//popuWindow中list
    private PopupWindow popupWindow;

    private EditText input_moq_et;//最少起订量
    private EditText input_weight_et;//商品重量
    private TextView unit_tv;//商品重量单位
    //外箱包装
    private EditText outbox_amout_et;
    private EditText outbox_size_et;
    private EditText outbox_weight_et;
    private TextView outbox_weight_unit_tv;
    //中盒包装
    private EditText centerbox_amout_et;
    private EditText centerbox_size_et;
    private EditText centerbox_weight_et;
    private TextView centerbox_weight_unit_tv;
    private LinearLayout outbox_weight_unit_ll;
    private LinearLayout centerbox_weight_unit_ll;
    private LinearLayout goods_unit_ll;//重量单位
    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_goods_for_buyer);
        initYuyan();
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        selectById();

        adapter = new MyDiyAdapterforBuyer(datas, ModifyGoodsForBuyerActivity.this);
        diy_add_lv.setAdapter(adapter);
        //将myCommodity对象中的数据依次设置到对应的控件上
        input_goods_number.setText(myCommodity.getSerial_number());//公司货号
        input_goods_name.setText(myCommodity.getName());//商品名称
        input_goods_unit_et.setText(myCommodity.getUnit());//商品单位
        input_goods_material.setText(myCommodity.getMaterial());//商品材质
        input_goods_color.setText(myCommodity.getColor());//商品颜色
        input_goods_price.setText(String.valueOf(myCommodity.getPrice()));//商品价格
        huobi_tv.setText(myCommodity.getCurrency_variety());//货币类型
        tiaokuan_tv.setText(myCommodity.getPrice_clause());//价格条款
        if (myCommodity.getPrice_clause_diy() != null && !myCommodity.getPrice_clause_diy().equals("")) {
            input_address.setVisibility(View.VISIBLE);
            input_address.setText(myCommodity.getPrice_clause_diy());//价格条款自定义
        }
        input_goods_beizhu.setText(myCommodity.getRemark());//商品备注
        input_goods_introduction.setText(myCommodity.getIntroduction());//商品简介
        //得到自定义的item
        //如果为空 则跳过 反之则设置
        if (myCommodity.getDiy() != null) {
            self_defined = myCommodity.getDiy();
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
        //得到四张图片的路径
        byte[] product_imgs1 = myCommodity.getProduct_imgs1();
        byte[] product_imgs2 = myCommodity.getProduct_imgs2();
        byte[] product_imgs3 = myCommodity.getProduct_imgs3();
        byte[] product_imgs4 = myCommodity.getProduct_imgs4();
        Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        image1_iv.setImageBitmap(bitmap1);
        image2_iv.setImageBitmap(bitmap2);
        image3_iv.setImageBitmap(bitmap3);
        image4_iv.setImageBitmap(bitmap4);

        input_moq_et.setText(myCommodity.getMoq());
        input_weight_et.setText(myCommodity.getGoods_weight());
        unit_tv.setText(myCommodity.getGoods_weight_unit());
        outbox_amout_et.setText(myCommodity.getOutbox_number());
        outbox_size_et.setText(myCommodity.getOutbox_size());
        outbox_weight_et.setText(myCommodity.getOutbox_weight());
        outbox_weight_unit_tv.setText(myCommodity.getOutbox_weight_unit());
        centerbox_amout_et.setText(myCommodity.getCenterbox_number());
        centerbox_size_et.setText(myCommodity.getCenterbox_size());
        centerbox_weight_et.setText(myCommodity.getCenterbox_weight());
        centerbox_weight_unit_tv.setText(myCommodity.getCenterbox_weight_unit());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
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
        language = CursorUtils.selectYuYan(ModifyGoodsForBuyerActivity.this);
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
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_goods_number = (EditText) findViewById(R.id.input_goods_number);
        input_goods_name = (EditText) findViewById(R.id.input_goods_name);
        input_goods_unit_et = (EditText) findViewById(R.id.input_goods_unit_et);
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
        business_img = (LinearLayout) findViewById(R.id.business_img);
        image1_iv = (ImageView) findViewById(R.id.image1_iv);
        image2_iv = (ImageView) findViewById(R.id.image2_iv);
        image3_iv = (ImageView) findViewById(R.id.image3_iv);
        image4_iv = (ImageView) findViewById(R.id.image4_iv);
        imageViewList.add(image1_iv);
        imageViewList.add(image2_iv);
        imageViewList.add(image3_iv);
        imageViewList.add(image4_iv);
        keep_bt = (Button) findViewById(R.id.keep_bt);

        input_moq_et = (EditText) findViewById(R.id.input_moq_et);
        input_weight_et = (EditText) findViewById(R.id.input_weight_et);
        unit_tv = (TextView) findViewById(R.id.unit_tv);
        outbox_amout_et = (EditText) findViewById(R.id.outbox_amout_et);
        outbox_size_et = (EditText) findViewById(R.id.outbox_size_et);
        outbox_weight_et = (EditText) findViewById(R.id.outbox_weight_et);
        outbox_weight_unit_tv = (TextView) findViewById(R.id.outbox_weight_unit_tv);
        centerbox_amout_et = (EditText) findViewById(R.id.centerbox_amout_et);
        centerbox_size_et = (EditText) findViewById(R.id.centerbox_size_et);
        centerbox_weight_et = (EditText) findViewById(R.id.centerbox_weight_et);
        centerbox_weight_unit_tv = (TextView) findViewById(R.id.centerbox_weight_unit_tv);
        goods_unit_ll = (LinearLayout) findViewById(R.id.goods_unit_ll);
        outbox_weight_unit_ll = (LinearLayout) findViewById(R.id.outbox_weight_unit_ll);
        centerbox_weight_unit_ll = (LinearLayout) findViewById(R.id.centerbox_weight_unit_ll);

        image1_iv.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        image2_iv.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        image3_iv.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        image4_iv.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        fanhui_rl.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        huobi_ll.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        tiaokuan_ll.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        add.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        upload_image_iv.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        keep_bt.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        goods_unit_ll.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        outbox_weight_unit_ll.setOnClickListener(ModifyGoodsForBuyerActivity.this);
        centerbox_weight_unit_ll.setOnClickListener(ModifyGoodsForBuyerActivity.this);
    }

    @Override
    public void onClick(View view) {
        MyCommodity myCommodity = CursorUtils.selectById2(ModifyGoodsForBuyerActivity.this, id);
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
                //点击保存数据执行修改的操作
                updata();
                finish();
                break;
            //外箱重量单位
            case R.id.outbox_weight_unit_ll:
                showPopuWindow(outbox_weight_unit_ll, 3);
                break;
            //中盒重量单位
            case R.id.centerbox_weight_unit_ll:
                showPopuWindow(centerbox_weight_unit_ll, 4);
                break;
            //重量单位
            case R.id.goods_unit_ll:
                showPopuWindow(goods_unit_ll, 4);
                break;
            case R.id.image1_iv:
                byte[] product_imgs1 = myCommodity.getProduct_imgs1();
                Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
                String path1 = OtherUtils.saveBitmap(this, bitmap1);
                GraffitiActivity.GraffitiParams params1 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params1.mImagePath = path1;
                GraffitiActivity.startActivityForResult(ModifyGoodsForBuyerActivity.this, params1, REQ_CODE_GRAFFITI_EDIT_ONE);
                break;
            case R.id.image2_iv:
                byte[] product_imgs2 = myCommodity.getProduct_imgs2();
                Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
                String path2 = OtherUtils.saveBitmap(this, bitmap2);
                GraffitiActivity.GraffitiParams params2 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params2.mImagePath = path2;
                GraffitiActivity.startActivityForResult(ModifyGoodsForBuyerActivity.this, params2, REQ_CODE_GRAFFITI_EDIT_TWO);


                break;
            case R.id.image3_iv:
                byte[] product_imgs3 = myCommodity.getProduct_imgs3();
                Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
                String path3 = OtherUtils.saveBitmap(this, bitmap3);
                GraffitiActivity.GraffitiParams params3 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params3.mImagePath = path3;
                GraffitiActivity.startActivityForResult(ModifyGoodsForBuyerActivity.this, params3, REQ_CODE_GRAFFITI_EDIT_THREE);


                break;
            case R.id.image4_iv:
                byte[] product_imgs4 = myCommodity.getProduct_imgs4();
                Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
                String path4 = OtherUtils.saveBitmap(this, bitmap4);
                GraffitiActivity.GraffitiParams params4 = new GraffitiActivity.GraffitiParams();
                // 图片路径
                params4.mImagePath = path4;
                GraffitiActivity.startActivityForResult(ModifyGoodsForBuyerActivity.this, params4, REQ_CODE_GRAFFITI_EDIT_FOUR);

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
            popupWindow = new PopupWindow(popupWindowView, android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            popupWindow = new PopupWindow(popupWindowView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_goods_activitythree, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(ModifyGoodsForBuyerActivity.this, 0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
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
                popupWindow.dismiss();
            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(ModifyGoodsForBuyerActivity.this, 1f);
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

                    GraffitiActivity.startActivityForResult(ModifyGoodsForBuyerActivity.this, params, REQ_CODE_GRAFFITI);
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

    //拍照和选择相册的方法
    private void selectGallery() {
        Intent intent = new Intent(ModifyGoodsForBuyerActivity.this, MultiImageSelectorActivity.class);
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

    //修改数据的方法
    private void updata() {
        //获得修改后的数据
        String serial_number = input_goods_number.getText().toString().trim();//公司货号
        String goodsname = input_goods_name.getText().toString().trim();//商品名称
        String goodsunit = input_goods_unit_et.getText().toString().trim();//商品单位
        String goodsmaterial = input_goods_material.getText().toString().trim();//商品材质
        String goodscolor = input_goods_color.getText().toString().trim();//商品颜色
        Double price = Double.valueOf(input_goods_price.getText().toString().trim());//商品价格
        String currency_variety = huobi_tv.getText().toString().trim();//货币类型
        String price_clause = tiaokuan_tv.getText().toString().trim();//价格条款
        String price_clause_diy = null;
        if (!price_clause.equals("EXW")) {
            price_clause_diy = input_address.getText().toString().trim();//价格条款自定义
        } else {
            price_clause_diy = "";
        }
        String remark = input_goods_beizhu.getText().toString().trim();//商品备注
        String introduction = input_goods_introduction.getText().toString().trim();//商品简介
        //获得自定义item的字符串
        StringBuilder stringBuilder = new StringBuilder();
        //得到自定义的listview的item上用户输入的数据
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> stringObjectMap = datas.get(i);
            if (stringObjectMap.get("man") == null | stringObjectMap.get("minus") == null) continue;
            String man = (String) stringObjectMap.get("man");
            String minus = (String) stringObjectMap.get("minus");
            stringBuilder.append(man).append(":").append(minus).append("|");
            self_defined = stringBuilder.toString().substring(0, (stringBuilder.toString().lastIndexOf("|")));
        }
        byte[] product_imgs1 = new byte[0];
        byte[] product_imgs2 = new byte[0];
        byte[] product_imgs3 = new byte[0];
        byte[] product_imgs4 = new byte[0];
        if (allPictureUrl == null) {
            product_imgs1 = myCommodity.getProduct_imgs1();
            product_imgs2 = myCommodity.getProduct_imgs2();
            product_imgs3 = myCommodity.getProduct_imgs3();
            product_imgs4 = myCommodity.getProduct_imgs4();
        } else {
            // 2016/12/7 计算出有几个“|”线
            int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
            //一张图片
            if (i == 0) {
                //将路径转换为bitmap
                Bitmap bitmap = BitmapUtils.compressImageFromFile(allPictureUrl);
                //将bitmap转换为二进制流
                product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap);
                product_imgs2 = myCommodity.getProduct_imgs2();
                product_imgs3 = myCommodity.getProduct_imgs3();
                product_imgs4 = myCommodity.getProduct_imgs4();
                //两张图片
            } else if (i == 1) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = myCommodity.getProduct_imgs1();
                }
                Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                product_imgs3 = myCommodity.getProduct_imgs3();
                product_imgs4 = myCommodity.getProduct_imgs4();

            } else if (i == 2) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = myCommodity.getProduct_imgs1();
                }
                if (!split[1].equals("")) {
                    Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                    product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                } else {
                    product_imgs2 = myCommodity.getProduct_imgs2();
                }
                Bitmap bitmap3 = BitmapUtils.compressImageFromFile(split[2]);
                product_imgs3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                product_imgs4 = myCommodity.getProduct_imgs4();
            } else if (i == 3) {
                String[] split = allPictureUrl.split("\\|");
                if (!split[0].equals("")) {
                    Bitmap bitmap1 = BitmapUtils.compressImageFromFile(split[0]);
                    product_imgs1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                } else {
                    product_imgs1 = myCommodity.getProduct_imgs1();
                }
                if (!split[1].equals("")) {
                    Bitmap bitmap2 = BitmapUtils.compressImageFromFile(split[1]);
                    product_imgs2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                } else {
                    product_imgs2 = myCommodity.getProduct_imgs2();
                }
                if (!split[2].equals("")) {
                    Bitmap bitmap3 = BitmapUtils.compressImageFromFile(split[2]);
                    product_imgs3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                } else {
                    product_imgs3 = myCommodity.getProduct_imgs3();
                }
                Bitmap bitmap4 = BitmapUtils.compressImageFromFile(split[3]);
                product_imgs4 = BitmapUtils.compressBmpFromBmp(bitmap4);

            }
        }
        String create_time = myCommodity.getCreate_time();//录入时间
        int goods_type = myCommodity.getGoods_type();


        String moq = input_moq_et.getText().toString().trim();
        String goodsweight = input_weight_et.getText().toString().trim();
        String goodsweightunit = unit_tv.getText().toString().trim();
        String outboxamout = outbox_amout_et.getText().toString().trim();
        String outboxsize = outbox_size_et.getText().toString().trim();
        String outboxweight = outbox_weight_et.getText().toString().trim();
        String outboxweightunit = outbox_weight_unit_tv.getText().toString().trim();
        String centerbox_amout = centerbox_amout_et.getText().toString().trim();
        String centerbox_size = centerbox_size_et.getText().toString().trim();
        String centerboxweight = centerbox_weight_et.getText().toString().trim();
        String centerboxweightunit = centerbox_weight_unit_tv.getText().toString().trim();

        //构建需要修改的数据
        MyCommodity myCommodity = new MyCommodity(id, serial_number, goodsname, goodsunit,
                goodsmaterial, goodscolor, price, currency_variety, price_clause, price_clause_diy, remark,
                introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                goods_type, create_time, moq, goodsweight, goodsweightunit, outboxamout, outboxsize,
                outboxweight, outboxweightunit, centerbox_amout, centerbox_size, centerboxweight,
                centerboxweightunit);
        MyCommodityDao myCommodityDao = new MyCommodityDao(ModifyGoodsForBuyerActivity.this);
        //修改数据
        int rows = myCommodityDao.updataAll(myCommodity);
        if (rows == 0) {
            Toast.makeText(ModifyGoodsForBuyerActivity.this, R.string.modify_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ModifyGoodsForBuyerActivity.this, R.string.modify_success, Toast.LENGTH_SHORT).show();
        }

    }

    //根据id来查询的方法
    private void selectById() {
        myCommodity = CursorUtils.selectById(ModifyGoodsForBuyerActivity.this, id);
    }

    private void initDate(int status) {
        switch (status) {
            case 1:
                if (tag.equals("ch")) {
                    dropList.add("美元");
                    dropList.add("欧元");
                    dropList.add("英镑");
                    dropList.add("日元");
                    dropList.add("人民币");
                } else if (tag.equals("en")) {
                    dropList.add("Dollar");
                    dropList.add("EUR");
                    dropList.add("GBP");
                    dropList.add("JPY");
                    dropList.add("RMB");
                }
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
            case 4:
                dropList.add("mg");
                dropList.add("g");
                dropList.add("kg");
                dropList.add("t");
                break;
        }
    }

    private void showPopuWindow(final View v, int status) {
        dropList.clear();
        initDate(status);
        View mV = LayoutInflater.from(ModifyGoodsForBuyerActivity.this).inflate(R.layout.listview_popupwindow, null);
        mV.setBackgroundColor(Color.WHITE);
        popupWindow = new PopupWindow(mV, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(mV);
        final ListView listview = (ListView) mV.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(ModifyGoodsForBuyerActivity.this, android.R.layout.simple_list_item_1, dropList);
        listview.setAdapter(adapter);
        popupWindow.setFocusable(true);
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
                    case R.id.outbox_weight_unit_ll://外箱重量单位
                        outbox_weight_unit_tv.setText(s);
                        break;
                    case R.id.centerbox_weight_unit_ll://中盒重量单位
                        centerbox_weight_unit_tv.setText(s);
                        break;
                    case R.id.goods_unit_ll:
                        unit_tv.setText(s);
                        break;

                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(ModifyGoodsForBuyerActivity.this, 0.5f);//0.0-1.0
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(ModifyGoodsForBuyerActivity.this, 1f);
            }
        });
    }

}
