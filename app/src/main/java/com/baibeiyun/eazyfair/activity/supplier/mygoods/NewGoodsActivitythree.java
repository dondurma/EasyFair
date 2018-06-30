package com.baibeiyun.eazyfair.activity.supplier.mygoods;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.MyDiyAdapter;
import com.baibeiyun.eazyfair.dao.MyCommodityDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CreameBitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DateUtil;
import com.baibeiyun.eazyfair.utils.ImageFactoryandOther;
import com.baibeiyun.eazyfair.view.MyDialog;
import com.baibeiyun.eazyfair.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.hzw.graffiti.GraffitiActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class NewGoodsActivitythree extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private EditText input_beizhu_et;//备注
    private EditText input_introduction_et;//商品简介
    private LinearLayout add;//自定义的添加item
    private Button upload_image_iv;//上传图片的Button
    private Button confirm_bt;//确认按钮

    //四张图片的控件
    private ImageView image1_iv, image2_iv, image3_iv, image4_iv;
    private int from = 0;
    private PopupWindow mPopupWindow;


    private MyListView diy_add_lv;
    private MyDiyAdapter adapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();


    private List<ImageView> imageViewList = new ArrayList<>();//存放显示图片的四个ImageView
    private final int REQUEST_IMAGE = 2;
    private final int maxNum = 4;//最多显示四张图片
    private ArrayList<String> mSelectPath;//所选的图片的地址
    private List<Bitmap> bitmapList = new ArrayList<>();//存放所选的图片
    private List<String> finalSelectPaths = new ArrayList<>();//存放最终所选择的图片的地址


    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    public static final int REQ_CODE_GRAFFITI = 101;

    //得到前两个页面传递过来的数据
    private String companynumber, goodsname, goodsmaterial, goodsunit, moq;
    private String goodsweight, goodsweightunit, goodscolor, goodsprice, huobi;
    private String tiaokuan, tiaokuan_diy, outboxamout, outboxsize, outboxweight;
    private String outboxweightunit, centerboxamout, centerboxsize, centerboxweight, centerboxweightunit;

    //得到当前输入的数据
    private String goodsbeizhu, introduction, substring, allPictureUrl;
    private MyDialog myDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 9) {
                myDialog.dialogDismiss();
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods_activitythree);
        initYuyan();
        initView();
        getbeforeData();
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
        myDialog = new MyDialog(this);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_beizhu_et = (EditText) findViewById(R.id.input_beizhu_et);
        input_introduction_et = (EditText) findViewById(R.id.input_introduction_et);
        add = (LinearLayout) findViewById(R.id.add);
        upload_image_iv = (Button) findViewById(R.id.upload_image_iv);
        confirm_bt = (Button) findViewById(R.id.confirm_bt);
        image1_iv = (ImageView) findViewById(R.id.image1_iv);
        image2_iv = (ImageView) findViewById(R.id.image2_iv);
        image3_iv = (ImageView) findViewById(R.id.image3_iv);
        image4_iv = (ImageView) findViewById(R.id.image4_iv);
        fanhui_rl.setOnClickListener(NewGoodsActivitythree.this);
        add.setOnClickListener(NewGoodsActivitythree.this);
        upload_image_iv.setOnClickListener(NewGoodsActivitythree.this);
        confirm_bt.setOnClickListener(NewGoodsActivitythree.this);
        diy_add_lv = (MyListView) findViewById(R.id.diy_add_lv);
        adapter = new MyDiyAdapter(datas, NewGoodsActivitythree.this);
        diy_add_lv.setAdapter(adapter);

        imageViewList.add(image1_iv);
        imageViewList.add(image2_iv);
        imageViewList.add(image3_iv);
        imageViewList.add(image4_iv);
    }

    //得到上个页面传递过来的数据
    private void getbeforeData() {
        Intent intent = getIntent();
        companynumber = intent.getStringExtra("commpanynumber");
        goodsname = intent.getStringExtra("goodsname");
        goodsmaterial = intent.getStringExtra("goodsmaterial");
        goodsunit = intent.getStringExtra("goodsunit");
        moq = intent.getStringExtra("moq");
        goodsweight = intent.getStringExtra("goodsweight");
        goodsweightunit = intent.getStringExtra("goodsweightunit");
        goodscolor = intent.getStringExtra("goodscolor");
        goodsprice = intent.getStringExtra("goodsprice");
        huobi = intent.getStringExtra("huobi");
        tiaokuan = intent.getStringExtra("tiaokuan");
        tiaokuan_diy = intent.getStringExtra("tiaokuan_diy");
        outboxamout = intent.getStringExtra("outboxamout");
        outboxsize = intent.getStringExtra("outboxsize");

        outboxweight = intent.getStringExtra("outboxweight");
        outboxweightunit = intent.getStringExtra("outboxweightunit");
        centerboxamout = intent.getStringExtra("centerboxamout");
        centerboxsize = intent.getStringExtra("centerboxsize");

        centerboxweight = intent.getStringExtra("centerboxweight");
        centerboxweightunit = intent.getStringExtra("centerboxweightunit");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
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
            //确认按钮
            case R.id.confirm_bt:
                myDialog.dialogShow();
                NewGoodsActivityone.activityone.finish();
                NewGoodsActivitytwo.activitytwo.finish();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        saved();

                        Message message = new Message();
                        message.what = 9;
                        handler.sendMessage(message);

                    }
                }).start();


                break;
        }

    }

    //上传图片的PopupWindow弹出的方向
    private enum Location {
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
        backgroundAlpha(NewGoodsActivitythree.this, 0.5f);
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
        //选择照片
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
            backgroundAlpha(NewGoodsActivitythree.this, 1f);
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

                    GraffitiActivity.startActivityForResult(NewGoodsActivitythree.this, params, REQ_CODE_GRAFFITI);
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
                    int size = bitmapList.size();
                    for (int i = 0; i < size; i++) {
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
        }

    }


    //保存用户输入的所有数据 并存入数据库
    private void saved() {
        //创建一个实体类
        MyCommodity myCommodity = new MyCommodity();
        //将用户输入的所有数据加载到myCommodity中
        myCommodity.setSerial_number(companynumber);//公司货号
        myCommodity.setName(goodsname);//商品名
        myCommodity.setUnit(goodsunit);//商品单位
        myCommodity.setMaterial(goodsmaterial);//商品材质
        myCommodity.setColor(goodscolor);//商品颜色
        if (!"".equals(goodsprice)) {
            myCommodity.setPrice(Double.parseDouble(goodsprice));//商品价格
        } else {
            myCommodity.setPrice(0.0);//商品价格
        }

        myCommodity.setCurrency_variety(huobi);
        myCommodity.setPrice_clause(tiaokuan);
        myCommodity.setPrice_clause_diy(tiaokuan_diy);
        myCommodity.setRemark(goodsbeizhu);
        myCommodity.setIntroduction(introduction);//简介
        myCommodity.setDiy(substring);//自定义item

        Bitmap bitmap_no = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
        byte[] bytes_no = BitmapUtils.compressBmpFromBmp(bitmap_no);
        if (allPictureUrl != null) {
            //  2016/12/7 计算出有几个“|”线
            int i = allPictureUrl.length() - allPictureUrl.replace("|", "").length();
            //一张图片
            if (i == 0) {
                Bitmap bitmap = ImageFactoryandOther.getSmallBitmap(allPictureUrl);
                //将bitmap转换为二进制流
                byte[] bytes = BitmapUtils.compressBmpFromBmp(bitmap);
                myCommodity.setProduct_imgs1(bytes);
                myCommodity.setProduct_imgs2(bytes_no);
                myCommodity.setProduct_imgs3(bytes_no);
                myCommodity.setProduct_imgs4(bytes_no);
                //两张图片
            } else if (i == 1) {
                String[] split = allPictureUrl.split("\\|");
                Bitmap bitmap1 = ImageFactoryandOther.getSmallBitmap(split[0]);
                Bitmap bitmap2 = ImageFactoryandOther.getSmallBitmap(split[1]);
                byte[] bytes1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                byte[] bytes2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                myCommodity.setProduct_imgs1(bytes1);
                myCommodity.setProduct_imgs2(bytes2);
                myCommodity.setProduct_imgs3(bytes_no);
                myCommodity.setProduct_imgs4(bytes_no);
            } else if (i == 2) {
                String[] split = allPictureUrl.split("\\|");
                Bitmap bitmap1 = ImageFactoryandOther.getSmallBitmap(split[0]);
                Bitmap bitmap2 = ImageFactoryandOther.getSmallBitmap(split[1]);
                Bitmap bitmap3 = ImageFactoryandOther.getSmallBitmap(split[2]);
                byte[] bytes1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                byte[] bytes2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                byte[] bytes3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                myCommodity.setProduct_imgs1(bytes1);
                myCommodity.setProduct_imgs2(bytes2);
                myCommodity.setProduct_imgs3(bytes3);
                myCommodity.setProduct_imgs4(bytes_no);
            } else if (i == 3) {
                String[] split = allPictureUrl.split("\\|");
                Bitmap bitmap1 = ImageFactoryandOther.getSmallBitmap(split[0]);
                Bitmap bitmap2 = ImageFactoryandOther.getSmallBitmap(split[1]);
                Bitmap bitmap3 = ImageFactoryandOther.getSmallBitmap(split[2]);
                Bitmap bitmap4 = ImageFactoryandOther.getSmallBitmap(split[3]);
                byte[] bytes1 = BitmapUtils.compressBmpFromBmp(bitmap1);
                byte[] bytes2 = BitmapUtils.compressBmpFromBmp(bitmap2);
                byte[] bytes3 = BitmapUtils.compressBmpFromBmp(bitmap3);
                byte[] bytes4 = BitmapUtils.compressBmpFromBmp(bitmap4);
                myCommodity.setProduct_imgs1(bytes1);
                myCommodity.setProduct_imgs2(bytes2);
                myCommodity.setProduct_imgs3(bytes3);
                myCommodity.setProduct_imgs4(bytes4);
            }
        } else {
            myCommodity.setProduct_imgs1(bytes_no);
            myCommodity.setProduct_imgs2(bytes_no);
            myCommodity.setProduct_imgs3(bytes_no);
            myCommodity.setProduct_imgs4(bytes_no);
        }
        String date = DateUtil.getDate();
        myCommodity.setGoods_type(0);//供应商 我的商品
        myCommodity.setCreate_time(date);//商品录入时间
        myCommodity.setMoq(moq);
        myCommodity.setGoods_weight(goodsweight);
        myCommodity.setGoods_weight_unit(goodsweightunit);
        myCommodity.setOutbox_number(outboxamout);
        myCommodity.setOutbox_size(outboxsize);
        myCommodity.setOutbox_weight(outboxweight);
        myCommodity.setOutbox_weight_unit(outboxweightunit);
        myCommodity.setCenterbox_number(centerboxamout);
        myCommodity.setCenterbox_size(centerboxsize);
        myCommodity.setCenterbox_weight(centerboxweight);
        myCommodity.setCenterbox_weight_unit(centerboxweightunit);


        //创建一个方法层的对象
        MyCommodityDao myCommodityDao = new MyCommodityDao(NewGoodsActivitythree.this);
        //执行该方法层中添加的方法
        myCommodityDao.insertDataforAll(myCommodity);

    }

    //得到当前输入的部分数据(除了图片)
    private void getData() {
        goodsbeizhu = input_beizhu_et.getText().toString().trim();
        introduction = input_introduction_et.getText().toString().trim();
        StringBuilder stringBuilder = new StringBuilder();
        //得到自定义的listview的item上用户输入的数据
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> stringObjectMap = datas.get(i);
            if (stringObjectMap.get("man") == null | stringObjectMap.get("minus") == null) continue;
            String man = (String) stringObjectMap.get("man");
            String minus = (String) stringObjectMap.get("minus");
            stringBuilder.append(man).append(":").append(minus).append("|");
            substring = stringBuilder.toString().substring(0, (stringBuilder.toString().lastIndexOf("|")));
        }
    }

    //拍照和选择相册的方法
    private void selectGallery() {
        Intent intent = new Intent(NewGoodsActivitythree.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片,显示
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量 4张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式,选取多张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }


}













